import scala.offheap._
import java.util.concurrent.ConcurrentHashMap

@data class NodeElementL(var value: Long)
@data class NodeElementI(var value: Int)

trait NodeBase[K,V] {
  def putIfAbscent(key: K, value: V): Unit
  def get(key: K): V
  def delete(key: K): Unit
}

class Node extends NodeBase[String, Any] {
  import HashTactics._
  implicit val alloc = malloc
  implicit val props = Region.Props(Pool(malloc, pageSize=81920, chunkSize=81920))

  val hashKeyAddress = new ConcurrentHashMap[Long, Long]()

  private[this] def hash(key: String)(implicit ev: Hasher[String]): Long = {
    ev.hash(key)
  }

  private[this] def put(key: String, value: Any) : Unit = {
    //allocate value memory by @data class
    val obj = NodeElementL(10L) //TODO
    val address = obj.addr
    val keyHash = hash(key)
    hashKeyAddress.put(keyHash, address)
  }

  def putIfAbscent(key: String, value: Any): Unit = {
    //check in concurrent hashmap
    // if not exist
    val keyHash = hash(key)
    if (!hashKeyAddress.contains(keyHash)) {
      put(key, value)
    }
  }

  def get(key: String): Option[Any] = {
    //calculate hash as KHash
    //get Vaddr from concurrent hashmap
    //get adrr from unsafe heap
    //deserialize
    None
  }

  def delete(key: String): Unit = {
    //calculate hash as KHash
    //get Vaddr from concurrent hashmap
    //get adrr from unsafe heap
    //free addr
  }

}
