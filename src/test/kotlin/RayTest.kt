import org.junit.Test

import org.junit.Assert.*

class RayTest {

    @Test
    fun isClose() {
        val ray1 = Ray(origin = Point(1.0F, 2.0F, 3.0F), dir = Vector(5.0F, 4.0F, -1.0F))
        val ray2 = Ray(origin = Point(1.0F, 2.0F, 3.0F), dir = Vector(5.0F, 4.0F, -1.0F))
        val ray3 = Ray(origin = Point(5.0F, 1.0F, 4.0F), dir = Vector(3.0F, 9.0F, 4.0F))
        assertTrue(ray1.isClose(ray2))
        assertFalse(ray1.isClose(ray3))
    }

    @Test
    fun at() {
        val ray = Ray(origin = Point(1.0F, 2.0F, 4.0F), dir = Vector(4.0F, 2.0F, 1.0F))
        assertTrue(ray.at(0.0F).isClose(ray.origin))
        assertTrue(ray.at(1.0F).isClose(Point(5.0F, 4.0F, 5.0F)))
        assertTrue(ray.at(2.0F).isClose(Point(9.0F, 6.0F, 6.0F)))

    }
}