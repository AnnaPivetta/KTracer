import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import kotlin.math.PI

class TransformationTest {

    @Test
    fun isClose() {
        val t = Transformation(
            m =arrayOf(
                floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
                floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F)
            ),
            im = arrayOf(
                floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
                floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F)
            ) )
        assertTrue(t.isConsistent())
        val t2 = Transformation(
            m =arrayOf(
                floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
                floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F)
            ),
            im = arrayOf(
                floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
                floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F)
            ) )
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
        val t1 = Transformation(
            m = arrayOf(
            floatArrayOf(1.0F, 2.0F, 3.0F, 4.0F), floatArrayOf(5.0F, 6.0F, 7.0F, 8.0F),
            floatArrayOf(9.0F, 9.0F, 8.0F, 7.0F), floatArrayOf(6.0F, 5.0F, 4.0F, 1.0F)),
            im = arrayOf(
            floatArrayOf(-3.75F, 2.75F, -1.0F, 0.0F), floatArrayOf(4.375F, -3.875F, 2.0F, -0.5F),
            floatArrayOf(0.5F, 0.5F, -1.0F, 1.0F), floatArrayOf(-1.375F, 0.875F, 0.0F, -0.5F)
        ))
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

        assertTrue((t.rotationX(angle = 90.0F) * VECY).isClose(VECZ, epsilon=1e-7F))
        assertTrue((t.rotationY(angle = 90.0F) * VECZ).isClose(VECX, epsilon=1e-7F))
        assertTrue((t.rotationZ(angle = 90.0F) * VECX).isClose(VECY, epsilon=1e-7F))
    }


    @Test
    fun timesTransformation() {
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
    fun timesVec() {
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

        val expectedV = Vector(14.0F, 38.0F, 51.0F)
        assertTrue(expectedV.isClose(m * Vector(1.0F, 2.0F, 3.0F)))


    }

    @Test
    fun timesPoint() {
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


        val expectedP = Point(18.0F, 46.0F, 58.0F)
        assertTrue(expectedP.isClose(m * Point(1.0F, 2.0F, 3.0F)))

    }

        @Test
        fun timesNorm() {
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

            val expectedN = Normal(-8.75F, 7.75F, -3.0F)
            assertTrue(expectedN.isClose(m * Normal(3.0F, 2.0F, 4.0F)))
        }

    @Test
    fun timesShapes() {
        val T = Transformation()
        //Sphere
        val translation = T.translation(Vector(10.0F, 0.0F, 0.0F))
        val s = Sphere(translation)
        val s2 = translation * Sphere()

        val ray1 = Ray(origin = Point(10.0F, 0.0F, 2.0F), dir=-VECZ)
        val int1 = s.rayIntersection(ray1)
        val int2 = s2.rayIntersection(ray1)

        assertTrue(int1?.worldPoint!!.isClose(int2?.worldPoint!!))
        assertTrue(int1.normal.isClose(int2.normal))
        assertTrue(int1.t.isClose(int2.t))
        assertTrue(int1.surfacePoint.isClose(int2.surfacePoint))

        //Box
        val trans = translation * T.rotationX(0.25F * PI.toFloat())
        val b = Box(T = trans)
        val b2 = trans* Box()

        val intb1 = b.rayIntersection(ray1)
        val intb2 = b2.rayIntersection(ray1)

        assertTrue(intb1?.worldPoint!!.isClose(intb2?.worldPoint!!))
        assertTrue(intb1.normal.isClose(intb2.normal))
        assertTrue(intb1.t.isClose(intb2.t))
        assertTrue(intb1.surfacePoint.isClose(intb2.surfacePoint))

        //Cylinder
        val c = Cylinder(T = trans)
        val c2 = trans* Cylinder()

        val intc1 = c.rayIntersection(ray1)
        val intc2 = c2.rayIntersection(ray1)

        assertTrue(intc1?.worldPoint!!.isClose(intc2?.worldPoint!!))
        assertTrue(intc1.normal.isClose(intc2.normal))
        assertTrue(intc1.t.isClose(intc2.t))
        assertTrue(intc1.surfacePoint.isClose(intc2.surfacePoint))

        //Plane
        val p = Plane(T = trans)
        val p2 = trans* Plane()

        val intp1 = p.rayIntersection(ray1)
        val intp2 = p2.rayIntersection(ray1)

        assertTrue(intp1?.worldPoint!!.isClose(intp2?.worldPoint!!))
        assertTrue(intp1.normal.isClose(intp2.normal))
        assertTrue(intp1.t.isClose(intp2.t))
        assertTrue(intp1.surfacePoint.isClose(intp2.surfacePoint))

        //Difference
        val cyl = Cylinder(T.rotationY(0.5F*PI.toFloat()) * T.scaling(3.0F*VECZ+0.5F*(VECX+VECY)))
        val d = CSGDifference(cyl, Sphere(), trans)
        val d2 = trans* CSGDifference(cyl, Sphere())

        val intd1 = d.rayIntersection(ray1)
        val intd2 = d2.rayIntersection(ray1)

        assertTrue(intd1?.worldPoint!!.isClose(intd2?.worldPoint!!))
        assertTrue(intd1.normal.isClose(intd2.normal))
        assertTrue(intd1.t.isClose(intd2.t))
        assertTrue(intd1.surfacePoint.isClose(intd2.surfacePoint))

        //Union
        val u = CSGDifference(cyl, Sphere(), trans)
        val u2 = trans* CSGDifference(cyl, Sphere())

        val intu1 = u.rayIntersection(ray1)
        val intu2 = u2.rayIntersection(ray1)

        assertTrue(intu1?.worldPoint!!.isClose(intu2?.worldPoint!!))
        assertTrue(intu1.normal.isClose(intu2.normal))
        assertTrue(intu1.t.isClose(intu2.t))
        assertTrue(intu1.surfacePoint.isClose(intu2.surfacePoint))

        //Intersection
        val i = CSGDifference(cyl, Sphere(), trans)
        val i2 = trans* CSGDifference(cyl, Sphere())

        val inti1 = i.rayIntersection(ray1)
        val inti2 = i2.rayIntersection(ray1)

        assertTrue(inti1?.worldPoint!!.isClose(inti2?.worldPoint!!))
        assertTrue(inti1.normal.isClose(inti2.normal))
        assertTrue(inti1.t.isClose(inti2.t))
        assertTrue(inti1.surfacePoint.isClose(inti2.surfacePoint))
    }
}