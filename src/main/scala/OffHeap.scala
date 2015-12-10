import offheap._

@data class Point(x: Int, y: Int)

@data class Dummy(id: Int) {
  def hello: Unit = println(s"Hello, i'm $id")
}

object OffHeap extends App {
  implicit val alloc = malloc
  implicit val props = Region.Props(Pool(malloc, pageSize = 81920, chunkSize = 81920))

}
