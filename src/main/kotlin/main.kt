fun main() {
    val w = 10
    val h = 10
    val img = HdrImage(w, h, Array(w * h) { Color(1.0F, 0.0F, 0.0F) })

    img.writeOnFile("red_test.pfm")
}