import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

val ID4X4 = Array(4) { i -> FloatArray(4) { k -> if (k != i) 0.0F else 1.0F } }

class Transformation(
    private var m: Array<FloatArray> = ID4X4,
    private var im: Array<FloatArray> = ID4X4
) {
    fun isClose (other: Transformation, epsilon : Float = 1e-10F) : Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4){
                if (abs(m[i][j] - other.m[i][j]) > epsilon) {return false}
            }
        }
        return true
    }

    fun inverse() : Transformation{
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
    override fun toString(): String {
        var row = "(${m[0][0]} ${m[0][1]} ${m[0][2]} ${m[0][3]})"
        for (el in m.slice(1 until m.size)) {

            row += "\n\t(${el[0]} ${el[1]} ${el[2]} ${el[3]})"
        }
        return "T ( " + row + " )"
    }

    private fun matrixProd(a: Array<FloatArray>, b: Array<FloatArray>): Array<FloatArray> {
 /*       if (a[0].size != b.size) {
            throw MatrixFormatException("Invalid matrices size: must be A[a][m] * B[m][b]")
            return Array(0) { FloatArray(0) }
        }

  */
        val result = Array(a.size) { FloatArray(b[0].size) { 0.0F } }
        for (j in 0 until result.size) {
            for (i in 0 until result[0].size) {
                for (k in 0 until a[0].size) {
                    result[i][j] += a[i][k] * b[k][j]
                }
            }
        }
        return result
    }

    operator fun times(other: Transformation): Transformation {
        return Transformation(matrixProd(m, other.m), matrixProd(other.im, im))
    }

    operator fun times(other: Vector) : Vector {
        val row0 = m[0]
        val row1 = m[1]
        val row2 = m[2]
        return Vector(
            row0[0] * other.x + row0[0] * other.y + row0[0] * other.z,
            row1[0] * other.x + row1[0] * other.y + row1[0] * other.z,
            row2[0] * other.x + row2[0] * other.y + row2[0] * other.z
        )
    }

    operator fun times(other: Point) : Point {
        val row0 = m[0]
        val row1 = m[1]
        val row2 = m[2]
        val p =  Point(
            row0[0] * other.x + row0[0] * other.y + row0[0] * other.z,
            row1[0] * other.x + row1[0] * other.y + row1[0] * other.z,
            row2[0] * other.x + row2[0] * other.y + row2[0] * other.z
        )
        val lambda = p.x+p.y+p.z
        if (lambda == 1.0F) return p
        else return Point(p.x/lambda, p.y/lambda, p.z/lambda)
    }


    fun scaling (vec: Vector) :Transformation {
        val m = arrayOf(
        floatArrayOf(vec.x, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, vec.y, 0.0F, 0.0F),
        floatArrayOf(0.0F, 0.0F, vec.z, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        val im = arrayOf(
            floatArrayOf(1/vec.x, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, 1/vec.y, 0.0F, 0.0F),
            floatArrayOf(0.0F, 0.0F, 1/vec.z, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        return Transformation(m, im)
    }

    fun rotationX (angle : Float) : Transformation { //angle must be in radins
        val COS = cos(angle)
        val SIN = sin(angle)
        val m = arrayOf(
            floatArrayOf(1.0F, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, COS, -SIN, 0.0F),
            floatArrayOf(0.0F, SIN, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        val im = arrayOf(
            floatArrayOf(1.0F, 0.0F, 0.0F, 0.0F), floatArrayOf(0.0F, COS, SIN, 0.0F),
            floatArrayOf(0.0F, -SIN, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        return Transformation(m, im)
    }

    fun rotationY (angle : Float) : Transformation { //angle must be in radins
        val COS = cos(angle)
        val SIN = sin(angle)
        val m = arrayOf(
            floatArrayOf(COS, 0.0F, SIN, 0.0F), floatArrayOf(0.0F, 1.0F, 0.0F, 0.0F),
            floatArrayOf(-SIN, 0.0F, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        val im = arrayOf(
            floatArrayOf(COS, 0.0F, -SIN, 0.0F), floatArrayOf(0.0F, 1.0F, 0.0F, 0.0F),
            floatArrayOf(SIN, 0.0F, COS, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        return Transformation(m, im)
    }

    fun rotationZ (angle : Float) : Transformation { //angle must be in radins
        val COS = cos(angle)
        val SIN = sin(angle)
        val m = arrayOf(
            floatArrayOf(COS, -SIN, 0.0F, 0.0F), floatArrayOf(SIN, COS, 0.0F, 0.0F),
            floatArrayOf(0.0F, 0.0F, 1.0F, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        val im = arrayOf(
            floatArrayOf(COS, SIN, 0.0F, 0.0F), floatArrayOf(-SIN, COS, 0.0F, 0.0F),
            floatArrayOf(0.0F, 0.0F, 1.0F, 0.0F), floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F))
        return Transformation(m, im)
    }





}
