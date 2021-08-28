enum class KeywordEnum() {
    NEW,
    MATERIAL,
    SHAPE,
    PLANE,
    SPHERE,
    BOX,
    CYLINDER,
    CSGUNION,
    CSGDIFFERENCE,
    CSGINTERSECTION,
    DIFFUSE,
    SPECULAR,
    UNIFORM,
    CHECKERED,
    IMAGE,
    MARBLE,
    WOOD,
    LAVA,
    IDENTITY,
    TRANSLATION,
    ROTATION_X,
    ROTATION_Y,
    ROTATION_Z,
    SCALING,
    CAMERA,
    ORTHOGONAL,
    PERSPECTIVE,
    FLOAT
}
val KEYWORDS = mapOf(
                "new" to KeywordEnum.NEW, "New" to KeywordEnum.NEW,
                "material" to KeywordEnum.MATERIAL, "Material" to KeywordEnum.MATERIAL,
                "shape" to KeywordEnum.SHAPE, "Shape" to KeywordEnum.SHAPE,
                "plane" to KeywordEnum.PLANE, "Plane" to KeywordEnum.PLANE,
                "sphere" to KeywordEnum.SPHERE, "Sphere" to KeywordEnum.SPHERE,
                "diffuse" to KeywordEnum.DIFFUSE, "Diffuse" to KeywordEnum.DIFFUSE,
                "specular" to KeywordEnum.SPECULAR, "Specular" to KeywordEnum.SPECULAR,
                "uniform" to KeywordEnum.UNIFORM, "Uniform" to KeywordEnum.UNIFORM,
                "checkered" to KeywordEnum.CHECKERED, "Checkered" to KeywordEnum.CHECKERED,
                "image" to KeywordEnum.IMAGE, "Image" to KeywordEnum.IMAGE,
                "marble" to KeywordEnum.MARBLE, "Marble" to KeywordEnum.MARBLE,
                "wood" to KeywordEnum.WOOD, "Wood" to KeywordEnum.WOOD,
                "lava" to KeywordEnum.LAVA, "Lava" to KeywordEnum.LAVA,
                "identity" to KeywordEnum.IDENTITY, "Identity" to KeywordEnum.IDENTITY,
                "translation" to KeywordEnum.TRANSLATION, "Translation" to KeywordEnum.TRANSLATION,
                "rotation_x" to KeywordEnum.ROTATION_X, "Rotation_x" to KeywordEnum.ROTATION_X, "rotationX" to KeywordEnum.ROTATION_X, "RotationX" to KeywordEnum.ROTATION_X,
                "rotation_y" to KeywordEnum.ROTATION_Y, "Rotation_y" to KeywordEnum.ROTATION_Y, "rotationY" to KeywordEnum.ROTATION_Y, "RotationY" to KeywordEnum.ROTATION_Y,
                "rotation_z" to KeywordEnum.ROTATION_Z, "Rotation_z" to KeywordEnum.ROTATION_Z, "rotationZ" to KeywordEnum.ROTATION_Z, "RotationZ" to KeywordEnum.ROTATION_Z,
                "scaling" to KeywordEnum.SCALING, "Scaling" to KeywordEnum.SCALING,
                "camera" to KeywordEnum.CAMERA, "Camera" to KeywordEnum.CAMERA,
                "orthogonal" to KeywordEnum.ORTHOGONAL, "Orthogonal" to KeywordEnum.ORTHOGONAL,
                "perspective" to KeywordEnum.PERSPECTIVE, "Perspective" to KeywordEnum.PERSPECTIVE,
                "float" to KeywordEnum.FLOAT, "Float" to KeywordEnum.FLOAT,
                "box" to KeywordEnum.BOX, "Box" to KeywordEnum.BOX,
                "cylinder" to KeywordEnum.CYLINDER, "Cylinder" to KeywordEnum.CYLINDER,
                "union" to KeywordEnum.CSGUNION, "Union" to KeywordEnum.CSGUNION,
                "difference" to KeywordEnum.CSGDIFFERENCE, "Difference" to KeywordEnum.CSGDIFFERENCE,
                "intersection" to KeywordEnum.CSGINTERSECTION, "intersection" to KeywordEnum.CSGINTERSECTION)