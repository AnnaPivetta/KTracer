import kotlin.math.abs
import kotlin.math.sqrt

class Normal (var x: Float = 0.0F, var y: Float = 0.0F, var z: Float = 0.0F) {

    override fun toString(): String {
        return "Normal ($x, $y, $z)"
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

    fun isClose(other: Normal, epsilon: Float = 1e-10F): Boolean {
        return abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon
    }

    fun norm2(): Float {
        return x * x + y * y + z * z
    }

    fun norm(): Float {
        return sqrt(norm2())
    }

    fun normalize () {
        val n = norm()
        x /= n
        y /= n
        z /= n
    }


}