import akka.actor.Actor
import scala.offheap._
import java.util.concurrent.ConcurrentHashMap

class Node[K,V] extends Actor {
  import HashTactics._
  implicit val alloc = malloc
  implicit val props = Region.Props(Pool(malloc, pageSize=81920, chunkSize=81920))

  val hashKeyAddress = new ConcurrentHashMap[Long, Long]()


  @data class NodeElement(Long value)

  def receive = {
    case _ =>
  }

  private[this] def hash(K key)(implicit ev: Hashser[K]): Long = {
    ev.hash(key)
  }

  private[this] def put(K key) : Unit = {
    //allocate value memory by @data class
    //calculate memory address as VAddrr
    //calculate key hash as KHash
    //store [KHash, Vaddrr] to concurrent hashmap
  }

  def putIfAbscent(K key): Unit = {
    //check in concurrent hashmap
    // if not exist
        put(key)
    // else return
  }

  def get(K key): Option[V] = {
    //calculate hash as KHash
    //get Vaddr from concurrent hashmap
    //get adrr from unsafe heap
    //deserialize
  }

  def delete(K key): Unit = {
    //calculate hash as KHash
    //get Vaddr from concurrent hashmap
    //get adrr from unsafe heap
    //free addr
  }

}
