import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * BRDF for a diffusive material
 *
 * This class inherits from abstract class BRDF and implements the ideal diffusive material
 * (may be found as *Lambertian*)
 *
 * Class properties:
 * - [p] - The [Pigment] that weights the BRDF contribution given the specific point in the surface. Default is a
 * white [UniformPigment]
 *
 * @see BRDF
 */
class DiffuseBRDF (p : Pigment = UniformPigment(WHITE.copy())) : BRDF(p){
    override fun eval(n: Normal, inDir: Vector, outDir: Vector, uv: Vector2d): Color {
        return p.getColor(uv) * (1.0F / PI.toFloat() )
    }

    @kotlin.ExperimentalUnsignedTypes
    override fun scatterRay(pcg: PCG, inDir: Vector, hitPoint: Point, normal: Normal, depth: Int): Ray {
        val (e1, e2, e3) = createONBfromZ(normal)
        val cosT2 = pcg.rand()
        val cosT = sqrt(cosT2)
        val sinT = sqrt(1.0F - cosT2)
        val phi = 2.0F * PI * pcg.rand()

        return Ray(
            origin=hitPoint,
            dir=e1 * cos(phi).toFloat() * cosT + e2 * sin(phi).toFloat() * cosT + e3 * sinT,
            tmin=1.0e-3F,
            tmax= Float.POSITIVE_INFINITY,
            depth = depth
        )
    }
}