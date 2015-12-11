import scala.util.MurMurHash3

trait Hasher[K] {
  def hash(key: K): Long
}

object HashTactics {
  implicit object StringHash extends Hasher[String] {
    override def hash(key: String): Long {

    }
  }

  implicit object LongHash extends Hasher[Long] {
    override def hash(key: Long): Long {

    }
  }
}
