/**
 * Material
 *
 * Class implementing the actual material for objects
 *
 * Class properties:
 * - [brdf] - The [BRDF] of the material
 * - [emittedRad] - The emitted radiance of the material
 */
data class Material (val brdf: BRDF = DiffuseBRDF(), val emittedRad : Pigment = UniformPigment(BLACK)) {
}