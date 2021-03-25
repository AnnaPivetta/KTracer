//import sun.nio.cs.UTF_8
//import java.io.FileInputStream
import java.io.*
//import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.util.*


//import java.nio.ByteOrder
//import java.nio.charset.Charset

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

    fun readLine (stream: InputStream) : String {
        return stream.bufferedReader().readLine()

        //stream.bufferedReader().close() NB forse il problema è qui, ma anche chiudendo non funziona
        // capire se chiudendo poi riparte a leggere dalla prima riga


        //val reader = BufferedReader(stream.reader())
        //return reader.readLine()

        //val reader = BufferedReader(InputStreamReader(stream))
        //return reader.readLine()

        //val scan = Scanner (stream)
        //return scan.next()


       /* val content = StringBuilder()
        var bool = true
        while (bool==true) {
            var currentByte = stream.read().toByte()
            if (currentByte != "\n".toByte(hashCode())) {
                content.append(currentByte.toString())
                //bool = true
            }
            else bool = false
        }
        return content.toString()*/
    }

}

