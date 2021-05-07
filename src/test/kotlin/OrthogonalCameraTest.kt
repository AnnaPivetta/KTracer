import org.junit.Test
import kotlin.test.assertEquals

import kotlin.test.assertTrue

class OrthogonalCameraTest {

    @Test
    fun fireRay() {
        val cam = OrthogonalCamera(AR=2.0F)

        val ray1 = cam.fireRay(0.0F, 0.0F)
        val ray2 = cam.fireRay(1.0F, 0.0F)
        val ray3 = cam.fireRay(0.0F, 1.0F)
        val ray4 = cam.fireRay(1.0F, 1.0F)

        assertEquals(0.0F, ray1.dir.cross(ray2.dir).norm2())
        assertEquals(0.0F, ray1.dir.cross(ray3.dir).norm2())
        assertEquals(0.0F, ray1.dir.cross(ray4.dir).norm2())

        assertTrue( ray1.at(1.0F).isClose(Point(0.0F, 2.0F, -1.0F)))
        assertTrue( ray2.at(1.0F).isClose(Point(0.0F, -2.0F, -1.0F)))
        assertTrue( ray3.at(1.0F).isClose(Point(0.0F, 2.0F, 1.0F)))

    }
}