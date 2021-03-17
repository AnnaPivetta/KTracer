fun main() {
    println("Hello World!")
    val col1 = Color(1.0F, 2.0F, 3.0F)
    val col2 = Color(1.0F, 2.0F, 3.0F)
    println(col1.isClose(col2))
    val img = HdrImage(7, 7)
    img.height = 8
}