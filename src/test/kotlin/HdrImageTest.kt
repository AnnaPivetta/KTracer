import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteOrder
import java.util.*
import kotlin.test.assertFailsWith

class HdrImageTest {
    //Global variables
    //Content of "reference_le.pfm" (little-endian file) (Int type)
    val LE_RAW_BYTES = arrayOf(
        0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x2d, 0x31, 0x2e, 0x30, 0x0a,
        0x00, 0x00, 0xc8, 0x42, 0x00, 0x00, 0x48, 0x43, 0x00, 0x00, 0x96, 0x43,
        0x00, 0x00, 0xc8, 0x43, 0x00, 0x00, 0xfa, 0x43, 0x00, 0x00, 0x16, 0x44,
        0x00, 0x00, 0x2f, 0x44, 0x00, 0x00, 0x48, 0x44, 0x00, 0x00, 0x61, 0x44,
        0x00, 0x00, 0x20, 0x41, 0x00, 0x00, 0xa0, 0x41, 0x00, 0x00, 0xf0, 0x41,
        0x00, 0x00, 0x20, 0x42, 0x00, 0x00, 0x48, 0x42, 0x00, 0x00, 0x70, 0x42,
        0x00, 0x00, 0x8c, 0x42, 0x00, 0x00, 0xa0, 0x42, 0x00, 0x00, 0xb4, 0x42
    )
    //Conversion to bytes
    val LE_REFERENCE_BYTES = ByteArray(LE_RAW_BYTES.size){ i -> LE_RAW_BYTES[i].toByte()}

    //Content of "reference_be.pfm" (big-endian file) (Int type)
    val BE_RAW_BYTES = arrayOf(
    0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x31, 0x2e, 0x30, 0x0a, 0x42,
    0xc8, 0x00, 0x00, 0x43, 0x48, 0x00, 0x00, 0x43, 0x96, 0x00, 0x00, 0x43,
    0xc8, 0x00, 0x00, 0x43, 0xfa, 0x00, 0x00, 0x44, 0x16, 0x00, 0x00, 0x44,
    0x2f, 0x00, 0x00, 0x44, 0x48, 0x00, 0x00, 0x44, 0x61, 0x00, 0x00, 0x41,
    0x20, 0x00, 0x00, 0x41, 0xa0, 0x00, 0x00, 0x41, 0xf0, 0x00, 0x00, 0x42,
    0x20, 0x00, 0x00, 0x42, 0x48, 0x00, 0x00, 0x42, 0x70, 0x00, 0x00, 0x42,
    0x8c, 0x00, 0x00, 0x42, 0xa0, 0x00, 0x00, 0x42, 0xb4, 0x00, 0x00
    )
    //Conversion to bytes
    val BE_REFERENCE_BYTES = ByteArray(BE_RAW_BYTES.size){ i -> BE_RAW_BYTES[i].toByte()}

    @Test
    fun validCoordinates() {
        val img = HdrImage(7, 7)
        assertTrue(img.validCoordinates(0, 2))

    }

    @Test
    fun pixelOffset() {
        val img = HdrImage(10, 20)
        assertTrue(img.pixelOffset(3, 6) == 63)
        assertTrue(img.pixelOffset(9, 19) == 10 * 20 - 1) //test for the last pixel (lower right)
    }

    @Test
    fun setPixel() {
        val img = HdrImage(3, 2)
        val col1 = Color(1.0e1F, 2.0e1F, 3.0e1F)
        val col2 = Color(5.0e1F, 6.0e1F, 7.0e1F)
        img.setPixel(0, 0, col1)
        img.setPixel(1, 1, col2)
        assertTrue(img.getPixel(1, 1) == col2)
    }

    @Test
    fun writePfmFile() {
        val img = HdrImage(3, 2)

        img.setPixel(0, 0, Color(1.0e1F, 2.0e1F, 3.0e1F)) // Each component is
        img.setPixel(1, 0, Color(4.0e1F, 5.0e1F, 6.0e1F)) // different from any
        img.setPixel(2, 0, Color(7.0e1F, 8.0e1F, 9.0e1F)) // other: important in
        img.setPixel(0, 1, Color(1.0e2F, 2.0e2F, 3.0e2F)) // tests!
        img.setPixel(1, 1, Color(4.0e2F, 5.0e2F, 6.0e2F))
        img.setPixel(2, 1, Color(7.0e2F, 8.0e2F, 9.0e2F))

        val buf = ByteArrayOutputStream()
        img.writePfmFile(buf)
        assertTrue(Arrays.equals(buf.toByteArray(), BE_REFERENCE_BYTES))

    }

