import java.lang.IndexOutOfBoundsException
import kotlin.math.abs
import kotlin.math.sqrt

data class Vector(var x: Float = 0.0F, var y: Float = 0.0F, var z: Float = 0.0F) {

    override fun toString(): String {
        return "Vector ($x, $y, $z)"
    }

    fun isClose(other: Vector, epsilon: Float = 1e-10F): Boolean {
        return abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon
    }

    fun toNormal(): Normal {
        return Normal (x, y, z)
    }

    operator fun plus(other: Vector): Vector {
        return Vector(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector): Vector {
        return Vector(x - other.x, y - other.y, z - other.z)
    }

    operator fun unaryMinus(): Vector {
        return Vector(-x, -y, -z)
    }

    operator fun times(scalar: Float): Vector {
        return Vector(x * scalar, y * scalar, z * scalar)
    }

    operator fun times(other: Vector): Float {
        return x * other.x + y * other.y + z * other.z
    }


    //Same implementation for multiple choice of usage
    fun dot (other: Vector): Float {
        return x * other.x + y * other.y + z * other.z
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

    fun cross(other: Vector): Vector {
        return Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    fun norm2(): Float {
        return x * x + y * y + z * z
    }

    fun norm(): Float {
        return sqrt(norm2())
    }

    fun normalize() {
        val n = norm()
        x /= n
        y /= n
        z /= n
    }
}