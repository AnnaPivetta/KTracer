import org.junit.Test

import org.junit.Assert.*

class VectorTest {

    @Test
    fun isClose() {
        val v1 = Vector(1.0F, 2.0F, 3.0F)
        val v2 = Vector(1.0F, 2.0F, 3.0F)
        assertTrue(v1.isClose(v2))
    }

    @Test
    fun plus() {
        val v1 = Vector(1.0F, 2.0F, 3.0F)
        val v2 = Vector(5.0F, 6.0F, 7.0F)
        val sum = v1 + v2
        assertTrue(sum.isClose(Vector(6.0F, 8.0F, 10.0F)))
    }

    @Test
    fun minus() {
        val v1 = Vector(1.0F, 2.0F, 3.0F)
        val v2 = Vector(5.0F, 7.0F, 9.0F)
        val diff = v2 - v1
        assertTrue(diff.isClose(Vector(4.0F, 5.0F, 6.0F)))
    }

    @Test
    operator fun unaryMinus() {
        val v = Vector(1.0F, 2.0F, 3.0F)
        assertTrue( (-v).isClose(Vector(-1.0F, -2.0F, -3.0F)))
    }

    @Test
    fun times() {
        val v = Vector(1.0F, 2.0F, 3.0F )
        val a = 2.0F
        val prod = v*a
        assertTrue(prod.isClose(Vector(2.0F, 4.0F, 6.0F)))
    }

    @Test
    fun scalarTimes() {
        val v1 = Vector(2.0F, 3.0F, 4.0F)
        val v2 = Vector(5.0F, 7.0F, 9.0F)
        val prod = v1 * v2
        assertTrue(prod == 67.0F)
    }

    @Test
    fun cross() {
        val v1 = Vector(2.0F, 3.0F, 4.0F)
        val v2 = Vector(5.0F, 7.0F, 9.0F)
        val prod = v1.cross(v2)
        assertTrue(prod.isClose(Vector(-1.0F, 2.0F, -1.0F)))
    }

    @Test
    fun norm2() {
        val v1 = Vector(1.0F, 4.0F, 8.0F)
        assertTrue(v1.norm2() == 81.0F)
    }

    @Test
    fun norm() {
        val v1 = Vector(1.0F, 4.0F, 8.0F)
        assertTrue(v1.norm() == 9.0F)
    }

    @Test
    fun normalize() {
        val v1 = Vector(1.0F, 4.0F, 8.0F)
        v1.normalize()
        assertTrue(v1.isClose(Vector(1.0F/9.0F, 4.0F/9.0F, 8.0F/9.0F)))
    }
}