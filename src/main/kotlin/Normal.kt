import kotlin.math.abs

class Normal (var x: Float = 0.0F, var y: Float = 0.0F, var z: Float = 0.0F) {
    fun isClose(other: Normal, epsilon: Float = 1e-10F): Boolean {
        return abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon
    }
}