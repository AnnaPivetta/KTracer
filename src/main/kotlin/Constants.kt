//Terminal fancy settings
const val RED = "\u001b[0;31m"
const val RESET = "\u001b[0m"

//Geometry constants
val ID4X4 = Array(4) { i -> FloatArray(4) { k -> if (k != i) 0.0F else 1.0F } }
val VEC_X = Vector(1.0F, 0.0F, 0.0F)
val VEC_Y = Vector(0.0F, 1.0F, 0.0F)
val VEC_Z = Vector(0.0F, 0.0F, 1.0F)