/**
 * Renderer
 *
 * This class is an abstract class for implementing the different algorithms of rendering
 *
 *  * Class properties:
 * - [world] - The [World] to be rendered
 * - [backgroundColor] - The background [Color] of the scene
 *
 * Concrete Renderers are:
 * - [OnOffRenderer]
 * - [FlatRenderer]
 * - [PathTracer]
 *
 */
abstract class Renderer (val world: World, val backgroundColor: Color){
    abstract fun computeRadiance(): (r : Ray) -> Color
}