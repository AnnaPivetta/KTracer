import org.junit.Test

import org.junit.Assert.*

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
       /* img = HdrImage(3, 2)

        img.set_pixel(0, 0, Color(1.0e1, 2.0e1, 3.0e1)) # Each component is
        img.set_pixel(1, 0, Color(4.0e1, 5.0e1, 6.0e1)) # different from any
        img.set_pixel(2, 0, Color(7.0e1, 8.0e1, 9.0e1)) # other: important in
        img.set_pixel(0, 1, Color(1.0e2, 2.0e2, 3.0e2)) # tests!
        img.set_pixel(1, 1, Color(4.0e2, 5.0e2, 6.0e2))
        img.set_pixel(2, 1, Color(7.0e2, 8.0e2, 9.0e2))

        with open("reference.pfm", "wb") as inpf:
        reference_bytes = inpf.readall()

        buf = BytesIO()
        img.write_pfm(buf)
        assert buf.getvalue() == reference_bytes

        */
    }
}