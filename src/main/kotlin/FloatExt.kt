import kotlin.math.abs

//Implementation of Float class Extension methods
//operator times is overloaded for product with:
                                                //Vectors
                                                //Colors

operator fun Float.times (v: Vector) : Vector {
    return Vector(this*v.x, this*v.y, this*v.z)
}

operator fun Float.times (c: Color) : Color {
    return Color(this*c.r, this*c.g, this*c.b)
}

fun Float.isClose(f : Float, epsilon : Float =10e-10F) : Boolean {
    return abs(this-f) < epsilon
}