import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PerspectiveCameraTest {

    @Test
    fun fireRay() {
        val cam = PerspectiveCamera(dist= 1.0F, AR=2.0F)

        val ray1 = cam.fireRay(0.0F, 0.0F)
        val ray2 = cam.fireRay(1.0F, 0.0F)
        val ray3 = cam.fireRay(0.0F, 1.0F)
        val ray4 = cam.fireRay(1.0F, 1.0F)

        assertTrue(ray1.origin.isClose(ray2.origin))
        assertTrue(ray1.origin.isClose(ray3.origin))
        assertTrue(ray1.origin.isClose(ray4.origin))

        assertTrue(ray1.at(1.0F).isClose(Point(0.0F, 2.0F, -1.0F)))
        assertTrue(ray2.at(1.0F).isClose(Point(0.0F, -2.0F, -1.0F)))
        assertTrue(ray3.at(1.0F).isClose(Point(0.0F, 2.0F, 1.0F)))
    }
}