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
        max= Point(0.2F, 2F, 2.0F)
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

/*
#schienale
box((0,-1,0.5), (0.2, 0.5, 1.8), chair, rotation_z(15))
#bracciolo sx
box((-1, 0.4, 0.5), (0,0.5,1), chair, rotation_z(15))
#bracciolo dx
box((-1,-1,0.5), (0,-0.9,1), chair, rotation_z(15))
#seduta
box((-1, -1, 0.3), (0.2, 0.5, 0.55), chair, rotation_z(15))




 */