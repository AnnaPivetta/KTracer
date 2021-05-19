import org.junit.Test

import org.junit.Assert.*

class ImagePigmentTest {

    @Test
    fun getColor() {
        val image = HdrImage(width=2, height=2)
        image.setPixel(0, 0, Color(1.0F, 2.0F, 3.0F))
        image.setPixel(1, 0, Color(2.0F, 3.0F, 1.0F))
        image.setPixel(0, 1, Color(2.0F, 1.0F, 3.0F))
        image.setPixel(1, 1, Color(3.0F, 2.0F, 1.0F))

        val pigment = ImagePigment(image)

        assertTrue(pigment.getColor(Vector2d(0.0F, 0.0F)).isClose(Color(1.0F, 2.0F, 3.0F)))
        assertTrue(pigment.getColor(Vector2d(1.0F, 0.0F)).isClose(Color(2.0F, 3.0F, 1.0F)))
        assertTrue(pigment.getColor(Vector2d(0.0F, 1.0F)).isClose(Color(2.0F, 1.0F, 3.0F)))
        assertTrue(pigment.getColor(Vector2d(1.0F, 1.0F)).isClose(Color(3.0F, 2.0F, 1.0F)))
    }
}