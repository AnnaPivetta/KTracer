import kotlin.math.abs

class Point (var x : Float = 0.0F, var y : Float = 0.0F, var z : Float = 0.0F){
    override fun toString() : String {
        return "Point ($x; $y; $z)"
    }

    fun isClose(other: Point, epsilon: Float = 1e-10F): Boolean {
        return abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon
    }

    operator fun plus(vec: Vector) : Point {
        return Point(x+vec.x, y+vec.y, z+vec.z)
    }

    operator fun minus(other: Point) : Vector{
        return Vector(x-other.x, y-other.y, z-other.z)
    }

    operator fun minus(vec: Vector) : Point{
        return Point(x-vec.x, y-vec.y, z-vec.z)
    }





}

