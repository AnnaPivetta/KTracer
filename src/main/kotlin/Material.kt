/**
 * Material
 *
 * Class implementing the actual material for objects
 *
 * Class properties:
 * - [brdf] - The [BRDF] of the material. Default is [DiffuseBRDF]
 * - [emittedRad] - The emitted radiance of the material. Default is a black [UniformPigment]
 */
data class Material (val brdf: BRDF = DiffuseBRDF(), val emittedRad : Pigment = UniformPigment(BLACK)) {
}