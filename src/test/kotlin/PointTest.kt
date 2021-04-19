import org.junit.Test

import org.junit.Assert.*

class PointTest {

    @Test
    fun testToString() {
        var p = Point(1F, 2F, 3F)
        println(p.toString())
        assertTrue(p.toString()=="(1.0;2.0;3.0)")
    }

    @Test
    fun isClose() {
        var a = Point(1.0F, 2.0F, 3.0F)
        var b = Point(4.0F, 6.0F, 8.0F)
        assertTrue( a.isClose(a) )
        assertFalse( a.isClose(b) )
    }

    @Test
    fun plus() {
        var a = Point(1.0F, 2.0F, 3.0F)
        var b = Vector(4.0F, 6.0F, 8.0F)
        var c = Point(5.0F, 8.0F, 11.0F)
        assertTrue((a+b).isClose(c))
    }

    @Test
    fun minus() {
        var a = Point(1.0F, 2.0F, 3.0F)
        var b = Point(4.0F, 6.0F, 8.0F)
        var c = Vector(-3.0F, -4.0F, -5.0F)
       // assertTrue((a-b).isClose(c))
    }

    @Test
    fun testMinus() {
        var a = Point(1.0F, 2.0F, 3.0F)
        var b = Vector(4.0F, 6.0F, 8.0F)
        var c = Point(-3.0F, -4.0F, -5.0F)
        assertTrue((a-b).isClose(c))

    }
}