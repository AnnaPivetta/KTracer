import org.junit.Test

import org.junit.Assert.*

class UniformPigmentTest {

    @Test
    fun getColor() {
        val color = Color(10.0F, 2.0F, 3.0F)
        val pigment = UniformPigment(color= color)
        assertTrue(pigment.getColor(Vector2d(0.0F, 0.0F)).isClose(color))
        assertTrue(pigment.getColor(Vector2d(1.0F, 0.0F)).isClose(color))
        assertTrue(pigment.getColor(Vector2d(0.0F, 1.0F)).isClose(color))
        assertTrue(pigment.getColor(Vector2d(1.0F, 1.0F)).isClose(color))

    }
}