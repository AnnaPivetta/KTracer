import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class SphereTest {

    @Before
    fun setUp() {
    }

    @Test
    fun rayIntersection() {
        val s = Sphere(Transformation())
        val ray1 = Ray(origin = Point(0.0F, 0.0F, 2.0F), dir=-VECZ)
        val expectedHitPoint1 = Point(0.0F, 0.0F, 1.0F)
        val expectedNormal1 = VECZ.toNormal()
        val expectedUV1 = Vector2d(0.0F, 0.0F)
        val expectedT1 = 1.0F
        val int = s.rayIntersection(ray1)

        assertTrue(int?.worldPoint!!.isClose(expectedHitPoint1))
        assertTrue(int.normal.isClose(expectedNormal1))
        assertTrue(int.t.isClose(expectedT1))
        assertTrue(int.surfacePoint.isClose(expectedUV1))
    }
}