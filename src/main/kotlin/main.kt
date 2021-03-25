fun main() {
    val w = 10
    val h = 10
    val img = HdrImage(w, h, Array(w * h) { Color(1.0F, 0.0F, 0.0F) })

    img.saveImg("red_test.pfm")
    val mystring = "Questa è una prova\nprobabilmente fallirà\n"
    val mystream=mystring.byteInputStream()
    print(img.readLine(mystream))
    print(img.readLine(mystream))
}