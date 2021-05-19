import java.lang.IndexOutOfBoundsException
import kotlin.math.abs

class Point (var x : Float = 0.0F, var y : Float = 0.0F, var z : Float = 0.0F){
    override fun toString() : String {
        return "($x;$y;$z)"
    }

    fun isClose(other: Point, epsilon: Float = 1e-10F): Boolean {
        return abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon
    }

    fun toVector () : Vector {
        return this - Point(0.0F, 0.0F, 0.0F)
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

    operator fun get(i : Int) : Float {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> {
                throw IndexOutOfBoundsException()
            }
        }
    }
}

