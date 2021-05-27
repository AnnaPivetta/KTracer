import org.junit.Test

import org.junit.Assert.*

class CheckeredPigmentTest {

    @Test
    fun getColor() {
        val color1 = Color(1.0F, 2.0F, 3.0F)
        val color2 = Color(10.0F, 20.0F, 30.0F)

        val pigment = CheckeredPigment(color1=color1, color2=color2, numOfSteps = 2)

        assertTrue(pigment.getColor(Vector2d(0.25F, 0.25F)).isClose(color1))
        assertTrue(pigment.getColor(Vector2d(0.75F, 0.25F)).isClose(color2))
        assertTrue(pigment.getColor(Vector2d(0.25F, 0.75F)).isClose(color2))
        assertTrue(pigment.getColor(Vector2d(0.75F, 0.75F)).isClose(color1))
    }
}