import org.junit.Test

import org.junit.Assert.*

class CylinderTest {

    @Test
    fun isPointInternal() {
    }

    @Test
    fun rayIntersection() {
        val c = Cylinder(Transformation())
        val ray1 = Ray(origin = Point(2.0F, 0.0F, 0.0F), dir=-VECX)
        val expectedHitPoint1 = Point(1.0F, 0.0F, 0.0F)
        val expectedNormal1 = VECX.toNormal()
        val expectedUV1 = Vector2d(0.5F, 0.5F)
        val expectedT1 = 1.0F
        val int1 = c.rayIntersection(ray1)

        assertTrue(int1?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int1.normal.isClose(expectedNormal1))
        assertTrue(int1.t.isClose(expectedT1))
        assertTrue(int1.surfacePoint.isClose(expectedUV1))

        val ray2 = Ray(origin = Point(-3.0F, 0.0F, 0.0F), dir=VECX)
        val expectedHitPoint2 = Point(-1.0F, 0.0F, 0.0F)
        val expectedNormal2 = -VECX.toNormal()
        val expectedUV2 = Vector2d(0.5F, 0.5F)
        val expectedT2 = 2.0F
        val int2 = c.rayIntersection(ray2)

        assertTrue(int2?.worldPoint!!.isClose(expectedHitPoint2))
        assertTrue(int2.normal.isClose(expectedNormal2))
        assertTrue(int2.t.isClose(expectedT2))
        assertTrue(int2.surfacePoint.isClose(expectedUV2))


        val ray3 = Ray(origin = Point(0.0F, 0.0F, 0.0F), dir=VECY)
        val expectedHitPoint3 = Point(0.0F, 1.0F, 0.0F)
        val expectedNormal3 = -VECY.toNormal()
        val expectedUV3 = Vector2d(0.5F, 0.5F)
        val expectedT3 = 1.0F
        val int3 = c.rayIntersection(ray3)

        assertTrue(int3?.worldPoint!!.isClose(expectedHitPoint3))
        assertTrue(int3.normal.isClose(expectedNormal3))
        assertTrue(int3.t.isClose(expectedT3))
        assertTrue(int3.surfacePoint.isClose(expectedUV3))

        val ray4 = Ray(origin = Point(-2.0F, 0.0F, 2.5F), dir=(VECX-VECZ))
        val expectedHitPoint4 = Point(0.0F, 0.0F, 0.5F)
        val expectedNormal4 = VECZ.toNormal()
        val expectedUV4 = Vector2d(0.5F, 0.5F)
        val expectedT4 = 2.0F
        val int4 = c.rayIntersection(ray4)

        assertTrue(int4?.worldPoint!!.isClose(expectedHitPoint4))
        assertTrue(int4.normal.isClose(expectedNormal4))
        assertTrue(int4.t.isClose(expectedT4))
        assertTrue(int4.surfacePoint.isClose(expectedUV4))


        val ray5 = Ray(origin = Point(-0.5F, 0.0F, 2.5F), dir=(0.1F*VECX-VECZ))
        val expectedHitPoint5 = Point(-0.3F, 0.0F, 0.5F)
        val expectedNormal5 = VECZ.toNormal()
        val expectedUV5 = Vector2d(0.5F, 0.5F)
        val expectedT5 = 2.0F
        val int5 = c.rayIntersection(ray5)

        assertTrue(int5?.worldPoint!!.isClose(expectedHitPoint5))
        assertTrue(int5.normal.isClose(expectedNormal5))
        assertTrue(int5.t.isClose(expectedT5))
        assertTrue(int5.surfacePoint.isClose(expectedUV5))
    }


    @Test
    fun rayIntersectionList() {
    }
}