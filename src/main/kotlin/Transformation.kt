import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class Transformation(
    private var m: Array<FloatArray> = ID4X4,
    private var im: Array<FloatArray> = ID4X4
) {

    override fun toString(): String {
        var row = "(${m[0][0]} ${m[0][1]} ${m[0][2]} ${m[0][3]})"
        for (el in m.slice(1 until m.size)) {
            row += "\n\t(${el[0]} ${el[1]} ${el[2]} ${el[3]})"
        }
        return "T ( $row )"
    }

    /*
    Private supporting functions
        areMatrixClose  --> Check if all correspondent elements of two matrices are equal within epsilon
        matrixProd      --> Implements the row x col product for 2 given matrices
     */
    private fun areMatrixClose(m1: Array<FloatArray>, m2: Array<FloatArray>, epsilon: Float = 1e-4F): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (abs(m1[i][j] - m2[i][j]) > epsilon) {
                    return false
                }
            }
        }
        return true
    }

    private fun matrixProd(a: Array<FloatArray>, b: Array<FloatArray>): Array<FloatArray> {

        val result = Array(a.size) { FloatArray(b[0].size) { 0.0F } }
        for (j in result.indices) {
            for (i in 0 until result[0].size) {
                for (k in 0 until a[0].size) {
                    result[i][j] += a[i][k] * b[k][j]
                }
            }
        }
        return result
    }

    /*
    Overloading of operator * (times)
        T * Transformation
            Vector
            Point
            Normal
            Ray
     */
    operator fun times(other: Transformation): Transformation {
        return Transformation(matrixProd(m, other.m), matrixProd(other.im, im))
    }

    operator fun times(other: Vector): Vector {
        val row0 = m[0]
        val row1 = m[1]
        val row2 = m[2]
        return Vector(
            row0[0] * other.x + row0[1] * other.y + row0[2] * other.z,
            row1[0] * other.x + row1[1] * other.y + row1[2] * other.z,
            row2[0] * other.x + row2[1] * other.y + row2[2] * other.z
        )
    }

    operator fun times(other: Point): Point {
        val row0 = m[0]
        val row1 = m[1]
        val row2 = m[2]
        val row3 = m[3]
        val p = Point(
            row0[0] * other.x + row0[1] * other.y + row0[2] * other.z + row0[3],
            row1[0] * other.x + row1[1] * other.y + row1[2] * other.z + row1[3],
            row2[0] * other.x + row2[1] * other.y + row2[2] * other.z + row2[3]
        )
        val lambda = other.x * row3[0] + other.y * row3[1] + other.z * row3[2] + row3[3]
        return if (lambda == 1.0F) {
            p
        } else {
            Point(p.x / lambda, p.y / lambda, p.z / lambda)
        }
    }

    operator fun times(other: Normal): Normal {
        val row0 = im[0]
        val row1 = im[1]
        val row2 = im[2]
        return Normal(
            row0[0] * other.x + row1[0] * other.y + row2[0] * other.z,
            row0[1] * other.x + row1[1] * other.y + row2[1] * other.z,
            row0[2] * other.x + row1[2] * other.y + row2[2] * other.z
        )
    }

    operator fun times(other: Ray): Ray {
        return Ray(
            this * other.origin,
            this * other.dir,
            other.tmin,
            other.tmax,
            other.depth
        )
    }

    operator fun times(other: HitRecord): HitRecord {
        return HitRecord(
            worldPoint = this * other.worldPoint,
            normal = this * other.normal,
            surfacePoint = other.surfacePoint,
            t = other.t,
            ray = this * other.ray,
            shape = other.shape
        )
    }

    /*
    Transformations
        inverse     --> returns the inverse of this Transformation
        translation --> returns the Translation of the given Vector
        scaling     --> returns the scale Transformation of the given Vector
        rotationK   --> K = X, Y, Z  returns the rotation around axis K of the given angle (in radians)
     */
    fun inverse(): Transformation {
        return Transformation(im, m)
    }


    fun translation(vec: Vector): Transformation {
        val m = arrayOf(
            floatArrayOf(1.0F, 0.0F, 0.0F, vec.x), floatArrayOf(0.0F, 1.0F, 0.0F, vec.y),
            floatArrayOf(0.0F, 0.0F, 1.0F, vec.z), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        val im = arrayOf(
            floatArrayOf(1.0F, 0.0F, 0.0F, -vec.x), floatArrayOf(0.0F, 1.0F, 0.0F, -vec.y),
            floatArrayOf(0.0F, 0.0F, 1.0F, -vec.z), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        return Transformation(m, im)
    }


    fun scaling(vec: Vector): Transformation {
        val m = arrayOf(
            floatArrayOf(vec.x, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, vec.y, 0.0F, 0.0F),
            floatArrayOf(0.0F, 0.0F, vec.z, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        val im = arrayOf(
            floatArrayOf(1 / vec.x, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, 1 / vec.y, 0.0F, 0.0F),
            floatArrayOf(0.0F, 0.0F, 1 / vec.z, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        return Transformation(m, im)
    }

    fun rotationX(angle: Float): Transformation { //angle must be in radians
        var radAngle = angle*(180.0F/PI.toFloat())
        val COS = cos(radAngle)
        val SIN = sin(radAngle)
        val m = arrayOf(
            floatArrayOf(1.0F, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, COS, -SIN, 0.0F),
            floatArrayOf(0.0F, SIN, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        val im = arrayOf(
            floatArrayOf(1.0F, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, COS, SIN, 0.0F),
            floatArrayOf(0.0F, -SIN, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        return Transformation(m, im)
    }

    fun rotationY(angle: Float): Transformation { //angle must be in radians
        var radAngle = angle*(180.0F/PI.toFloat())
        val COS = cos(radAngle)
        val SIN = sin(radAngle)
        val m = arrayOf(
            floatArrayOf(COS, 0.0F, SIN, 0.0F), floatArrayOf(0.0F, 1.0F, 0.0F, 0.0F),
            floatArrayOf(-SIN, 0.0F, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        val im = arrayOf(
            floatArrayOf(COS, 0.0F, -SIN, 0.0F), floatArrayOf(0.0F, 1.0F, 0.0F, 0.0F),
            floatArrayOf(SIN, 0.0F, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        return Transformation(m, im)
    }

    fun rotationZ(angle: Float): Transformation { //angle must be in radians
        var radAngle = angle*(180.0F/PI.toFloat())
        val COS = cos(radAngle)
        val SIN = sin(radAngle)
        val m = arrayOf(
            floatArrayOf(COS, -SIN, 0.0F, 0.0F), floatArrayOf(SIN, COS, 0.0F, 0.0F),
            floatArrayOf(0.0F, 0.0F, 1.0F, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        val im = arrayOf(
            floatArrayOf(COS, SIN, 0.0F, 0.0F), floatArrayOf(-SIN, COS, 0.0F, 0.0F),
            floatArrayOf(0.0F, 0.0F, 1.0F, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F)
        )
        return Transformation(m, im)
    }


    /*
    Testing Functions
        isClose         --> Check if Transformation (both m and im) are equal to a given Transformation within epsilon
        isConsistent    --> Check that for this Transformation m*im = ID4X4
     */
    fun isClose(other: Transformation, epsilon: Float = 1e-5F): Boolean {
        return areMatrixClose(m, other.m, epsilon) && areMatrixClose(im, other.im, epsilon)
    }

    fun isConsistent(): Boolean {
        return areMatrixClose(ID4X4, matrixProd(m, im))
    }


}
