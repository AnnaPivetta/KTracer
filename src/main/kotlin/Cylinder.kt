import kotlin.math.*
import kotlin.reflect.typeOf

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
        return canP.x * canP.x + canP.y * canP.y <= 1.0F && canP.z in -0.5F..0.5F
    }

    /**
     * This function evaluates if the given [Ray] intersects the [Cylinder] and returns the
     * closest intersection from the observer point of view.
     *
     * It's a wrapping of [rayIntersectionList], returning the first component of the list, if not empty.
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the closest intersection to the [Ray.origin] if any. Otherwise null is returned
     *
     * @see rayIntersectionList
     */

    override fun rayIntersection(r: Ray): HitRecord? {
        //Probably this is not the best way to do it
        //We can save some time without searching for the II interaction
       return rayIntersectionList(r)?.get(0)
    }

    /**
     * This function evaluates if the given [Ray] intersects the [Cylinder] and returns the
     * list of all the intersections
     *
     * This method is used for CSG.
     *
     * @param r The [Ray] to check the intersections with
     * @return A [List] of[HitRecord] containing the intersections if any. Otherwise null is returned
     *
     * @see CSGUnion
     * @see CSGDifference
     * @see CSGIntersection
     */
    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        val ir = T.inverse() * r
        val hits = mutableListOf<HitRecord>()

        //Solve the quadratic equation for ray intersecting the
        //cylinder lateral surface
        val oVx = ir.origin.x
        val oVy = ir.origin.y
        val a = ir.dir.x * ir.dir.x + ir.dir.y * ir.dir.y
        val b = oVx * ir.dir.x + oVy * ir.dir.y
        val c = oVx * oVx + oVy * oVy - 1

        //Intersections with lateral surface of cylinder
        var t1 = (-b - sqrt(b * b - a * c)) / a
        var t2 = (-b + sqrt(b * b - a * c)) / a

        if(a == 0.0F) {
            t1 = Float.POSITIVE_INFINITY
            t2 = Float.POSITIVE_INFINITY
        }


        val xypos = ir.origin.x * ir.origin.x + ir.origin.y * ir.origin.y
        //time to reach the upper face
        val tzUp = (-sign(ir.dir.z) * 0.5F - ir.origin.z) / ir.dir.z
        //time to reach the bottom face
        val tzDown = (sign(ir.dir.z) * 0.5F - ir.origin.z) / ir.dir.z

        //if origin of ray inside the infinite cylinder with r = 1
        //check possible intersections with top/bottom face
        if (xypos <= 1.0F) {
            if (tzUp in r.tmin..t2) {
                val hit = ir.at(tzUp)
                hits.add(
                    HitRecord(
                        worldPoint = T * hit,
                        normal = T * (-VECZ.toNormal() * sign(ir.dir.z)),
                        surfacePoint = toSurPoint(hit),
                        t = tzUp,
                        ray = r,
                        shape = this
                    )
                )
            }

            if (tzDown in r.tmin..t2) {
                val hit = ir.at(tzDown)
                hits.add(
                    HitRecord(
                        worldPoint = T * hit,
                        normal = T * (-VECZ.toNormal() * sign(ir.dir.z)),
                        surfacePoint = toSurPoint(hit),
                        t = tzDown,
                        ray = r,
                        shape = this
                    )
                )
            }
        } else if (t1 in r.tmin..r.tmax) {
            //Check if intersection point's  z component is in the cylinder
            val hit1 = ir.at(t1)
            when {
                (hit1.z in -0.5F..0.5F) -> hits.add(
                    HitRecord(
                        worldPoint = T * hit1,
                        normal = T * getNormal(hit1, ir.dir),
                        surfacePoint = toSurPoint(hit1),
                        t = t1,
                        ray = r,
                        shape = this
                    )
                )
                //If the intersection would occur above the edge
                //we need to check if there's an intersection with the
                //top surface
                (hit1.z > 0.5F && ir.dir.z < 0.0F) -> {
                    //compute how much time the ray has, before missing the cylinder base
                    val tmin = t2 - t1
                    //evaluate the distance along z
                    val dz = hit1.z - 0.5F
                    //...and the time to get to the base
                    val tz = dz / abs(ir.dir.z)
                    //check if the time is enough
                    if (tz < tmin) {
                        //set where is the first intersection
                        t1 += tz
                        val hit = ir.at(t1)
                        hits.add(
                            HitRecord(
                                worldPoint = T * hit,
                                normal = T * VECZ.toNormal(),
                                surfacePoint = toSurPoint(hit),
                                t = t1,
                                ray = r,
                                shape = this
                            )
                        )
                        //If no first intersection occurs
                        //then no intersection will ever occur
                    } else return null
                }
                //otherwise we check for intersection with bottom surface
                (hit1.z < -0.5F && ir.dir.z > 0.0F) -> {
                    //compute how much time the ray has before missing the cylinder base
                    val tmin = t2 - t1
                    //evaluate the distance along z
                    val dz = -0.5F - hit1.z
                    //...and the time to get to the base
                    val tz = dz / ir.dir.z
                    //check if the time is enough
                    if (tz < tmin) {
                        //set where is the first intersection
                        t1 += tz
                        val hit = ir.at(t1)
                        hits.add(
                            HitRecord(
                                worldPoint = T * hit,
                                normal = T * -VECZ.toNormal(),
                                surfacePoint = toSurPoint(hit),
                                t = t1,
                                ray = r,
                                shape = this
                            )
                        )
                    } else return null
                }
                //must close "when" statement,
                //if z direction of ray is not good then there's no intersection
                else -> return null
            }
        }
        else return null

        //inside the cylinder already checked for bases intersection
        //so we add eventually an intersection with the lateral surface
        if (xypos <=1.0F) {
            val hit2 = ir.at(t2)
            if (t2 in r.tmin..r.tmax && hit2.z in -0.5F..0.5F) hits.add(
                HitRecord(
                    worldPoint = T * hit2,
                    normal = T * getNormal(hit2, ir.dir),
                    surfacePoint = toSurPoint(hit2),
                    t = t2,
                    ray = r,
                    shape = this
                )
            )
        }
        //now check II intersection when origin outside the infinite cylinder
        else {
            if (t2 in r.tmin..r.tmax) {
                //Check if intersection point z component is in the cylinder
                val hit2 = ir.at(t2)
                when {
                    (hit2.z in -0.5F..0.5F) -> hits.add(
                        HitRecord(
                            worldPoint = T * hit2,
                            normal = T * getNormal(hit2, ir.dir),
                            surfacePoint = toSurPoint(hit2),
                            t = t2,
                            ray = r,
                            shape = this
                        )
                    )
                    //If the intersection would occur above the edge
                    //then it means that the intersection will occur with the top base
                    (hit2.z > 0.5F) -> {
                        //the intersection will not be in hit2, but in the point
                        //where the ray meets the base
                        val dz = 0.5F - ir.origin.z
                        //time to get to the base
                        val tz = dz / ir.dir.z
                        val hit = ir.at(tz)
                        hits.add(
                            HitRecord(
                                worldPoint = T * hit,
                                normal = T * -VECZ.toNormal(),
                                surfacePoint = toSurPoint(hit),
                                t = tz,
                                ray = r,
                                shape = this
                            )
                        )
                    }
                    //otherwise we check for intersection with bottom surface
                    (hit2.z < -0.5F) -> {
                        //the intersection will not be in hit2, but in the point
                        //where the ray meets the base
                        val dz = -0.5F - ir.origin.z
                        //time to get to the base
                        val tz = dz / ir.dir.z
                        val hit = ir.at(tz)
                        hits.add(
                            HitRecord(
                                worldPoint = T * hit,
                                normal = T * VECZ.toNormal(),
                                surfacePoint = toSurPoint(hit),
                                t = tz,
                                ray = r,
                                shape = this
                            )
                        )
                    }
                    //must close "when" statement,
                    //i think this should never be called
                    else -> return null
                }
            }
        }
        return if (hits.isEmpty()) null
        else hits.sortedBy { it.t }
    }


    private fun getNormal(p: Point, rayDir: Vector): Normal {
            val n = Normal(p.x, p.y, 0.0F)
            return if (p.x * rayDir.x + p.y * rayDir.y < 0.0F) n else -n
    }

    /**
     * U, V mapping for cylinder
     * (0,0)     (0.5, 0)         (1,0)
     * +---------------------------+
     * |             |             |
     * |    bottom   |     top     |
     * |     face    |    face     |
     * |             |             |
     * +---------------------------+ (1, 0.5)
     * |                           |
     * |        lateral face       |
     * |                           |
     * |                           |
     * +---------------------------+
     * (0,1)                        (1,1)
     */
    private fun toSurPoint(hit: Point): Vector2d {
        return when {
            hit.z.isClose(0.5F) -> Vector2d( //Top face
                u = 0.5F + (hit.x + 1.0F) / 4.0F,    // x is in [-1,1] -> x+1 in [0,2] -> (x+1)/4 in [0,0.5]
                v = (hit.y + 1.0F) / 4.0F           //same as x
            )
            hit.z.isClose(-0.5F) -> Vector2d(    //Bottom face
                u = (hit.x + 1.0F) / 4.0F,    // x is in [-1,1] -> x+1 in [0,2] -> (x+1)/4 in [0,0.5]
                v = (hit.y + 1.0F) / 4.0F           //same as x
            )
            else -> Vector2d(        //Lateral Face
                u = ((atan2(hit.y, hit.x) + (2.0F * PI.toFloat())) % (2.0F * PI.toFloat())) / (2.0F * PI.toFloat()),
                v = 1.0F - (hit.z + 0.5F) / 2.0F
            )
        }
    }
}