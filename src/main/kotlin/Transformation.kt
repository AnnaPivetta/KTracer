val ID4X4 = Array(4) { i -> FloatArray(4) { k -> if (k != i) 0.0F else 1.0F } }

class Transformation(
    private var m: Array<FloatArray> = ID4X4,
    private var im: Array<FloatArray> = ID4X4
) {

}