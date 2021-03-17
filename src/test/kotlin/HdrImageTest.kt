import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayOutputStream

class HdrImageTest {

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
        val col = Color(1.0e1F, 2.0e1F, 3.0e1F)
        img.setPixel(0, 0, col)
        assertTrue(img.getPixel(0,0) == col)
    }

    @Test
    fun writePfm() {
        val img = HdrImage(3, 2)

        img.setPixel(0, 0, Color(1.0e1F, 2.0e1F, 3.0e1F)) // Each component is
        img.setPixel(1, 0, Color(4.0e1F, 5.0e1F, 6.0e1F)) // different from any
        img.setPixel(2, 0, Color(7.0e1F, 8.0e1F, 9.0e1F)) // other: important in
        img.setPixel(0, 1, Color(1.0e2F, 2.0e2F, 3.0e2F)) // tests!
        img.setPixel(1, 1, Color(4.0e2F, 5.0e2F, 6.0e2F))
        img.setPixel(2, 1, Color(7.0e2F, 8.0e2F, 9.0e2F))

        val referenceBytes = arrayOf (
            "0x50", "0x46", "0x0a", "0x33", "0x20", "0x32", "0x0a", "0x2d", "0x31", "0x2e", "0x30", "0x0a",
            "0x00", "0x00", "0xc8", "0x42", "0x00", "0x00", "0x48", "0x43", "0x00", "0x00", "0x96", "0x43",
            "0x00"," 0x00", "0xc8", "0x43", "0x00", "0x00", "0xfa", "0x43", "0x00", "0x00", "0x16", "0x44",
            "0x00", "0x00", "0x2f", "0x44", "0x00", "0x00", "0x48", "0x44", "0x00", "0x00", "0x61", "0x44",
            "0x00", "0x00", "0x20", "0x41", "0x00", "0x00", "0xa0", "0x41", "0x00", "0x00", "0xf0", "0x41",
            "0x00", "0x00", "0x20", "0x42", "0x00", "0x00", "0x48", "0x42", "0x00", "0x00", "0x70", "0x42",
            "0x00", "0x00", "0xc8", "0x42", "0x00", "0x00", "0x48", "0x43", "0x00", "0x00", "0x96", "0x43"
        )


        val buf = ByteArrayOutputStream()
        img.writePfm(buf)
        assertTrue(buf.toByteArray().equals(referenceBytes))

    }
}