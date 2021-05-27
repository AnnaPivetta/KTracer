import kotlin.math.abs
import kotlin.math.acos

/** Bidirectional Reflectance Distribution Function (BRDF)
 *
 * This class is an abstract class for BRDF of different surfaces
 *
 * Class properties:
 * - [p] - The [Pigment] that weights the BRDF contribution given the specific point in the surface
 *
 * Concrete BRDFs are:
 * - [DiffuseBRDF]
 * - [SpecularBRDF]

 * @see DiffuseBRDF
 * @see SpecularBRDF
*/

abstract class BRDF (val p : Pigment){
    abstract fun eval (n : Normal, inDir : Vector, outDir : Vector, uv : Vector2d) : Color

    @kotlin.ExperimentalUnsignedTypes
    abstract fun scatterRay(pcg : PCG,
                            inDir : Vector,
                            hitPoint : Point,
                            normal : Normal,
                            depth : Int) : Ray
}