import akka.actor.Actor
import scala.offheap._

class Node extends Actor {
  implicit val alloc = malloc
  implicit val props = Region.Props(Pool(malloc, pageSize=81920, chunkSize=81920))

  def receive = {
    case _ =>
  }
}
