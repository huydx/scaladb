import scala.offheap._
import java.util.concurrent.ConcurrentHashMap
import scala.util.hashing.MurmurHash3

trait StorageBase[K,V] {
  def put(key: K, value: V): Unit
  def putIfAbscent(key: K, value: V): Unit
  def get(key: K): Option[V]
  def delete(key: K): Unit
}

class OnHeapStorage extends StorageBase[String, String] {
  val store = new ConcurrentHashMap[String, String]()

  override def put(key: String, value: String): Unit = {
    store.put(key, value)
  }

  override def putIfAbscent(key: String, value: String): Unit = {
    store.putIfAbsent(key, value)
  }

  override def get(key: String): Option[String] = {
    store.get(key) match {
      case str if str != null => Some(str)
      case null => None
    }
  }

  override def delete(key: String): Unit = {
    store.remove(key)
  }
}

class OffHeapStorage(val token: Long) extends StorageBase[String, String] {
  implicit val alloc = malloc
  implicit val props = Region.Props(Pool(malloc, pageSize=81920, chunkSize=81920))
  import HashTactics._

  val hashKeyAddress = new ConcurrentHashMap[Long, Long]()
  
  private[this] def hash(key: String)(implicit ev: Hasher[String]): Long = {
    ev.hash(key)
  }

  override def put(key: String, value: String) : Unit = {
    val hashKey = hash(key)
    if (hashKeyAddress.contains(hashKey)) {
      delete(key) //delete first
    }
    val arr = offheap.Array.uninit[Char](value.length)
    for (i <- 0 to value.length - 1) arr(i) = value(i)
    hashKeyAddress.put(hashKey, arr.addr)
  }

  override def putIfAbscent(key: String, value: String): Unit = {
    val keyHash = hash(key)
    if (!hashKeyAddress.contains(keyHash)) {
      put(key, value)
    }
  }

  override def get(key: String): Option[String] = {
    val keyHash = hash(key)
    val addr = hashKeyAddress.get(keyHash)
    if (addr != 0) {
      val arrayFromOffheap = offheap.Array.fromAddr[Char](addr)
      Some(arrayFromOffheap.toArray.mkString(""))
    } else None
  }

  override def delete(key: String): Unit = {
    val keyHash = hash(key)
    if (hashKeyAddress.contains(keyHash)) {
      val addr = hashKeyAddress.get(keyHash)
      alloc.free(addr)
    }
    hashKeyAddress.remove(key)
  }
}

trait Hasher[K] {
  def hash(key: K): Long
}

object HashTactics {
  // 後で配列のキーをサポートなども考える
  implicit val stringHash: Hasher[String] = new Hasher[String] {
    override def hash(key: String): Long = {
      MurmurHash3.stringHash(key)
    }
  }
}
