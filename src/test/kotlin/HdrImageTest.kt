import org.junit.Test

import org.junit.Assert.*

class HdrImageTest {

    @Test
    fun pixelOffset() {
        val img = HdrImage(10, 20)
        assertTrue(img.pixelOffset(3, 6)==63)
        assertTrue(img.pixelOffset(9, 19) == 10*20 -1) //test for the last pixel (lower right)
    }
}