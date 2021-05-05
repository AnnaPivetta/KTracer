import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass

class ImageTracerTest {
/*
    @Before
    fun oneTimeSetUp() {
        val image = HdrImage(width = 4, height = 2)
        val camera = PerspectiveCamera(AR = 2.0F)
        val tracer = ImageTracer(image = image, camera = camera)
    }


 */
    @Test
    fun uvSubmapping() {
    val image = HdrImage(width = 4, height = 2)
    val camera = PerspectiveCamera(AR = 2.0F)
    val tracer = ImageTracer(image = image, camera = camera)

    val ray1 = tracer.fireRay(0, 0, uPixel = 2.5F, vPixel = 1.5F)
    val ray2 = tracer.fireRay(2, 1, uPixel = 0.5F, vPixel = 0.5F)
    assertTrue(ray1.isClose(ray2))

}
    @Test
    fun Orientation() {
        val image = HdrImage(width = 4, height = 2)
        val camera = PerspectiveCamera(AR = 2.0F)
        val tracer = ImageTracer(image = image, camera = camera)

        // Fire a ray against top-left corner of the screen
        val topLeft = tracer.fireRay(0, 0, uPixel = 0.0F, vPixel = 0.0F)
        assertTrue(Point(0.0F, 2.0F, 1.0F).isClose(topLeft.at(1.0F)))

        //Fire a ray against bottom-right corner of the screen
        val bottomRight = tracer.fireRay(3, 1, uPixel = 1.0F, vPixel = 1.0F)
        assertTrue(Point(0.0F, -2.0F, -1.0F).isClose(bottomRight.at(1.0F)))
    }

    @Test
    fun ImageCoverage() {
        val image = HdrImage(width = 4, height = 2)
        val camera = PerspectiveCamera(AR = 2.0F)
        val tracer = ImageTracer(image = image, camera = camera)

        val f : (Ray) -> Color = { Color(1.0F, 2.0F, 3.0F) }
        tracer.fireAllRays ( f )
        for (row in 0 until image.getHeight()) {
            for (col in 0 until image.getWidth()) {
                assertTrue(image.getPixel(col, row) == Color(1.0F, 2.0F, 3.0F))
            }
        }
    }
}








