import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * Shape implementing a Cylinder
 *
 * This class inherits from abstract class Shape and implements a canonical cylinder aligned with z axis.
 * Canonical Cylinder has height = 1 (from z=-0.5 to z=0.5) and radius = 1
 *
 * Class properties:
 * - [T] - (optional) The [Transformation] to apply to the Cylinder
 * - [material] - The [Material] of which the [Cylinder] is made of
 *
 * @see Shape
 */

class Cylinder(
    T: Transformation = Transformation(),
    material: Material = Material()
) : Shape(T, material) {

    override fun isPointInternal(p: Point): Boolean {
        //Canonical p
        val canP = T.inverse() * p
        return canP.x * canP.x + canP.y * canP.y < 1.0F && p.z in -0.5F..0.5F
    }

    override fun rayIntersection(r: Ray): HitRecord? {
        val ir = T.inverse() * r


        val oVx = ir.origin.x
        val oVy = ir.origin.y
        val a = ir.dir.x * ir.dir.x + ir.dir.y * ir.dir.y
        val b = oVx * ir.dir.x + oVy * ir.dir.y
        val c = oVx * oVx + oVy * oVy - 1

        //Intersections with lateral surface of cilynder
        val t1 = (-b - sqrt(b * b - a * c)) / a
        val t2 = (-b + sqrt(b * b - a * c)) / a

        val tHit = when {
            t1 in r.tmin..r.tmax -> {
                t1
            }
            t2 in r.tmin..r.tmax -> {
                t2
            }
            else -> {
                return null
            }
        }

        if (tHit == Float.POSITIVE_INFINITY) {
            //If ir is along z axis
            return HitRecord(
                worldPoint = T * Point(ir.origin.x, ir.origin.y, -sign(ir.dir.z) * 0.5F),
                normal = T * (-VECZ.toNormal() * sign(ir.dir.z)),
                surfacePoint = toSurPoint(Point(ir.origin.x, ir.origin.y, -sign(ir.dir.z) * 0.5F)),
                t = abs(ir.origin.z + sign(ir.dir.z) * 0.5F),
                ray = r,
                shape = this
            )
        } else {

            //Check if vertically there's intersection
            val hit1 = ir.at(tHit)
            when {
                (hit1.z in -0.5F..0.5F) -> return HitRecord(
                    worldPoint = T * hit1,
                    normal = T * getNormal(hit1, ir.dir),
                    surfacePoint = toSurPoint(hit1),
                    t = tHit,
                    ray = r,
                    shape = this
                )
                //If the intersection would occur above the edge
                //we need to check if there's an intersection with the
                //top surface
                (hit1.z > 0.5F && ir.dir.z <0.0F) -> {
                    //compute how much time the ray has before missing the cylinder base
                    val tmin = t2-tHit
                    //evaluate the distance along z
                    val dz = hit1.z - 0.5F
                    //...and the time to get to the base
                    val tz = dz / abs(ir.dir.z)
                    //check if the time is enough
                    return if (tz < tmin) {
                        val hit = ir.at(tHit + tz)
                        HitRecord(
                            worldPoint = T * hit,
                            normal = T * VECZ.toNormal(),
                            surfacePoint = toSurPoint(hit),
                            t = tHit + tz,
                            ray = r,
                            shape = this
                        )
                    } else null
                }
                //otherwise we check for intersection with bottom surface
                (hit1.z < -0.5F && ir.dir.z >0.0F) -> {
                    //compute how much time the ray has before missing the cylinder base
                    val tmin = t2-tHit
                    //evaluate the distance along z
                    val dz = -0.5F -hit1.z
                    //...and the time to get to the base
                    val tz = dz / abs(ir.dir.z)
                    //check if the time is enough
                    return if (tz < tmin) {
                        val hit = ir.at(tHit + tz)
                        HitRecord(
                            worldPoint = T * hit,
                            normal = T * -VECZ.toNormal(),
                            surfacePoint = toSurPoint(hit),
                            t = tHit + tz,
                            ray = r,
                            shape = this
                        )
                    } else null
                }
                else -> return null
            }
        }
    }

    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        val ir = T.inverse() * r
        val hits = mutableListOf<HitRecord>()

        val oVx = ir.origin.x
        val oVy = ir.origin.y
        val a = ir.dir.x * ir.dir.x + ir.dir.y * ir.dir.y
        val b = oVx * ir.dir.x + oVy * ir.dir.y
        val c = oVx * oVx + oVy * oVy - 1

        //Intersections with lateral surface of cilynder
        val t1 = (-b - sqrt(b * b - a * c)) / a
        val t2 = (-b + sqrt(b * b - a * c)) / a

        if (t1 == Float.POSITIVE_INFINITY) {
            hits.add(
                HitRecord(
                    worldPoint = T * Point(ir.origin.x, ir.origin.y, 1.0F),
                    normal = T * VECZ.toNormal(),
                    surfacePoint = toSurPoint(Point(ir.origin.x, ir.origin.y, 1.0F)),
                    t = abs(ir.origin.z - 1.0F),
                    ray = r,
                    shape = this
                )
            )

            hits.add(
                HitRecord(
                    worldPoint = T * Point(ir.origin.x, ir.origin.y, 0.0F),
                    normal = T * -VECZ.toNormal(),
                    surfacePoint = toSurPoint(Point(ir.origin.x, ir.origin.y, 0.0F)),
                    t = abs(ir.origin.z),
                    ray = r,
                    shape = this
                )
            )
            return hits.sortedBy { it.t }
        }
/*
        //Check if vertically there's intersection
        val hit1 = ir.at(t1)
        if (hit1.z in 0.0F..1.0F )  hits.add(
            HitRecord(
                worldPoint = T * hit1,
                normal = T * getNormal(hit1, ir.dir),
                surfacePoint = toSurPoint(hit1),
                t = t1,
                ray = r,
                shape = this
            )
        )
        else if (hit1.z >1.0F) {
            val dz = hit1.z-1.0F
            if (abs(ir.dir.y * dz) < 2.0F && abs (ir.dir.x * dz) < 2.0F ) {
                val hit = ir.at(t1 + dz)
                hits.add(
                    HitRecord(
                        worldPoint = T * hit,
                        normal = T * VECZ.toNormal(),
                        surfacePoint = toSurPoint(hit),
                        t = t1 + dz,
                        ray = r,
                        shape = this
                    )
                )
            }

        }
        else {
            val dz = -hit1.z
            if (abs(ir.dir.y * dz) < 2.0F && abs (ir.dir.x * dz) < 2.0F ) {
                val hit = ir.at(t1 + dz)
                hits.add(
                    HitRecord(
                        worldPoint = T * hit,
                        normal = T * -VECZ.toNormal(),
                        surfacePoint = toSurPoint(hit),
                        t = t1 + dz,
                        ray = r,
                        shape = this
                    )
                )
            }
        }


 */
        val hit2 = ir.at(t2)
        if (hit2.z in 0.0F..1.0F) hits.add(
            HitRecord(
                worldPoint = T * hit2,
                normal = T * getNormal(hit2, ir.dir),
                surfacePoint = toSurPoint(hit2),
                t = t2,
                ray = r,
                shape = this
            )
        )
        /*
        else if (hit2.z >1.0F) {
            val dz = abs(hit1.z-1.0F)
            val hit = ir.at(t1 + dz)
                hits.add(
                    HitRecord(
                        worldPoint = T * hit,
                        normal = T * -VECZ.toNormal(),
                        surfacePoint = toSurPoint(hit),
                        t = t1 + dz,
                        ray = r,
                        shape = this
                    )
                )
            }
        else {
            val dz = hit1.z
            val hit = ir.at(t1 + dz)
                hits.add(
                    HitRecord(
                        worldPoint = T * hit,
                        normal = T * VECZ.toNormal(),
                        surfacePoint = toSurPoint(hit),
                        t = t1 + dz,
                        ray = r,
                        shape = this
                    )
                )
            }


         */

        return if (hits.isEmpty()) null
        else hits.sortedBy { it.t }
    }

    private fun getNormal(p: Point, rayDir: Vector): Normal {
        val n = Normal(p.x, p.y, 0.0F)
        return if (p.x * rayDir.x + p.y * rayDir.y < 0.0F) n else -n
    }


    private fun toSurPoint(hit: Point): Vector2d {
        return Vector2d(0.5F, 0.5F)
    }
}