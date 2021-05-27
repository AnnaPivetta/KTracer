/**
 * Constructive Solid Geometry (CSG)
 *
 * CSG provides a simple way of creating complex objects by simple ones, using boolean operators.
 * CSG are created as [Shape] objects in order to provide the possibility of combining more operations
 *
 * [CSGUnion]
 *
 * This class implements the Union between two shapes
 *
 * Class properties:
 * - [s1] - The first [Shape]
 * - [s2] - The second [Shape]
 * - [T] - (optional) The [Transformation] to apply to the Union
 *
 * @see CSGIntersection
 * @see CSGDifference
 * @see Shape
 *
 */
class CSGUnion (val s1: Shape, val s2: Shape, T : Transformation = Transformation()) : Shape (T, s1.material){

    /**
     * In Union a point is internal if it's inside either [s1] or [s2]
     */
    override fun isPointInternal(p: Point): Boolean {
        return s1.isPointInternal(p) or s2.isPointInternal(p)
    }

    /**
     * This function evaluates if the given [Ray] intersects the Union and returns the
     * closest intersection from the observer point of view.
     *
     * All intersections with [s1] and [s2] are computed and then it's performed a search for the
     * closest
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the  closest intersection to the [Ray.origin] if any.
     * Otherwise null is returned
     */
    override fun rayIntersection(r: Ray): HitRecord? {
        val hits = mutableListOf<HitRecord>()
        val hits1 = s1.rayIntersectionList(r)?.toList()
        val hits2 = s2.rayIntersectionList(r)?.toList()
        if (hits1 != null) hits.addAll(hits1)
        if (hits2 != null) hits.addAll(hits2)
        return if (hits.isEmpty()) null
        else {
            var closest = hits[0]
            for (h in hits.slice(1 until hits.size)) {
                if (h.t < closest.t) closest = h
            }
            closest
        }
    }

    /**
     * This function evaluates if the given [Ray] intersects the Union and returns all the
     * intersection from the observer point of view (only non-internal intersections are considered)
     *
     * All intersections with [s1] and [s2] are computed and then the internal ones are discarded.
     *
     * @param r The [Ray] to check the intersection with
     * @return A [List] of [HitRecord] containing all the intersections if any, sorted by increasing distance.
     * Otherwise null is returned
     */
    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        val hits = mutableListOf<HitRecord>()
        val hits1 = s1.rayIntersectionList(r)?.toList()
        val hits2 = s2.rayIntersectionList(r)?.toList()
        if (hits1 != null) {
            for (h in hits1) {
                if (!s2.isPointInternal(h.worldPoint)) hits.add(h)
            }
        }
        if (hits2 != null) {
            for (h in hits2) {
                if (!s1.isPointInternal(h.worldPoint)) hits.add(h)
            }
        }
        return if (hits.isEmpty()) null
        else hits.sortedBy { it.t }
    }
}
/**
 * Constructive Solid Geometry (CSG)
 *
 * CSG provides a simple way of creating complex objects by simple ones, using boolean operators.
 * CSG are created as [Shape] objects in order to provide the possibility of combining more operations
 *
 * [CSGDifference]
 *
 * This class implements the Difference between first shape and second shape
 *
 * Class properties:
 * - [s1] - The first [Shape]
 * - [s2] - The second [Shape]
 * - [T] - (optional) The [Transformation] to apply to the Difference
 *
 * @see CSGUnion
 * @see CSGIntersection
 * @see Shape
 *
 */

class CSGDifference (val s1: Shape, val s2: Shape, T : Transformation = Transformation()) : Shape (T, s1.material) {

    /**
     * In Difference a point is internal if it's inside [s1] and it's outside [s2]
     */
    override fun isPointInternal(p: Point): Boolean {
        return s1.isPointInternal(p) and !s2.isPointInternal(p)
    }

