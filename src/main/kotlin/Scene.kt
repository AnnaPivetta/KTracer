data class Scene (
    var materials : MutableMap<String, Material> = mutableMapOf(),
    var world : World = World(),
    var camera : Camera? = null,
    var floatVariables: MutableMap<String, Float> = mutableMapOf(),
    val shapeVariables: MutableMap<String, Shape> = mutableMapOf(),
    var overriddenVariables: Set<String> = setOf()
    )
