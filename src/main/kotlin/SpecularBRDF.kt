import kotlin.math.abs
import kotlin.math.acos

/**
 * BRDF for a specular material
 *
 * This class inherits from abstract class BRDF and implements the ideal mirror
 *
 * Class properties:
 * - [p] - The [Pigment] that weights the BRDF contribution given the specific point in the surface. Default is a
 * white [UniformPigment]
 *
 * @see BRDF
 */

class SpecularBRDF(p: Pigment = UniformPigment(WHITE.copy())) : BRDF(p) {
    override fun eval(n: Normal, inDir: Vector, outDir: Vector, uv: Vector2d): Color {
        //Providing this implementation for reference,
        // but we are not going to use it
        val tIn = acos(n.toVector() * (inDir))
        val tOut = acos(n.toVector() * (outDir))

        return if (abs(tIn - tOut) < 0.01) p.getColor(uv) else Color()
    }

    @kotlin.ExperimentalUnsignedTypes
    override fun scatterRay(pcg: PCG, inDir: Vector, hitPoint: Point, normal: Normal, depth: Int): Ray {
        val rayDir = Vector(inDir.x, inDir.y, inDir.z)
        rayDir.normalize()
        val n = normal.toVector()
        n.normalize()

        return Ray(
            origin=hitPoint,
            dir=rayDir - (n * 2.0F * (n * rayDir)),
            tmin=1.0e-3F,
            tmax= Float.POSITIVE_INFINITY
        )
    }
}