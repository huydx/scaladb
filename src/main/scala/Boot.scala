object Boot extends App {
    val node = new Node()
    node.putIfAbscent("foo", 2L)
}