    @Test
    fun readLine() {
        val img = HdrImage(3, 2)
        val mystring = "Hello\nWorld!"
        val mystream = mystring.byteInputStream()
        assertTrue(img.readLine(mystream) == "Hello")
        assertTrue(img.readLine(mystream) == "World!")
        assertTrue(img.readLine(mystream) == "")
        mystream.close()
    }

    @Test
    fun parseEndianness() {
        val img = HdrImage()
        //Test if correct Endianness is read
        assertTrue(img.parseEndianness("1.0") == ByteOrder.BIG_ENDIAN)
        assertTrue(img.parseEndianness("-1.0") == ByteOrder.LITTLE_ENDIAN)

        //Test if correct exception is thrown when something goes wrong
        assertFailsWith<InvalidPfmFileFormat> { img.parseEndianness("2.0") }
        assertFailsWith<InvalidPfmFileFormat> { img.parseEndianness("a") }
    }

    @Test
    fun parseImgSize() {
        val img = HdrImage()
        assertTrue(img.parseImgSize("18 20") == Pair(18,20))
        assertFailsWith<InvalidPfmFileFormat> { img.parseImgSize("-2 18") }
        assertFailsWith<InvalidPfmFileFormat> { img.parseImgSize("car") }
    }

    @Test
    fun readPfmFile() {
        val bytes = listOf(LE_REFERENCE_BYTES, BE_REFERENCE_BYTES)
        for (rb in bytes) {
            val img =  HdrImage()
            img.readPfmFile  (ByteArrayInputStream(rb))
            assertTrue(img.getWidth() == 3)
            assertTrue(img.getHeight() == 2)

            assertTrue( img.getPixel(0, 0).isClose(Color(1.0e1F, 2.0e1F, 3.0e1F)))
            assertTrue( img.getPixel(1, 0).isClose(Color(4.0e1F, 5.0e1F, 6.0e1F)))
            assertTrue( img.getPixel(2, 0).isClose(Color(7.0e1F, 8.0e1F, 9.0e1F)))
            assertTrue( img.getPixel(0, 1).isClose(Color(1.0e2F, 2.0e2F, 3.0e2F)))
            assertTrue( img.getPixel(0, 0).isClose(Color(1.0e1F, 2.0e1F, 3.0e1F)))
            assertTrue( img.getPixel(1, 1).isClose(Color(4.0e2F, 5.0e2F, 6.0e2F)))
            assertTrue( img.getPixel(2, 1).isClose(Color(7.0e2F, 8.0e2F, 9.0e2F)))
        }
    }

    @Test
    fun normalizeImg() {
        val img = HdrImage(2, 1)

        img.setPixel(0, 0, Color(5.0F,   10.0F,   15.0F))
        img.setPixel(1, 0, Color(500.0F, 1000.0F, 1500.0F))
        img.normalizeImg(1000.0F, 100.0F )

        assertTrue(img.getPixel(0, 0).isClose(Color(0.5e2F, 1.0e2F, 1.5e2F)))
        assertTrue(img.getPixel(1, 0).isClose(Color(0.5e4F, 1.0e4F, 1.5e4F)))
    }

    @Test
    fun clampImg() {
        val img = HdrImage(2, 1)
        img.setPixel(0, 0, Color(5.0F,   10.0F,   15.0F))
        img.setPixel(1, 0, Color(500.0F, 1000.0F, 1500.0F))
        img.clampImg()

        for (p in img.pixels) {
            assertTrue(p.r in 0.0F..1.0F)
            assertTrue(p.g in 0.0F..1.0F)
            assertTrue(p.b in 0.0F..1.0F)
        }


    }

    @Test
    fun averageLuminosity() {
        val img = HdrImage(2, 1)

        img.setPixel(0,0, Color(5.0F, 10.0F, 15.0F))
        img.setPixel(1,0, Color(500.0F, 1000.0F, 1500.0F))

        print(img.averageLuminosity(delta=0.0F))
        assertEquals(100.0F,img.averageLuminosity(delta=0.0F))

    }
}