import kotlin.math.PI
/**
 * BRDF for a diffusive material
 *
 * This class inherits from abstract class BRDF and implements the ideal diffusive material
 * (may be found as *Lambertian*)
 *
 * Class properties:
 * - [p] - The [Pigment] that weights the BRDF contribution given the specific point in the surface. Default is a
 * white [UniformPigment]
 * - [ref] - The reflectance of the material. Default is 1.0
 * @see BRDF
 */
class DiffuseBRDF (p : Pigment = UniformPigment(WHITE.copy()), val ref : Float = 1.0F) : BRDF(p){
    override fun eval(n: Normal, inDir: Vector, outDir: Vector, uv: Vector2d): Color {
        return p.getColor(uv) * ( ref / PI.toFloat() )
    }
}