import scala.offheap._
import java.util.concurrent.ConcurrentHashMap

trait StorageBase[K,V] {
  implicit val alloc = malloc
  implicit val props = Region.Props(Pool(malloc, pageSize=81920, chunkSize=81920))
  val hashKeyAddress = new ConcurrentHashMap[Long, Long]()

  def putIfAbscent(key: K, value: V): Unit
  def get(key: K): Option[V]
  def delete(key: K): Unit
}

//each node has it own token
class Storage(val token: Long) extends StorageBase[String, String] {
  import HashTactics._

  private[this] def hash(key: String)(implicit ev: Hasher[String]): Long = {
    ev.hash(key)
  }

  private[this] def put(key: String, value: String) : Unit = {
    val vCastStr = value.asInstanceOf[String].toCharArray
    val arr = offheap.Array.uninit[Char](vCastStr.length)
    for (i <- 0 to vCastStr.length - 1) arr(i) = vCastStr(i)
    hashKeyAddress.put(hash(key), arr.addr)
  }

  def putIfAbscent(key: String, value: String): Unit = {
    val keyHash = hash(key)
    if (!hashKeyAddress.contains(keyHash)) {
      put(key, value)
    }
  }

  def get(key: String): Option[String] = {
    val keyHash = hash(key)
    val addr = hashKeyAddress.get(keyHash)
    if (addr != null) {
      val arrayFromOffheap = offheap.Array.fromAddr[Char](addr)
      Some(arrayFromOffheap.toArray.mkString(""))
    } else {
      None
    }
  }

  def delete(key: String): Unit = {
    val keyHash = hash(key)
    val addr = hashKeyAddress.get(keyHash)
    if (addr != null) {
      alloc.free(addr)
    }
  }
}

trait Hasher[K] {
  def hash(key: K): Long
}

object HashTactics {
  implicit val stringHash: Hasher[String] = new Hasher[String] {
    override def hash(key: String): Long = {
      1L
    }
  }

  implicit val longHash: Hasher[Long] = new Hasher[Long] {
    override def hash(key: Long): Long = {
      2L
    }
  }
}
