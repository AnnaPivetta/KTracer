import org.junit.Test

import org.junit.Assert.*
import kotlin.test.assertFailsWith

class PointTest {

    @Test
    fun testToString() {
        val p = Point(1F, 2F, 3F)
        println(p.toString())
        assertTrue(p.toString()=="(1.0; 2.0; 3.0)")
    }

    @Test
    fun isClose() {
        val a = Point(1.0F, 2.0F, 3.0F)
        val b = Point(4.0F, 6.0F, 8.0F)
        assertTrue( a.isClose(a) )
        assertFalse( a.isClose(b) )
    }

    @Test
    fun plus() {
        val a = Point(1.0F, 2.0F, 3.0F)
        val b = Vector(4.0F, 6.0F, 8.0F)
        val c = Point(5.0F, 8.0F, 11.0F)
        assertTrue((a+b).isClose(c))
    }

    @Test
    fun minus() {
        val a = Point(1.0F, 2.0F, 3.0F)
        val b = Point(4.0F, 6.0F, 8.0F)
        val c = Vector(-3.0F, -4.0F, -5.0F)
        assertTrue((a-b).isClose(c))
    }

    @Test
    fun testMinus() {
        val a = Point(1.0F, 2.0F, 3.0F)
        val b = Vector(4.0F, 6.0F, 8.0F)
        val c = Point(-3.0F, -4.0F, -5.0F)
        assertTrue((a-b).isClose(c))

    }

    @Test
    fun get() {
        val p = Point(0.0F, 1.0F, 2.0F)
        assertTrue(p[0] == 0.0F)
        assertTrue(p[1] == 1.0F)
        assertTrue(p[2] == 2.0F)
        assertFailsWith<IndexOutOfBoundsException>{p[3]}

    }
}