import org.junit.Test

import org.junit.Assert.*

class ImageTracerTest {

    @Test
    fun fireRays() {
        val image = HdrImage(width = 4, height = 2)
        val camera = PerspectiveCamera(AR = 2.0F)
        val tracer = ImageTracer(image = image, camera = camera)

        val ray1 = tracer.fireRay(0, 0, uPixel = 2.5F, vPixel = 1.5F)
        val ray2 = tracer.fireRay(2, 1, uPixel = 0.5F, vPixel = 0.5F)
        assertTrue(ray1.isClose(ray2))

        val f : (Ray) -> Color = { Color(1.0F, 2.0F, 3.0F) }
        tracer.fireAllRays ( f )
        for (row in 0 until image.getHeight()) {
            for (col in 0 until image.getWidth()) {
                assertTrue(image.getPixel(col, row) == Color(1.0F, 2.0F, 3.0F))
            }
        }
    }
}








