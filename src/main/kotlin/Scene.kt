data class Scene (
    val materials : Map<String, Material>,
    val world : World = World(),
    val camera : Camera? = null,
    val floatVariables: Map<String, Float>,
    val overriddenVariables: Set<String>
    )
