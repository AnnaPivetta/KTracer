class ImageTracer (
    var image : HdrImage,
    var camera: Camera
    )  {

    fun fireRay (col : Int, row : Int, uPixel : Float =0.5F, vPixel :Float =0.5F) : Ray {
        val u = (col + uPixel)/(image.getWidth() -1)
        val v = (row + vPixel)/(image.getHeight() -1)
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