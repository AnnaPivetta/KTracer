import org.junit.After
import org.junit.Test

import org.junit.Assert.*

class ColorTest {

    @After
    fun tearDown() {
    }

    @Test
    fun plus() {
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
    }
}