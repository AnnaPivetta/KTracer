class ImageTracer (
    var image : HdrImage,
    var camera: Camera
    )  {

    fun fireRay (col : Int, row : Int, uPixel : Float =0.5F, vPixel :Float =0.5F) : Ray {
        val u = (col + uPixel)/image.getWidth()
        val v = 1.0F - (row + vPixel)/image.getHeight()
        return camera.fireRay(u, v)
    }
    fun fireAllRays (function : (Ray) -> Color ) {
        for (row in 0 until image.getHeight()) {
            for (col in 0 until image.getWidth()){
                val ray = fireRay(col, row)
                val color = function(ray)
                image.setPixel(col, row, color)
            }
        }

    }
    @ExperimentalUnsignedTypes
    fun fireAllRays(function: (Ray) ->Color, AAgrid : Int?) {
        if (AAgrid == null) {
            fireAllRays(function)
            return
        } else {
            for (row in 0 until image.getHeight()) {
                for (col in 0 until image.getWidth()) {

                    val pcg = PCG()
                    var addColor = Color(0.0F, 0.0F, 0.0F)
                    for (i in 0 until AAgrid) {
                        for (j in 0 until AAgrid) {
                            val newU = pcg.rand((1.0F / AAgrid) * i, (1.0F / AAgrid) * (i + 1))
                            val newV = pcg.rand((1.0F / AAgrid) * j, (1.0F / AAgrid) * (j + 1))
                            val ray = fireRay(col, row, newU, newV)
                            val color = function(ray)
                            addColor+=color
                        }
                    }
                    image.setPixel(
                        col,
                        row,
                        addColor*(1.0F/(AAgrid*AAgrid))
                    )
                }
            }
        }
    }
}