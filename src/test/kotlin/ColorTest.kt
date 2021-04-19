import org.junit.After
import org.junit.Test

import org.junit.Assert.*

class ColorTest {

    @After
    fun tearDown() {
    }

    @Test
    fun plus() {
        val col1 = Color(1.0F, 2.0F, 3.0F)
        val col2 = Color(5.0F, 6.0F, 7.0F)
        val sum = col1 + col2
        assertTrue(sum.isClose(Color(6.0F, 8.0F, 10.0F)))
    }

    @Test
    fun scalarTimes() {
        val a = Color(1.0F, 2.0F, 3.0F )
        val b = 2.0F
        val prod = a*b
        assertTrue(prod.isClose(Color(2.0F, 4.0F, 6.0F)))
    }

    @Test
    fun colorTimes() {
        val a = Color(1.0F, 2.0F,3.0F )
        val b = Color(4.0F, 5.0F,6.0F )
        val prod = a*b
        assertTrue(prod.isClose(Color(4.0F, 10.0F, 18.0F)))
    }

    @Test
    fun isClose() {
        val col1 = Color(1.0F, 2.0F, 3.0F)
        val col2 = Color(1.0F, 2.0F, 3.0F)
        assertTrue(col1.isClose(col2))
    }

    @Test
    fun luminosity() {
        val col1 = Color(1.0F, 2.0F, 3.0F)
        val col2 = Color(9.0F, 5.0F, 7.0F)

        assertEquals(2.0F, col1.luminosity())
        assertEquals(7.0F, col2.luminosity())

    }
}