fun main() {
    println("Hello World!")
    val col1 = Color(1.0F, 2.0F, 3.0F)
    val col2 = Color(1.0F, 2.0F, 3.0F)
    println(col1.isClose(col2))
    val w : Int = 3
    val h : Int = 2
    val img = HdrImage(w, h, Array(w * h) { Color(10F, 0.0F, 0.0F) })
    img.setPixel(0, 0, Color(1.0e1F, 2.0e1F, 3.0e1F)) // Each component is
    img.setPixel(1, 0, Color(4.0e1F, 5.0e1F, 6.0e1F)) // different from any
    img.setPixel(2, 0, Color(7.0e1F, 8.0e1F, 9.0e1F)) // other: important in
    img.setPixel(0, 1, Color(1.0e2F, 2.0e2F, 3.0e2F)) // tests!
    img.setPixel(1, 1, Color(4.0e2F, 5.0e2F, 6.0e2F))
    img.setPixel(2, 1, Color(7.0e2F, 8.0e2F, 9.0e2F))
    img.writeOnFile("test.pfm")
}