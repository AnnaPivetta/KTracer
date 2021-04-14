import java.awt.image.BufferedImage
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import java.io.*
import javax.imageio.ImageIO
import kotlin.math.log10
import kotlin.math.pow

class HdrImage(
    private var width: Int = 0,
    private var height: Int = 0,
    var pixels: Array<Color> = Array(width * height) { Color(0.0F, 0.0F, 0.0F) }
) {

    /*
    I/O Functions
        readPfmFile     --> read file Pfm from generic InputStream
        readImg         --> Implements the API for reading Image from file
        writePfmFile    --> write file in Pfm format to generic OutputStream
        saveHDRImg      --> Implements the API for writing Image to file in HDR

        Supporting I/O Functions
            readLine            --> read one line at a time from InputStream
            parseEndianness     --> read Endianness from Pfm File Format and check if valid
            parseImgSize        --> read width and height from Pfm File Format and check if valid

        (P) readFloatFromStream --> read one float at a time from InputStream
        (P) writeFloatToStream  --> write the param:Float to OutputStream


    */
    fun readPfmFile(stream: InputStream) {
        val magic = readLine(stream)
        if (magic != "PF") throw InvalidPfmFileFormat("Invalid magic in PFM file")

        val (w, h) = parseImgSize(readLine(stream))
        width = w
        height = h
        val endianness = parseEndianness(readLine(stream))
        pixels = Array(width * height) { Color(0.0F, 0.0F, 0.0F) }
        for (y in (height - 1) downTo 0) {
            for (x in 0 until width) {
                val r = readFloatFromStream(stream, endianness)
                val g = readFloatFromStream(stream, endianness)
                val b = readFloatFromStream(stream, endianness)
                setPixel(x, y, Color(r, g, b))
            }
        }
    }

    fun readImg(fileIN: String) {
        FileInputStream(fileIN).use { INStream ->
            readPfmFile(INStream)
        }
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

    fun saveHDRImg(filename: String) {
        FileOutputStream(filename).use { outStream ->
            writePfmFile(outStream)
        }
    }

    fun readLine(stream: InputStream): String {
        //return stream.bufferedReader().readLine()
        var result = byteArrayOf()
        while (true) {
            val cb = stream.readNBytes(1)   //read Current Byte
            if (Arrays.equals(cb, "".toByteArray()) || cb[0] == '\n'.toByte()) return String(result)
            result += cb[0]
        }

    }

    fun parseEndianness(line: String): ByteOrder {
        val end: Float
        try {
            end = line.toFloat()
        } catch (e: NumberFormatException) {
            throw InvalidPfmFileFormat("Endianness specification not found")
        }
        when (end) {
            1.0F -> return ByteOrder.BIG_ENDIAN
            -1.0F -> return ByteOrder.LITTLE_ENDIAN
            else -> throw InvalidPfmFileFormat("Invalid Endianness specification. Value must be 1.0(BE) or -1.0(LE)")
        }
    }

    fun parseImgSize(line: String): Pair<Int, Int> {
        val elements = line.split(" ")
        if (elements.size != 2) {
            throw InvalidPfmFileFormat("Invalid image size specification")
        }
        val w: Int
        val h: Int
        try {
            w = elements[0].toInt()
            h = elements[1].toInt()
            if (w < 0 || h < 0) {
                throw NumberFormatException("Invalid image size specification: width and height must be >=0")
            }

        } catch (e: NumberFormatException) {
            throw InvalidPfmFileFormat("Invalid image size specification")
        }
        return Pair(w, h)
    }


    private fun readFloatFromStream(stream: InputStream, endianness: ByteOrder = ByteOrder.BIG_ENDIAN): Float {
        try {
            val bb = ByteBuffer.wrap(stream.readNBytes(4))
            bb.order(endianness)
            return bb.float
        } catch (e: java.nio.BufferUnderflowException) {
            throw InvalidPfmFileFormat("Not enough bytes left")
        }
    }

    private fun writeFloatToStream(stream: OutputStream, value: Float) {
        stream.write(ByteBuffer.allocate(4).putFloat(value).array())
    }

    /*
    Conversion to LDR methods
        writeLDRImg         --> saves on stream a LDR image in the given format, with the given gamma correction
        saveLDRImg          --> implements the API for saving LDR image on file

        Supporting conversion functions
            averageLuminosity   --> computes the average luminosity of the image with a logarithmic mean
            normalizeImg        --> rescales each pixel by a factor an the inverse of the averageLuminosity
            clampImg            --> rescales each pixel with the formula x / (1 + x)
        (P) clamp               --> implements the clamping of a single pixel

     */
    fun averageLuminosity(delta: Float = 1e-10F): Float {
        var sum = 0.0F
        for (pix in pixels) {
            sum += log10(delta + pix.luminosity())
        }
        return 10.0F.pow(sum / (pixels.size))
    }

    fun normalizeImg(factor: Float = 0.18F, luminosity: Float? = null) {
        val l = luminosity ?: averageLuminosity()   //If luminosity == null, compute it
        val il = 1.0F / l                                //Inverse of luminosity
        for (p in pixels) {
            p.r *= factor * il
            p.g *= factor * il
            p.b *= factor * il
        }
    }

    fun clampImg() {
        for (p in pixels) {
            p.r = clamp(p.r)
            p.g = clamp(p.g)
            p.b = clamp(p.b)
        }
    }

    private fun clamp(x: Float): Float {
        return x / (1 + x)
    }

    fun writeLDRImg(stream: OutputStream, format: String, gamma: Float = 1.0F) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val intR = (255 * getPixel(x, y).r.pow(1 / gamma)).toInt()
                val intG = (255 * getPixel(x, y).g.pow(1 / gamma)).toInt()
                val intB = (255 * getPixel(x, y).b.pow(1 / gamma)).toInt()
                val rgb = intR.shl(16) + intG.shl(8) + intB
                image.setRGB(x, y, rgb)
            }
        }
        ImageIO.write(image, format, stream)
    }

    fun saveLDRImg(filename: String, format: String, gamma: Float = 1.0F) {
        FileOutputStream(filename).use { outStream ->
            writeLDRImg(outStream, format, gamma)
        }
    }

    /*
    Access methods
        setPixel    --> implements the setter for 1 pixel in image
        getPixel    --> implements the getter for 1 pixel in image
        getWidth    --> implements the getter for image width
        getHeight   --> implements the getter for image height

        Supporting I/O Functions
            validCoordinates   --> check the validity of given coordinates (x, y)
            pixelOffset        --> returns the position in the Pixel Array for the given coordinates


     */
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

    fun getWidth(): Int {
        return width
    }

    fun getHeight(): Int {
        return height
    }


}

