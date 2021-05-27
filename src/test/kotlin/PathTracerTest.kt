import org.junit.Assert.*
import org.junit.Test

class PathTracerTest {
    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun FurnaceTest() {
        val pcg = PCG()
        for (i in 0..50) {
            val world = World()
            val emittedRadiance = pcg.rand()
            val reflectance = pcg.rand()
            val material = Material(
                brdf = DiffuseBRDF(p = UniformPigment(Color(1.0F, 1.0F, 1.0F) * reflectance)),
                emittedRad = UniformPigment(Color(1.0F, 1.0F, 1.0F) * emittedRadiance)
            )
            world.add(Sphere(material = material))
            val tracer = PathTracer(pcg = pcg, nRays = 1, world = world, maxDepth = 1000, rrTrigger = 1001)
            //val tracer = OnOffRenderer()
            val ray = Ray(origin = Point(0.0F, 0.0F, 0.0F), dir = Vector(1.0F, 1.0F, 1.0F))
            println("here")
            val color = tracer.computeRadiance().invoke(ray)
            println("here")
            val exp = emittedRadiance/(1.0F - reflectance)
            println(exp)
            println(color)
            assertTrue(exp.isClose(color.r, epsilon = 1e-3F))
            assertTrue(exp.isClose(color.g, epsilon = 1e-3F))
            assertTrue(exp.isClose(color.b, epsilon = 1e-3F))
        }
    }
}