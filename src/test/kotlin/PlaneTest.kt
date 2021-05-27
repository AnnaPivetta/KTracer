import org.junit.Test

import org.junit.Assert.*
import kotlin.math.PI

class PlaneTest {

    @Test
    fun rayIntersection() {
        val plane = Plane()
        val ray1 = Ray(origin=Point(0.0F, 0.0F, 1.0F), dir=-VECZ)
        val intersection1 = plane.rayIntersection(ray1)
        assertTrue(intersection1 != null)
        val hit = HitRecord (worldPoint = Point(0.0F, 0.0F, 0.0F),
            normal = Normal(0.0F, 0.0F, 1.0F),
            surfacePoint = Vector2d(0.0F, 0.0F),
            t=1.0F,
            ray=ray1)
        assertTrue(hit.isClose(intersection1))
        val ray2= Ray(origin = Point(0.0F, 0.0F, 1.0F), dir = VECZ)
        val intersection2 = plane.rayIntersection(ray2)
        assertTrue(intersection2 == null)

        val ray3= Ray(origin = Point(0.0F, 2.0F, 1.0F), dir = VECX)
        val intersection3 = plane.rayIntersection(ray3)
        assertTrue(intersection2 == null)

        val ray4= Ray(origin = Point(0.0F, 4.0F, 0.5F), dir = VECY)
        val intersection4 = plane.rayIntersection(ray4)
        assertTrue(intersection2 == null)

    }

    @Test
    fun transformation () {
        val plane = Plane(Transformation().rotationY((PI/2.0F).toFloat()))
        val ray1= Ray(origin = Point(1.0F, 0.0F, 0.0F), dir =-VECX)
        val intersection1 = plane.rayIntersection(ray1)
        assertTrue(intersection1 != null)
        println(intersection1)
        val hit = HitRecord(worldPoint = Point(0.0F, 0.0F, 0.0F),
        normal = Normal(1.0F, 0.0F, 0.0F),
        surfacePoint = Vector2d(0.0F, 0.0F),
        t=1.0F,
        ray=ray1)
        assertTrue(hit.isClose(intersection1))

        val ray2 = Ray(origin = Point(0.0F, 0.0F, 1.0F), dir = VECX)
        val intersection2 = plane.rayIntersection(ray2)
        assertTrue(intersection2 == null)

        val ray3 = Ray(origin = Point(1.0F, 0.5F, -2.0F), dir=VECX)
        val intersection3 = plane.rayIntersection(ray3)
        assertTrue(intersection3 == null)
    }

    @Test
    fun UVCoordinates () {
        val plane = Plane()
        val ray1 = Ray(origin=Point(0.0F, 0.0F, 1.0F), dir = -VECZ)
        val intersection1 = plane.rayIntersection(ray1)
        assertTrue(intersection1!!.surfacePoint.isClose(Vector2d(0.0F, 0.0F)))

        val ray2 = Ray(origin=Point(0.25F, 0.75F, 1.0F), dir = -VECZ)
        val intersection2 = plane.rayIntersection(ray2)
        assertTrue(intersection2!!.surfacePoint.isClose(Vector2d(0.25F, 0.75F)))

        val ray3 = Ray(origin = Point(4.25F, 7.75F, 1.0F), dir = -VECZ)
        val intersection3 = plane.rayIntersection(ray3)
        assertTrue(intersection3!!.surfacePoint.isClose(Vector2d(0.25F, 0.75F)))
    }
}