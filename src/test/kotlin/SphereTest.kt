import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class SphereTest {

    @Test
    fun rayIntersection() {
        val s = Sphere(Transformation())
        val ray1 = Ray(origin = Point(0.0F, 0.0F, 2.0F), dir=-VECZ)
        val expectedHitPoint1 = Point(0.0F, 0.0F, 1.0F)
        val expectedNormal1 = VECZ.toNormal()
        val expectedUV1 = Vector2d(0.0F, 0.0F)
        val expectedT1 = 1.0F
        val int1 = s.rayIntersection(ray1)

        assertTrue(int1?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int1.normal.isClose(expectedNormal1))
        assertTrue(int1.t.isClose(expectedT1))
        assertTrue(int1.surfacePoint.isClose(expectedUV1))

        val ray2 = Ray(origin = Point(3.0F, 0.0F, 0.0F), dir=-VECX)
        val expectedHitPoint2 = Point(1.0F, 0.0F, 0.0F)
        val expectedNormal2 = VECX.toNormal()
        val expectedUV2 = Vector2d(0.0F, 0.5F)
        val expectedT2 = 2.0F
        val int2 = s.rayIntersection(ray2)

        assertTrue(int2?.worldPoint!!.isClose(expectedHitPoint2))
        assertTrue(int2.normal.isClose(expectedNormal2))
        assertTrue(int2.t.isClose(expectedT2))
        assertTrue(int2.surfacePoint.isClose(expectedUV2))

        val ray3 = Ray(origin = Point(0.0F, 0.0F, 0.0F), dir=VECX)
        val expectedHitPoint3 = Point(1.0F, 0.0F, 0.0F)
        val expectedNormal3 = -VECX.toNormal()
        val expectedUV3 = Vector2d(0.0F, 0.5F)
        val expectedT3 = 1.0F
        val int3 = s.rayIntersection(ray3)

        assertTrue(int3?.worldPoint!!.isClose(expectedHitPoint3))
        assertTrue(int3.normal.isClose(expectedNormal3))
        assertTrue(int3.t.isClose(expectedT3))
        assertTrue(int3.surfacePoint.isClose(expectedUV3))
    }

    @Test
    fun rayIntersectionWithT() {
        val translation = Transformation().translation(Vector(10.0F, 0.0F, 0.0F))
        val s = Sphere(translation)

        val ray1 = Ray(origin = Point(10.0F, 0.0F, 2.0F), dir=-VECZ)
        val expectedHitPoint1 = Point(10.0F, 0.0F, 1.0F)
        val expectedNormal1 = VECZ.toNormal()
        val expectedUV1 = Vector2d(0.0F, 0.0F)
        val expectedT1 = 1.0F
        val int1 = s.rayIntersection(ray1)
        val rayNot1 = Ray(origin = Point(0.0F, 0.0F, 2.0F), dir=-VECZ)

        assertTrue(int1?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int1.normal.isClose(expectedNormal1))
        assertTrue(int1.t.isClose(expectedT1))
        assertTrue(int1.surfacePoint.isClose(expectedUV1))
        assertTrue(s.rayIntersection(rayNot1)==null)


        val ray2 = Ray(origin = Point(13.0F, 0.0F, 0.0F), dir=-VECX)
        val expectedHitPoint2 = Point(11.0F, 0.0F, 0.0F)
        val expectedNormal2 = VECX.toNormal()
        val expectedUV2 = Vector2d(0.0F, 0.5F)
        val expectedT2 = 2.0F
        val int2 = s.rayIntersection(ray2)
        val rayNot2 = Ray(origin = Point(-10.0F, 0.0F, 2.0F), dir=-VECX)

        assertTrue(int2?.worldPoint!!.isClose(expectedHitPoint2))
        assertTrue(int2.normal.isClose(expectedNormal2))
        assertTrue(int2.t.isClose(expectedT2))
        assertTrue(int2.surfacePoint.isClose(expectedUV2))
        assertTrue(s.rayIntersection(rayNot2)==null)

    }
}