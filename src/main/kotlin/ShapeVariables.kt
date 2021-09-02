import kotlin.math.sqrt

val chair = Material (DiffuseBRDF(UniformPigment(DARKRED.copy())))
val brown = Material (DiffuseBRDF(UniformPigment(BROWN.copy())))

val sit = CSGDifference (
    Box(
        min = Point(-1F, -1F, 0.3F),
        max = Point(0.2F, 0.5F, 1.8F),
        material = chair
    ),
    Box(
        min = Point(-2.0F, -0.9F, 0.55F),
        max = Point(0F, 0.4F, 2.0F),
        material = chair
    )
        )
val arm = CSGDifference (
    sit,
    Box(
        min= Point(-2F, -2F, 1F),
        max= Point(-0.0001F, 2F, 2.0F),
        material = chair
    )
        )

val leg = Box (
    min = Point(-1F,-1F,0F),
    max = Point(-0.96F, -0.96F,0.3F),
    material = brown
        )

val arm1 = CSGUnion(
    arm,
    leg
)

val arm2 = CSGUnion(
    arm1,
    Transformation().translation(Vector(0F,1.46F,0F)) * leg
)

val arm3 = CSGUnion(
    arm2,
    Transformation().translation(Vector(1.16F,0F,0F)) * leg
)

val KTArmchair = CSGUnion(
    arm3,
    Transformation().translation(Vector(1.16F,1.46F,0F)) * leg
)


// KingChess

val T = Transformation()

val texture = Material (
    brdf = DiffuseBRDF( MarblePigment() ),
    emittedRad = UniformPigment(BLACK.copy())
)

val base = Cylinder (
    T = T.scaling(Vector(1.15f, 1.15f, 0.2f)) * T.translation(0.5f * VECZ),
    material = texture
)

val base2 = CSGUnion (
    base,
    T.scaling(Vector(0.85f, 0.85f, 1.5f)) * T.translation(0.2f * VECZ) * base
)

val body = CSGUnion (
    base2,
    Hyperboloid(
        T = T.translation( 2f* VECZ) * T.scaling(Vector(0.55f, 0.55f, 3f)),
        material = texture
    )
)

val body2 = CSGUnion (
    body,
    Cylinder(
        T = T.translation(3.55f * VECZ) * T.scaling(Vector(0.8f, 0.8f, 0.1f)),
        material = texture
    )
)

val up  = CSGIntersection(
    Sphere(T = T.scaling(Vector(3f, 3f, 3f)), material = texture),
    Cylinder(
        T = T.translation(2.9f*VECZ) * T.scaling(Vector(0.5f, 0.5f, 0.2f)),
        material = texture
    )
)

val upperBody = CSGUnion(
    Hyperboloid(
        minZ = 0f,
        maxZ= 1f,
        material = texture
    ),
    T.translation(-1.9f*VECZ) * T.scaling(Vector(2f* sqrt(2f), 2f* sqrt(2f), 1f)) * up

)

val noTop = CSGUnion(
    body2,
    T.translation(3.6f*VECZ) * T.scaling(Vector(0.7f, 0.7f, 1f)) * upperBody
)




val baseBox = Box(
    min= Point(-0.05f,0f , 0f),
    max= Point(0.05f,1f , 1f),
    material = texture
)
val cutBox = Box(
    min= Point(-1f,-3f , 0f),
    max= Point(1f,3f , 1f),
    material = texture
)
val baseCrossUnit = CSGDifference(
    baseBox,
    T.rotationX(63f) * cutBox
)

val crossUnit = CSGDifference (
    baseCrossUnit,
    T.translation(VECY) * T.rotationX(-63f) * cutBox,
    T = T.translation(-0.5f* VECY - 0.5f * VECZ)
)


val biCross = CSGUnion(
    crossUnit,
    T.translation(0.5f * VECZ) * T.rotationX(180f) * crossUnit
)

val cross = CSGUnion(
    T.scaling(Vector(0.95f, 1f, 1f)) * biCross ,
    T.translation(0.25f*VECZ + 0.25f*VECY) * T.rotationX(90f) * biCross
)


val top = CSGUnion(
    Sphere(T = T.scaling(Vector(0.3f, 0.3f, 0.3f)), material = texture),
    T.translation(0.7f * VECZ) * cross
)

val KTKing = CSGUnion(
    noTop,
    T.translation(4.8f*VECZ) * T.scaling(Vector(1f, 0.6f, 0.6f)) * top
)