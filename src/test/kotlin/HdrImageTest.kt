import org.junit.Test

import org.junit.Assert.*

class HdrImageTest {

    @Test
    fun validCoordinates() {
        val img = HdrImage(7, 7)
        assertTrue(img.validCoordinates(0,2))
    }
}