import scala.util.Random

object Boot extends App with RandomStringGenerator {
  val node = new OffHeapStorage(100L)
  val node2 = new OnHeapStorage

  def perf(node: StorageBase[String, String]) : Unit = {
    val loop = 50000
    val start = System.currentTimeMillis()
    for (i <- 0 to loop) {
      if (isGet) {
        node.get(nextKey)
      } else {
        node.put(nextKey, next(10000))
      }
    }
    val end = System.currentTimeMillis()
    println(s"cost ${end-start}")
  }
  perf(node)
}


trait RandomStringGenerator {
  def next(len: Int): String = {
    Random.alphanumeric.take(len).mkString("")
  }

  def isGet: Boolean = {
    Random.nextBoolean()
  }

  def nextKey: String = {
    "key" + Random.nextInt() % 100000
  }
}
