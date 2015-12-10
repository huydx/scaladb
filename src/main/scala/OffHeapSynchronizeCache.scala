import scala.offheap._

class OffHeapSynchronizeCache {
  implicit val alloc = malloc
  implicit val props = Region.Props(Pool(malloc, pageSize=81920, chunkSize=81920))
  //key value concurrent hashmap implement with offheap

}
