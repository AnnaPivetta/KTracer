/**
 * Renderer implementing a basic Flat Renderer
 *
 * This class inherits from abstract class [Renderer] and implements the algorithm of Flat Renderer.
 * Hit [Shape]s in the [world] are visualized using their associated [Pigment]. Any contribution of light is
 * neglected.
 *
 * Useful for debug and having a first glance of the composition in the [world], including the [Pigment] on the
 * surfaces
 *
 * @see Renderer
 */

class FlatRenderer (world: World = World(),
                    backgroundColor: Color = BLACK.copy()) :
    Renderer (world = world, backgroundColor = backgroundColor) {

    override fun computeRadiance(): (r: Ray) -> Color {
        return {
            val hit = world.rayIntersection(it)
            if (hit == null) backgroundColor
            else {
                val mat = hit.shape.material
                mat.emittedRad.getColor(hit.surfacePoint) + mat.brdf.p.getColor(hit.surfacePoint)
            }
        }
    }
}