import org.junit.Test

import org.junit.Assert.*

class TransformationTest {

    @Test
    fun translation() {
        val tr1 = Transformation().translation(Vector(1.0F, 2.0F, 3.0F))
        assertTrue( tr1.isConsistent())
    }

    @Test
    fun scaling() {
    }

    @Test
    fun rotationX() {
    }

    @Test
    fun rotationY() {
    }

    @Test
    fun rotationZ() {
    }

    @Test
    fun isClose() {
        val m = arrayOf(
            floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
            floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F))
        val invm = arrayOf(
            floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
            floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F))
        val t = Transformation(m, invm)
        assertTrue(t.isConsistent())
    }

    @Test
    fun inverse() {
        val m = arrayOf(
            floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
            floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F))
        val invm = arrayOf(
            floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
            floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F))
        val t1 = Transformation(m, invm)
        val t2 = t1.inverse()
        assertTrue(t2.isConsistent())

        val prod = t1*t2
        assertTrue(prod.isConsistent())
        assertTrue(prod.isClose(Transformation()))  // M*M^-1 = ID
    }
}