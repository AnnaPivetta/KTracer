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

}