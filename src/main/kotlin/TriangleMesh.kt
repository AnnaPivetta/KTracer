class TriangleMesh (T : Transformation = Transformation(),
                    material: Material = Material(),
                    var nTriangles : Int,
                    var nVertices : Int,
                    var VerticesX : Array<Float>,
                    var VerticesY : Array<Float>,
                    var VerticesZ : Array<Float>,
                    var VIndices : Array<Triple<Int,Int,Int>>,
                    var Normals : Array<Normal>) : Shape(T, material) {

    fun constructBox(): Box {
            return Box(Point(
                VerticesX.indices.minOf { i: Int -> VerticesX[i] },
                VerticesY.indices.minOf { i: Int -> VerticesY[i] },
                VerticesZ.indices.minOf { i: Int -> VerticesZ[i] }
            ),
                Point(
                    VerticesX.indices.maxOf { i: Int -> VerticesX[i] },
                    VerticesY.indices.maxOf { i: Int -> VerticesY[i] },
                    VerticesZ.indices.maxOf { i: Int -> VerticesZ[i] }
                )
            )
    }

    override fun isPointInternal(p: Point): Boolean {
        TODO("Not yet implemented")
    }

    override fun rayIntersection(r: Ray): HitRecord? {
        TODO("Not yet implemented")
    }
    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        TODO("Not yet implemented")
    }


}