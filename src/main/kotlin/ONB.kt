import java.awt.List

fun createONBfromZ (normal : Normal) :  Triple<Vector, Vector, Vector> {
    normal.normalize()
    val sign = if (normal.z > 0.0F) {
        1.0F
    }
    else {
        -1.0F
    }
    val a = (-1.0F/(sign + normal.z))
    val b = normal.x * normal.y * a

    val e1 = Vector(1.0F + sign * normal.x * normal.x * a, sign * b,-sign*normal.x)
    val e2 = Vector(b, sign + normal.y * normal.y *a, -normal.y)

    return Triple(e1, e2, Vector(normal.x, normal.y, normal.z))






}