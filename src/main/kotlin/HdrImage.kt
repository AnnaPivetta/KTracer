import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HdrImage(
    private val width: Int = 0,
    private val height: Int = 0,
    var pixels: Array<Color> = Array(width * height) { Color(0.0F, 0.0F, 0.0F) }
) {
    fun validCoordinates(x: Int, y: Int): Boolean {
        if (x in 0..width && y in 0..height) return true
        else return false
    }

    fun pixelOffset(x: Int, y: Int): Int {
        assert(validCoordinates(x, y))
        return y * width + x
    }

    fun getPixel(x: Int, y: Int): Color {
        return pixels[pixelOffset(x, y)]
    }

    fun setPixel(x: Int, y: Int, newColor: Color) {
        pixels[pixelOffset(x, y)] = newColor
    }

    fun writePfm(stream: OutputStream) {
        val endianness = 1.0
        val header = "PF\n$width $height\n$endianness\n"
        stream.write(header.toByteArray())

        for (y in (height - 1) downTo 0) {
            for (x in 0 until width) {
                val color = getPixel(x, y)
                writeFloatToStream(stream, color.r)
                writeFloatToStream(stream, color.g)
                writeFloatToStream(stream, color.b)
            }
        }
    }

    fun writeOnFile(filename: String) {
        FileOutputStream(filename).use { outStream ->
            writePfm(outStream)
        }
    }

    private fun writeFloatToStream(stream: OutputStream, value: Float) {
        stream.write(ByteBuffer.allocate(4).putFloat(value).array())
    }

}

