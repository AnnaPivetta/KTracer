/**
 * Renderer implementing a basic OnOff Renderer
 *
 * This class inherits from abstract class [Renderer] and implements the algorithm of OnOff Renderer.
 * Hit [Shape]s in the [world] are visualized in [color] and other pixels are visualized in [backgroundColor]
 *
 *
 * Useful for debug and having a first glance of the composition in the [world]
 *
 * @see Renderer
 */
class OnOffRenderer(world: World = World(),
                    backgroundColor: Color = BLACK.copy(),
                    val color: Color = WHITE.copy() ) :
    Renderer (world = world, backgroundColor = backgroundColor) {
    override fun computeRadiance(): (r: Ray) -> Color {
        return { if (world.rayIntersection(it) == null) backgroundColor.copy() else color.copy() }
    }
    }