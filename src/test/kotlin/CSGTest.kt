import org.junit.Test

import org.junit.Assert.*
import org.junit.BeforeClass

class CSGUnionTest (){
    @Test
    fun isPointInternal() {
        val s1 = Sphere(Transformation().translation(-0.5F * VECY))
        val s2 = Sphere(Transformation().translation(0.5F * VECY))
        val u = CSGUnion(s1 = s1, s2 = s2)
        assertTrue(u.isPointInternal(Point()))
        assertFalse(u.isPointInternal(Point(2.0F, 0.0F, 0.0F)))
    }

    @Test
    fun rayIntersection() {
        val s1 = Sphere(Transformation().translation(-0.5F * VECY))
        val s2 = Sphere(Transformation().translation(0.5F * VECY))
        val u = CSGUnion(s1 = s1, s2 = s2)

        val ray1 = Ray(origin = Point(0.0F, 2.0F, 0.0F), dir=-VECY)
        val expectedHitPoint0 = Point(0.0F, 1.5F, 0.0F)
        val expectedHitPoint1 = Point(0.0F, -1.5F, 0.0F)
        val expectedNormal = VECY.toNormal()
        val expectedUV0 = Vector2d(0.25F, 0.5F)
        val expectedUV1 = Vector2d(0.75F, 0.5F)
        val expectedT0 = 0.5F
        val expectedT1 = 3.5F
        val int0 = u.rayIntersectionList(ray1)?.get(0)
        val int1 = u.rayIntersectionList(ray1)?.get(1)

        assertTrue(int0?.worldPoint!!.isClose(expectedHitPoint0))
        assertTrue(int0.normal.isClose(expectedNormal))
        assertTrue(int0.t.isClose(expectedT0))
        assertTrue(int0.surfacePoint.isClose(expectedUV0))

        assertTrue(int1?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int1.normal.isClose(expectedNormal))
        assertTrue(int1.t.isClose(expectedT1))
        assertTrue(int1.surfacePoint.isClose(expectedUV1))

    }
}

class CSGDifferenceTest (){
    @Test
    fun isPointInternal() {
        val s1 = Sphere(Transformation().translation(-0.5F * VECY))
        val s2 = Sphere(Transformation().translation(0.5F * VECY))
        val u = CSGDifference(s1 = s1, s2 = s2)
        assertTrue(u.isPointInternal(Point(0.0F, -0.75F, 0.0F)))
        assertFalse(u.isPointInternal(Point()))
    }

    @Test
    fun rayIntersection() {
        val s1 = Sphere(Transformation().translation(-0.5F * VECY))
        val s2 = Sphere(Transformation().translation(0.5F * VECY))
        val u = CSGDifference(s1 = s1, s2 = s2)

        val ray1 = Ray(origin = Point(0.0F, 2.0F, 0.0F), dir=-VECY)
        val expectedHitPoint0 = Point(0.0F, -0.5F, 0.0F)
        val expectedHitPoint1 = Point(0.0F, -1.5F, 0.0F)
        val expectedNormal = VECY.toNormal()
        val expectedUV0 = Vector2d(0.75F, 0.5F)
        val expectedUV1 = Vector2d(0.75F, 0.5F)
        val expectedT0 = 2.5F
        val expectedT1 = 3.5F
        val int0 = u.rayIntersectionList(ray1)?.get(0)
        val int1 = u.rayIntersectionList(ray1)?.get(1)

        assertTrue(int0?.worldPoint!!.isClose(expectedHitPoint0))
        assertTrue(int0.normal.isClose(expectedNormal))
        assertTrue(int0.t.isClose(expectedT0))
        assertTrue(int0.surfacePoint.isClose(expectedUV0))

        assertTrue(int1?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int1.normal.isClose(expectedNormal))
        assertTrue(int1.t.isClose(expectedT1))
        assertTrue(int1.surfacePoint.isClose(expectedUV1))

    }

}

class CSGIntersectionTest (){
    @Test
    fun isPointInternal() {
        val s1 = Sphere(Transformation().translation(-0.5F * VECY))
        val s2 = Sphere(Transformation().translation(0.5F * VECY))
        val u = CSGIntersection(s1 = s1, s2 = s2)
        assertFalse(u.isPointInternal(Point(0.0F, -0.75F, 0.0F)))
        assertTrue(u.isPointInternal(Point()))
    }

    @Test
    fun rayIntersection() {
        val s1 = Sphere(Transformation().translation(-0.5F * VECY))
        val s2 = Sphere(Transformation().translation(0.5F * VECY))
        val u = CSGIntersection(s1 = s1, s2 = s2)

        val ray1 = Ray(origin = Point(0.0F, 2.0F, 0.0F), dir=-VECY)
        val expectedHitPoint0 = Point(0.0F, 0.5F, 0.0F)
        val expectedHitPoint1 = Point(0.0F, -0.5F, 0.0F)
        val expectedNormal = VECY.toNormal()
        val expectedUV0 = Vector2d(0.25F, 0.5F)
        val expectedUV1 = Vector2d(0.75F, 0.5F)
        val expectedT0 = 1.5F
        val expectedT1 = 2.5F
        val int0 = u.rayIntersectionList(ray1)?.get(0)
        val int1 = u.rayIntersectionList(ray1)?.get(1)

        assertTrue(int0?.worldPoint!!.isClose(expectedHitPoint0))
        assertTrue(int0.normal.isClose(expectedNormal))
        assertTrue(int0.t.isClose(expectedT0))
        assertTrue(int0.surfacePoint.isClose(expectedUV0))

        assertTrue(int1?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int1.normal.isClose(expectedNormal))
        assertTrue(int1.t.isClose(expectedT1))
        assertTrue(int1.surfacePoint.isClose(expectedUV1))

    }

}