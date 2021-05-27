//Terminal fancy settings
const val RED = "\u001b[0;31m"
const val RESET = "\u001b[0m"

//Geometry constants
val ID4X4 = Array(4) { i -> FloatArray(4) { k -> if (k != i) 0.0F else 1.0F } }
val VECX = Vector(1.0F, 0.0F, 0.0F)
val VECY = Vector(0.0F, 1.0F, 0.0F)
val VECZ = Vector(0.0F, 0.0F, 1.0F)

//Color Constants
val WHITE = Color(1.0F, 1.0F, 1.0F)
val BLACK = Color()
val NAVY = Color(0.0F, 0.0F, 128.0F)
val SKYBLUE = Color(0.5294117647F, 0.80784313725F, 0.92156862745F)
val SILVER = Color(0.75294117647F, 0.75294117647F, 0.75294117647F)
val CRIMSON = Color(0.86274509803F, 0.07843137254F, 0.23529411764F)
val DARKCYAN = Color(0.0F, 0.54509803921F, 0.54509803921F)
val OLIVE = Color(0.50196078431F, 0.50196078431F, 0.0F)
val PINK = Color (1.0F, 0.75294117647F, 0.79607843137F )