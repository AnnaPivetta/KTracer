import org.junit.Test

import org.junit.Assert.*
import kotlin.math.PI

class TransformationTest {

    @Test
    fun isClose() {
        var m = arrayOf(
            floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
            floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F)
        )
        val invm = arrayOf(
            floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
            floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F)
        )
        val t = Transformation(m, invm)
        assertTrue(t.isConsistent())
        val t2 = Transformation(m, invm)
        assertTrue(t.isClose(t2))
        val t3 = Transformation(
            m = arrayOf(
                floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
                floatArrayOf(9.0F, 9.0F, 9.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F)
            ),
            im = arrayOf(
                floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
                floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F)
            )
        )
        assertFalse(t3.isClose(t))
    }

    @Test
    fun translation() {
        val tr1 = Transformation().translation(Vector(1.0F, 2.0F, 3.0F))
        assertTrue(tr1.isConsistent())
    }


    @Test
    fun inverse() {
        val m = arrayOf(
            floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
            floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F)
        )
        val invm = arrayOf(
            floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
            floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F)
        )
        val t1 = Transformation(m, invm)
        val t2 = t1.inverse()
        assertTrue(t2.isConsistent())

        val prod = t1 * t2
        assertTrue(prod.isConsistent())
        assertTrue(prod.isClose(Transformation()))  // M*M^-1 = ID
    }

    @Test
    fun scaling() {
        val vec1 = Vector(2.0F, 5.0F, 10.0F )
        val tr1 = Transformation().scaling(vec1)
        assertTrue(tr1.isConsistent())
        val vec2 = Vector(3.0F, 2.0F, 4.0F )
        val tr2 = Transformation().scaling(vec2)
        assertTrue(tr2.isConsistent())
        val vec3 = Vector(6.0F, 10.0F, 40.0F )
        val expected = Transformation().scaling(vec3)
        assertTrue(expected.isClose(tr1*tr2))
    }

    @Test
    fun rotations() {
        val t = Transformation()
        assertTrue(t.rotationX(0.1F).isConsistent())
        assertTrue(t.rotationY(0.1F).isConsistent())
        assertTrue(t.rotationZ(0.1F).isConsistent())

        assertTrue((t.rotationX(angle = PI.toFloat() * 0.5F) * VEC_Y).isClose(VEC_Z, epsilon=1e-7F))
        assertTrue((t.rotationY(angle = PI.toFloat() * 0.5F) * VEC_Z).isClose(VEC_X, epsilon=1e-7F))
        assertTrue((t.rotationZ(angle = PI.toFloat() * 0.5F) * VEC_X).isClose(VEC_Y, epsilon=1e-7F))
    }


    @Test
    fun timesTransf() {
        val m1 = Transformation(
            m = arrayOf(
                floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F),
                floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
                floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F),
                floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F)
            ),
            im = arrayOf(
                floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F),
                floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
                floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F),
                floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F)
            )
        )
        assertTrue(m1.isConsistent())

        val m2 = Transformation(
            m = arrayOf(
                floatArrayOf(3.0F, 5.0F, 2.0F, 4.0F),
                floatArrayOf(4.0F, 1.0F, 0.0F, 5.0F),
                floatArrayOf(6.0F, 3.0F, 2.0F, 0.0F),
                floatArrayOf(1.0F, 4.0F, 2.0F, 1.0F)
            ),
            im = arrayOf(
                floatArrayOf(0.4F, -0.2F, 0.2F, -0.6F),
                floatArrayOf(2.9F, -1.7F, 0.2F, -3.1F),
                floatArrayOf(-5.55F, 3.15F, -0.4F, 6.45F),
                floatArrayOf(-0.9F, 0.7F, -0.2F, 1.1F)
            )
        )
        println()
        assertTrue(m2.isConsistent())

        val expected = Transformation(
            m = arrayOf(
                floatArrayOf(33.0F, 32.0F, 16.0F, 18.0F),
                floatArrayOf(89.0F, 84.0F, 40.0F, 58.0F),
                floatArrayOf(118.0F, 106.0F, 48.0F, 88.0F),
                floatArrayOf(63.0F, 51.0F, 22.0F, 50.0F)
            ),
            im = arrayOf(
                floatArrayOf(-1.45F, 1.45F, -1.0F, 0.6F),
                floatArrayOf(-13.95F, 11.95F, -6.5F, 2.6F),
                floatArrayOf(25.525F, -22.025F, 12.25F, -5.2F),
                floatArrayOf(4.825F, -4.325F, 2.5F, -1.1F)
            ),
        )
        assertTrue(expected.isConsistent())

        assertTrue(expected.isClose(m1 * m2))

    }

    @Test
    fun timesVecPointNorm() {
        val m = Transformation(
            m = arrayOf(
                floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F),
                floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
                floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F),
                floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
            ),
            im = arrayOf(
                floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F),
                floatArrayOf(5.75F, -4.75F, 2.0F, 1.0F),
                floatArrayOf(-2.25F, 2.25F, -1.0F, -2.0F),
                floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
            )
        )
        assertTrue(m.isConsistent())

        println()

        val expectedV = Vector(14.0F, 38.0F, 51.0F)
        assertTrue(expectedV.isClose(m * Vector(1.0F, 2.0F, 3.0F)))

        val expectedP = Point(18.0F, 46.0F, 58.0F)
        assertTrue(expectedP.isClose(m * Point(1.0F, 2.0F, 3.0F)))

        val expectedN = Normal(-8.75F, 7.75F, -3.0F)
        assertTrue(expectedN.isClose(m * Normal(3.0F, 2.0F, 4.0F)))

    }
}