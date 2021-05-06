import org.junit.Test

import org.junit.Assert.*

class WorldTest {

    @Test
    fun rayIntersection() {
        val s1 = Sphere(Transformation().translation(VECX*10F))
        val s2 = Sphere(Transformation().translation(VECZ*10F))
        val s3 = Sphere(Transformation().translation(VECY*(-7F)))
        val s4 = Sphere(Transformation().translation(VECZ*(-4F)))
        val w = World()
        w.add(s1)
        w.add(s2)
        w.add(s3)
        w.add(s4)
        val ray1 = Ray(origin = Point(0.0F, 0.0F, 5.0F), dir=-VECZ)
        val intersection = w.rayIntersection(ray1)
        assertTrue(intersection?.worldPoint!!.isClose(Point(0.0F, 00F, -3.0F)))
        assertFalse(intersection?.worldPoint!!.isClose(Point(2.0F, 00F, 0.0F)))
        val ray2 = Ray(origin = Point(10.0F, 0.0F, -6.0F), dir=VECZ)
        val intersection2 = w.rayIntersection(ray2)
        assertTrue(intersection2?.worldPoint!!.isClose(Point(10.0F, 00F, -1.0F)))
        val ray3 = Ray(origin = Point(20.0F, 20.0F, 20.0F), dir = VECZ)
        val intersection3 = w.rayIntersection(ray3)
        assertTrue(intersection3?.worldPoint == null)
    }
}