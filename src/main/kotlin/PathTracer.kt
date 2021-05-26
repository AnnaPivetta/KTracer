/**
 * Renderer implementing the PathTracer
 *
 * This class inherits from abstract class [Renderer] and implements the algorithm of Path Tracer.
 * Hit [Shape]s in the [world] are visualized using their associated [Pigment]. Any contribution of light is
 * neglected.
 *
 * Useful for debug and having a first glance of the composition in the [world], including the [Pigment] on the
 * surfaces
 *
 * @see Renderer
 */
@kotlin.ExperimentalUnsignedTypes
class PathTracer(
    world: World = World(),
    backgroundColor: Color = NAVY.copy(),
    val pcg: PCG = PCG(),
    val nRays: Int = 100,
    val maxDepth: Int = 10,
    val rrTrigger: Int = 6
) :
    Renderer(world = world, backgroundColor = backgroundColor) {

    private fun ptRad(r: Ray): Color {
        if (r.depth > maxDepth) return Color(0.0F, 0.0F, 0.0F)

        val hit = world.rayIntersection(r) ?: return backgroundColor
        val mat = hit.shape.material
        val eRad = mat.emittedRad.getColor(hit.surfacePoint)
        var hitCol = mat.brdf.p.getColor(hit.surfacePoint)

        //Russian Roulette
        val q = maxOf(hitCol.r, hitCol.g, hitCol.b)

        if (r.depth > rrTrigger) {
            if (pcg.rand() > q) {
                //Recursion return, weighted for finite stop
                hitCol *= 1.0F / (1.0F - q)
            }
            //Close recursion and return
            else return eRad
        }

        //MonteCarlo Integration
        var radSum = Color()

        //Recursion only when hit color is not full black
        if (q > 0.0F) {
            for (i in 0 until nRays) {
                val newRay = mat.brdf.scatterRay(
                    pcg = pcg,
                    inDir = hit.ray.dir,
                    hitPoint = hit.worldPoint,
                    normal = hit.normal,
                    depth = r.depth + 1
                )
                //Recursion
                val newRad = ptRad(newRay)
                radSum += hitCol * newRad

            }
        }

        return eRad + radSum * (1.0F / nRays.toFloat())
    }

    override fun computeRadiance(): (Ray) -> Color {
        return { ptRad(it) }
    }
}