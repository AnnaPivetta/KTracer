import java.io.FileOutputStream
import java.io.OutputStream

fun main() {
    println("Hello World!")
    val col1 = Color(1.0F, 2.0F, 3.0F)
    val col2 = Color(1.0F, 2.0F, 3.0F)
    println(col1.isClose(col2))
    val img = HdrImage(10, 7, Array(10 * 7) { Color(1F, 0.001F, 0.001F) })
    FileOutputStream("prova.pfm").use{
        outStream -> img.writePfm(outStream)
    }
}