    /**
     * This function evaluates if the given [Ray] intersects the Difference and returns the
     * closest intersection from the observer point of view.
     *
     * All intersections are evaluated with [rayIntersectionList] and then the closest
     * (*i.e.* the  first one) is selected
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the  closest intersection to the [Ray.origin] if any.
     * Otherwise null is returned
     *
     * @see rayIntersectionList
     */
    override fun rayIntersection(r: Ray): HitRecord? {
        return rayIntersectionList(r)?.get(0)
        }


    /**
     * This function evaluates if the given [Ray] intersects the Difference and returns all the
     * intersection from the observer point of view (only non-internal intersections are considered)
     *
     * All intersections with [s1] and [s2] are computed and then only the ones with the
     * following properties are selected:
     * - On [s1] border and outside [s2]
     * - On [s2] border and inside [s1]
     *
     * @param r The [Ray] to check the intersection with
     * @return A [List] of [HitRecord] containing all the intersections if any, sorted by increasing distance.
     * Otherwise null is returned
     */

    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        val hits = mutableListOf<HitRecord>()
        val hits1 = s1.rayIntersectionList(r)?.toList()
        val hits2 = s2.rayIntersectionList(r)?.toList()
        if (hits1 != null) {
            for (h in hits1) {
                if (!s2.isPointInternal(h.worldPoint)) hits.add(h)
            }
        }
        if (hits2 != null) {
            for (h in hits2) {
                if (s1.isPointInternal(h.worldPoint)) hits.add(h)
            }
        }
        return if (hits.isEmpty()) null
        else hits.sortedBy { it.t }
    }
}

/**
 * Constructive Solid Geometry (CSG)
 *
 * CSG provides a simple way of creating complex objects by simple ones, using boolean operators.
 * CSG are created as [Shape] objects in order to provide the possibility of combining more operations
 *
 * [CSGIntersection]
 *
 * This class implements the Intersection between two shapes
 *
 * Class properties:
 * - [s1] - The first [Shape]
 * - [s2] - The second [Shape]
 * - [T] - (optional) The [Transformation] to apply to the Intersection
 *
 * @see CSGUnion
 * @see CSGDifference
 * @see Shape
 *
 */

class CSGIntersection (val s1: Shape, val s2: Shape, T : Transformation = Transformation()) : Shape (T, s1.material){

    /**
     * In Intersection a point is internal if it's inside both [s1] and [s2]
     */
    override fun isPointInternal(p: Point): Boolean {
        return s1.isPointInternal(p) and s2.isPointInternal(p)
    }

    /**
     * This function evaluates if the given [Ray] intersects the Intersection and returns the
     * closest intersection from the observer point of view.
     *
     * All intersections are evaluated with [rayIntersectionList] and then it's performed a search
     * for the closest
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the  closest intersection to the [Ray.origin] if any.
     * Otherwise null is returned
     *
     * @see rayIntersectionList
     */

    override fun rayIntersection(r: Ray): HitRecord? {
        return rayIntersectionList(r)?.get(0)
    }

    /**
     * This function evaluates if the given [Ray] intersects the Intersection and returns all the
     * intersection from the observer point of view (only non-internal intersections are considered)
     *
     * All intersections with [s1] and [s2] are computed and then only the ones with the
     * following properties are selected:
     * - On [s1] border and inside [s2]
     * - On [s2] border and inside [s1]
     *
     * @param r The [Ray] to check the intersection with
     * @return A [List] of [HitRecord] containing all the intersections if any, sorted by increasing distance.
     * Otherwise null is returned
     */
    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        val hits = mutableListOf<HitRecord>()
        val hits1 = s1.rayIntersectionList(r)?.toList()
        val hits2 = s2.rayIntersectionList(r)?.toList()
        if (hits1 != null) {
            for (h in hits1) {
                if (s2.isPointInternal(h.worldPoint)) hits.add(h)
            }
        }
        if (hits2 != null) {
            for (h in hits2) {
                if (s1.isPointInternal(h.worldPoint)) hits.add(h)
            }
        }
        return if (hits.isEmpty()) null
        else hits.sortedBy { it.t }
    }
}