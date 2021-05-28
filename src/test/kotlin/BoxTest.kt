import org.junit.Test

import org.junit.Assert.*

class BoxTest {

    @Test
    fun rayIntersection() {
        val b = Box(T = Transformation())
        val ray1 = Ray(origin = Point(0.0F, 0.0F, 2.0F), dir=-VECZ)
        val expectedHitPoint1 = Point(0.0F, 0.0F, 0.5F)
        val expectedNormal1 = VECZ.toNormal()
        val expectedUV1 = Vector2d(0.25F+0.5F/4.0F, 0.5F)
        val expectedT1 = 1.5F
        val int1 = b.rayIntersection(ray1)

        println(int1?.surfacePoint)
        assertTrue(int1?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int1.normal.isClose(expectedNormal1))
        assertTrue(int1.t.isClose(expectedT1))
        assertTrue(int1.surfacePoint.isClose(expectedUV1))


        val ray2 = Ray(origin = Point(3.0F, 0.0F, 0.0F), dir=-VECX)
        val expectedHitPoint2 = Point(0.5F, 0.0F, 0.0F)
        val expectedNormal2 = VECX.toNormal()
        val expectedUV2 = Vector2d(0.5F+0.5F/4.0F, 0.5F)
        val expectedT2 = 2.5F
        val int2 = b.rayIntersection(ray2)

        assertTrue(int2?.worldPoint!!.isClose(expectedHitPoint2))
        assertTrue(int2.normal.isClose(expectedNormal2))
        assertTrue(int2.t.isClose(expectedT2))
        assertTrue(int2.surfacePoint.isClose(expectedUV2))

        val ray3 = Ray(origin = Point(0.0F, 0.0F, 0.0F), dir=VECX)
        val expectedHitPoint3 = Point(0.5F, 0.0F, 0.0F)
        val expectedNormal3 = -VECX.toNormal()
        val expectedUV3 = Vector2d(0.5F/4.0F, 0.5F)
        val expectedT3 = 0.5F
        val int3 = b.rayIntersection(ray3)

        println(int3?.worldPoint)
        assertTrue(int3?.worldPoint!!.isClose(expectedHitPoint3))
        assertTrue(int3.normal.isClose(expectedNormal3))
        assertTrue(int3.t.isClose(expectedT3))
        assertTrue(int3.surfacePoint.isClose(expectedUV3))

    }
}