abstract class Camera(
    val d : Float = 1.0F,
    val a : Float = 1.6F

) {
    abstract fun fireRay (u : Float, v: Float) : Ray
}