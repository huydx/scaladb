object Boot extends App {
    val node = new Storage(100L)
    node.putIfAbscent("foo", "sadasdad asdas dasd asd asda sdasda sda d")
    node.get("foo")
}
