import kotlin.math.abs
import kotlin.math.sqrt

class Normal (var x: Float = 0.0F, var y: Float = 0.0F, var z: Float = 0.0F) {

    fun isClose(other: Normal, epsilon: Float = 1e-10F): Boolean {
        return abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon
    }

    operator fun unaryMinus() : Normal {
        return Normal(-x, -y, -z)
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