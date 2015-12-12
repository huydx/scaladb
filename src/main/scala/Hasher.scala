import scala.util.hashing.MurmurHash3

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
