import kotlin.math.abs

class Normal (var x: Float = 0.0F, var y: Float = 0.0F, var z: Float = 0.0F) {

    fun isClose(other: Normal, epsilon: Float = 1e-10F): Boolean {
        return abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon
    }

    operator fun unaryMinus() : Normal {
        return Normal(-x, -y, -z)
    }

    operator fun times(c : Float) : Normal {
        return Normal (x*c, y*c, z*c)
    }

    fun toVector () : Vector {
        return Vector(x, y, z)
    }
}