import kotlin.math.abs

class KDTree(
    val nodes: MutableList<KDNode> = mutableListOf<KDNode>(),
    val minBound: Point,
    val maxBound: Point,
    val vertices: MutableList<Point>
) {

    fun buildTree(iN: Int, triangles: MutableList<Triple<Int, Int, Int>>, min: Point, max: Point) {
        //stop condition
        if (triangles.size < 11) {
            val node = KDNode(triangles, null, null, min, max)
            if (iN > nodes.size -1 ) {
                for (j in nodes.size until iN) {
                    nodes.add(j, KDNode())
                }
                nodes.add(iN, node)
            }
            else nodes.set(iN, node)
            return
        }
        //recursion
        val node = KDNode(triangles, 2 * iN + 1, 2 * iN + 2, min, max)
        if (iN > nodes.size -1 ) {
            for (j in nodes.size until iN) {
                nodes.add(j, KDNode())
            }
            nodes.add(iN, node)
        }
        else nodes.set(iN, node)

        val Xwidth = abs(nodes[iN].maxBound.x - nodes[iN].minBound.x)
        val Ywidth = abs(nodes[iN].maxBound.y - nodes[iN].minBound.y)
        val Zwidth = abs(nodes[iN].maxBound.z - nodes[iN].minBound.z)

        var w = 0F
        var maxD = -1
        for (i in 0 until 3) {
            val wi = abs(nodes[iN].maxBound[i] - nodes[iN].minBound[i])
            if (wi > w) {
                maxD = i
                w = wi
            }
        }
        val split = max[maxD] - w * 0.5F
        //left
        var newMax = max
        newMax[maxD] = split
        //right
        var newMin = min
        newMin[maxD] = split

        val leftTriangles: MutableList<Triple<Int, Int, Int>> = mutableListOf()
        val rightTriangles: MutableList<Triple<Int, Int, Int>> = mutableListOf()

        for (t in triangles) {
            for (i in 0 until 3) { // i runs over the three vertices of the t triangle
                var vIndex = t.toList()[i]
                if (vertices[vIndex].x in min.x..newMax.x &&
                    vertices[vIndex].y in min.y..newMax.y &&
                    vertices[vIndex].z in min.z..newMax.z
                ) {
                    leftTriangles.add(t)
                }
            }
        }
        for (t in triangles) {
            for (i in 0 until 3) { // i runs over the three vertices of the t triangle
                var vIndex = t.toList()[i]
                if (vertices[vIndex].x in newMin.x..max.x &&
                    vertices[vIndex].y in newMin.y..max.y &&
                    vertices[vIndex].z in newMin.z..max.z
                ) {
                    rightTriangles.add(t)
                }
            }
        }
        buildTree(2 * iN + 1, leftTriangles, min, newMax)
        buildTree(2 * iN + 2, rightTriangles, newMin, max)
    }
}


class KDNode(
    val triangles: List<Triple<Int, Int, Int>> = listOf(),
    val left: Int? = 0, // identifies the left child in the List<KDNode>
    val right: Int? = 0,
    val minBound: Point = Point(),
    val maxBound: Point = Point()) {
}


