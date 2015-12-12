import scala.util.Random

object Boot extends App with RandomStringGenerator {
  val node = new OffHeapStorage(100L)
  val loop = 50000
  val maxString = 400

  val start = System.currentTimeMillis()
  for (i <- 0 to loop) {
    if (isGet) {
      node.get(nextKey)
    } else {
      node.put(nextKey, next(Random.nextInt(maxString)))
    }
  }
  val end = System.currentTimeMillis()
  println(s"cost ${end-start}")
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
