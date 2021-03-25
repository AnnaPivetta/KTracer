import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

//import sun.nio.cs.UTF_8
//import java.io.FileInputStream
import java.io.*
//import java.lang.StringBuilder
//import java.nio.charset.Charset


class HdrImage(
    private val width: Int = 0,
    private val height: Int = 0,
    var pixels: Array<Color> = Array(width * height) { Color(0.0F, 0.0F, 0.0F) }
) {

    fun readPfmFile(fileIN : String) {

    }

    fun readFloatFromStream (stream : InputStream, endianness : ByteOrder = ByteOrder.BIG_ENDIAN): Float{
        try {
            val bb = ByteBuffer.wrap(stream.readNBytes(4))
            bb.order(endianness)
           return bb.float
        }
        catch (e: java.nio.BufferUnderflowException) {
            throw InvalidPfmFileFormat("Not enough bytes left")
        }
    }

    fun parseEndianness(line: String) : ByteOrder {
        val end : Float
        try {
            end = line.toFloat()
        }
        catch (e: NumberFormatException){
            throw InvalidPfmFileFormat("Endianness specification not found")
        }
        if (end == 1.0F) return ByteOrder.BIG_ENDIAN
        else if (end == -1.0F) return ByteOrder.LITTLE_ENDIAN
        else throw InvalidPfmFileFormat("Invalid Endiannes specification. Value must be 1.0(BE) or -1.0(LE)")
    }

    fun validCoordinates(x: Int, y: Int): Boolean {
        return (x in 0 until width && y in 0 until height)
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

    fun writePfmFile(stream: OutputStream) {
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

    fun saveImg(filename: String) {
        FileOutputStream(filename).use { outStream ->
            writePfmFile(outStream)
        }
    }

    private fun writeFloatToStream(stream: OutputStream, value: Float) {
        stream.write(ByteBuffer.allocate(4).putFloat(value).array())
    }

    fun readLine (stream: InputStream) : String? {
        //return stream.bufferedReader().readLine()
        var result = byteArrayOf()
        while (true) {
            val curbyte = stream.readNBytes(1)
            if (Arrays.equals(curbyte,"".toByteArray()) || curbyte[0] == '\n'.toByte()) return String(result)
            result += curbyte[0]
        }


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

