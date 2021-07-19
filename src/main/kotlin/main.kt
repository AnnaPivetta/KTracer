import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long

import java.io.FileInputStream
import java.io.FileReader
import kotlin.math.PI

class KTracer : CliktCommand() {
    override fun run() = Unit
}

class Demo : CliktCommand(name = "demo") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    @kotlin.ExperimentalUnsignedTypes
    override fun run() {
        val width = 480
        val height = 480
        val AAgrid = 1
        val pfmoutput = "demo.pfm"
        val ldroutput = "demo.png"
        val factor = 0.2F
        val luminosity = 0.1F
        val gamma = 1.0F
        val format="png"
        val initState = 42UL
        val initSeq = 54UL

        //Set the World
        val world = World()
        //Set a transformation for future use
        val T = Transformation()
        world.add(Plane(material=Material(DiffuseBRDF(p=UniformPigment(GREEN.copy())))))
        val sphereR = 50.0F
        world.add(Sphere(
                T = Transformation().scaling(Vector(sphereR, sphereR, sphereR)),
                material = Material(
                    DiffuseBRDF(UniformPigment(SKYBLUE.copy())),
                    UniformPigment(SKYBLUE.copy())
                )
            )
        )
        world.add(Sphere(T=Transformation().translation(Vector(5.0F, -3.0F, 8.0F)),
                        material = Material(DiffuseBRDF(p=UniformPigment(color= GOLD.copy())))))
        val firstCross = CSGUnion(
            Cylinder(
                T = T.translation(2F * VECZ + VECY * 1.3F) *
                        T.rotationY(180.0F * 0.5F) *
                        T.scaling(Vector(0.3F, 0.3F, 2.0F)),
                material = Material(
                    brdf = DiffuseBRDF(UniformPigment(OLIVE.copy())),
                    emittedRad = UniformPigment(BLACK.copy())
                )
            ),
            Cylinder(
                T = T.translation(2F * VECZ + VECY * 1.3F) *
                        T.scaling(Vector(0.3F, 0.3F, 2.0F)),
                material = Material(
                    brdf = DiffuseBRDF(UniformPigment(OLIVE.copy())),
                    emittedRad = UniformPigment(BLACK.copy())
                )
            )

        )


        val tripleCross = CSGUnion(
            firstCross,
            Cylinder(
                T = T.translation(2F * VECZ + VECY * 1.3F) *
                        T.rotationX(180.0F * 0.5F) *
                        T.scaling(Vector(0.3F, 0.3F, 2.0F)),
                material = Material(
                    brdf = DiffuseBRDF(UniformPigment(OLIVE.copy())),
                    emittedRad = UniformPigment(BLACK.copy())
                )
            )
        )

        val cubeS = CSGIntersection(
            Box(
                T = T.translation(2F * VECZ + VECY * 1.3F),
                material = Material(
                    brdf = DiffuseBRDF(UniformPigment(DARKRED.copy())),
                    emittedRad = UniformPigment(BLACK.copy())
                )
            ),
            Sphere(
                T = T.translation(2F * VECZ + VECY * 1.3F) *
                    T.scaling(Vector(0.65F,0.65F,0.65F)),
                material = Material(
                    brdf = DiffuseBRDF(UniformPigment(NAVY.copy())),
                    emittedRad = UniformPigment(BLACK.copy())
                )
            )
        )

        world.add(
         CSGDifference(
                cubeS,
            tripleCross
            )
        )

        val cameraT = T.translation(-2.0F * VECX + 3.0F * VECZ)
        val camera = PerspectiveCamera(AR = 1.0F, T = cameraT)
        val im = HdrImage(width, height)
        val pcg = PCG(initState, initSeq)
        val computeColor = FlatRenderer(world).computeRadiance()
        ImageTracer(im, camera).fireAllRays(computeColor)

        //Save HDR Image
        im.saveHDRImg(pfmoutput)
        echo("PFM Image has been saved to ${System.getProperty("user.dir")}/${pfmoutput}")

        //Tone Mapping
        echo("Applying tone mapping...")
        im.normalizeImg(factor = factor, luminosity = luminosity)
        im.clampImg()
        im.saveLDRImg(ldroutput, format, gamma)
        echo("LDR Image has been saved to ${System.getProperty("user.dir")}/${ldroutput}")
    }
}

/*class Render : CliktCommand(name = "KTracer") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private val width by option(
        "--width", "-w",
        help = "Image width"
    ).int().default(480)
    private val height by option(
        "--height", "-h",
        help = "Image height"
    ).int().default(480)
    private val orthogonal by option(help = "Use orthogonal camera projection").flag(default = false)
    private val algorithm by option(
        "--algorithm", "-a",
        help = "Renderer algorithm (pt is for Path Tracer"
    ).choice("onoff", "flat", "pt").default("pt")
    private val pfmoutput by option(
        "--pfm-o", "--hdr-o", "--pfmoutput",
        help = "File name for pfm output"
    ).default("KTracerDemo.pfm")
    private val ldroutput by option(
        "--ldr-o", "--ldroutput",
        help = "File name for ldr output"
    ).default("KTracerDemo.png")
    private val factor by option(help = "Tone mapping factor").float().default(0.2F)
    private val luminosity by option(help = "The required average luminosity").float()
    private val gamma by option(help = "Gamma correction value").float().default(1.0F)
    private val nR by option(
        "--nr", "-n",
        help = "Number of rays for evaluating integral"
    ).int().default(10)
    private val maxDepth by option(
        "--maxDepth", "-Md",
        help = "Max number of reflections per ray"
    ).int().default(3)
    private val rrTrigger by option(
        "--rrTrigger", "-rr",
        help = "Depth value after which Russian Roulette is activated"
    ).int().default(2)
    private val format by option(help = "Image format value - MUST correspond to ldr output name").choice(
        "BMP", "bmp", "jpeg", "wbmp",
        "png", "JPG", "PNG", "jpg", "WBMP", "JPEG"
    ).default("png")
    private val AAgrid by option("--AAgrid", "--AA", "--aa", "-A").int()
    @kotlin.ExperimentalUnsignedTypes
    private val initState by option("--initState").convert { it.toULong() }.default(42UL)
    @kotlin.ExperimentalUnsignedTypes
    private val initSeq by option("--initSeq").convert { it.toULong() }.default(54UL)

    @kotlin.ExperimentalUnsignedTypes
    override fun run() {

        //Set the World
        val world = World()
        //Define a Transformation to generate all transformations
        val T = Transformation()
        //A plane for the floor

        world.add(
            Plane(
                //T = Transformation().scaling(Vector()),
                material = Material(
                    DiffuseBRDF(),
                    CheckeredPigment(numOfSteps = 16)
                )
            )
        )

        //A big sphere for the sky
        val sphereR = 100.0F
        world.add(
            Sphere(
                T = Transformation().scaling(Vector(sphereR, sphereR, sphereR)),
                material = Material(
                    DiffuseBRDF(UniformPigment(BLACK.copy())),
                    UniformPigment(SKYBLUE.copy())
                )
            )
        )

        //A mirrored grey sphere
        world.add(
            Sphere(
                T = Transformation().translation(0.5F * VECZ - 2.0F * VECY + 4.0F * VECX) *
                        Transformation().scaling((Vector(3.0F, 1.0F, 1.0F))),
                material = Material(
                    SpecularBRDF(UniformPigment(SILVER.copy()))
                )
            )
        )

        //a white sphere behind the camera
        world.add(
            Sphere(
                T = Transformation().translation(-VECX * 12.0F + VECZ * 6.0F) *
                        Transformation().scaling(Vector(0.4F, 0.4F, 0.4F)),
                material = Material(
                    DiffuseBRDF(UniformPigment(WHITE.copy())), UniformPigment(WHITE.copy())
                )
            )

        )

/*
//Translation applied to single shapes

        val funkyCube = CSGDifference(
            Box(
                T = Transformation().translation(VECY + 0.5F * VECZ),
                material = Material(DiffuseBRDF(UniformPigment(DARKORANGE.copy())))
            ),
            Sphere(
                T = Transformation().translation(0.5F * (VECY + VECZ)) *
                        Transformation().scaling(Vector(0.3F, 0.3F, 0.3F)),
                material = Material(
                    DiffuseBRDF(UniformPigment(DARKCYAN.copy()))
                )
            )
        )


        world.add(
            CSGDifference(
                funkyCube,
                Sphere(
                    T = Transformation().translation(-VECX * 0.5F + VECY * 1.5F + VECZ * 1.0F) *
                            Transformation().scaling(Vector(0.2F, 0.2F, 0.2F)),
                    material = Material(
                        DiffuseBRDF(UniformPigment(OLIVE.copy()))
                    )
                )
            )
        )
*/






//Translation applied to CSG


        val funkyCube = CSGDifference(
            Box(
                material = Material(DiffuseBRDF(UniformPigment(DARKORANGE.copy())))
            ),
            Sphere(
                T = Transformation().translation(-0.5F * VECY ) *
                        Transformation().scaling(Vector(0.3F, 0.3F, 0.3F)),
                material = Material(
                    DiffuseBRDF(UniformPigment(DARKCYAN.copy()))
                )
            )
        )


        world.add(
            CSGDifference(
                funkyCube,
                Sphere(
                    T = Transformation().translation(-VECX * 0.5F + VECY * 0.5F + VECZ * 0.5F) *
                            Transformation().scaling(Vector(0.2F, 0.2F, 0.2F)),
                    material = Material(
                        DiffuseBRDF(UniformPigment(OLIVE.copy()))
                    )
                ),
                T= T.translation(VECY + 0.5F*VECZ)
            )
        )





        world.add(
            Sphere(
                T = T.translation(2.0F * VECZ + 2.5F * VECY + VECX) *
                        T.scaling(Vector(0.4F, 0.4F, 0.4F)),
                material = Material(
                    brdf = SpecularBRDF(UniformPigment(GOLD.copy())),
                    emittedRad = UniformPigment(GOLD.copy())
                )
            )
        )



        val worldMap = HdrImage()
        worldMap.readImg("src/main/src/map.pfm")
        world.add(
            Sphere(
                T = T.translation(VECZ * 2.5F) *
                        T.scaling(Vector(0.7F, 0.7F, 0.7F)),
                material = Material(
                    brdf = DiffuseBRDF(ImagePigment(worldMap)),
                    emittedRad = UniformPigment(BLACK.copy())
                )
            )
        )


        val ar = width.toFloat() / height.toFloat()
        val obsPos = Transformation().translation(-2.0F * VECX + VECZ)
        val camera = if (orthogonal) OrthogonalCamera(AR = ar, T = obsPos)
        else PerspectiveCamera(AR = ar, T = obsPos)
        val im = HdrImage(width, height)
        val pcg = PCG(initState, initSeq)
        val computeColor = when (algorithm) {
            "onoff" -> OnOffRenderer(world).computeRadiance()
            "flat" -> FlatRenderer(world).computeRadiance()
            "pt" -> PathTracer(
                world = world,
                nRays = nR,
                pcg = pcg,
                maxDepth = maxDepth,
                rrTrigger = rrTrigger
            ).computeRadiance()
            else -> throw RuntimeException()
        }
        ImageTracer(im, camera).fireAllRays(computeColor, AAgrid, pcg)

        //Save HDR Image
        im.saveHDRImg(pfmoutput)
        echo("PFM Image has been saved to ${System.getProperty("user.dir")}/${pfmoutput}")

        //Tone Mapping
        echo("Applying tone mapping...")
        im.normalizeImg(factor = factor, luminosity = luminosity)
        im.clampImg()
        im.saveLDRImg(ldroutput, format, gamma)
        echo("LDR Image has been saved to ${System.getProperty("user.dir")}/${ldroutput}")
    }
}*/

class Conversion : CliktCommand(name = "pfm2ldr") {
    private val inputPFMFileName by option("--input", "-i", help = "required: the pfm file to convert").required()
    private val factor by option("--factor", help="optional: a factor needed for the conversion.").float().default(0.2F)
    private val luminosity by option("--luminosity", help = "optional: he required average luminosity").float().default(0.1F)
    private val gamma by option("--gamma", help = "optional: the gamma factor that characterizes the answer of the monitor." +
            "If you know the gamma factor of your monitor you can specify it, otherwise the default value is 1").float().default(1.0F)
    private val format by option("--format", help= "required: the format of the output file").choice("BMP", "bmp", "jpeg", "wbmp", "png", "JPG", "PNG", "jpg", "WBMP", "JPEG").required()
    private val outputFileName by option("--output", "-o", help= "required: the name of the output file").required()

    override fun run() {
        val img = HdrImage()
        echo("Reading file...")
        FileInputStream(inputPFMFileName.toString()).use { INStream ->
            img.readPfmFile(INStream)
        }
        echo("File successfully read")
        echo("Normalizing pixels luminosity...")
        img.normalizeImg(factor = factor, luminosity = luminosity)
        img.clampImg()
        echo("Image normalized")
        echo("Writing image on disk...")

        img.saveLDRImg(outputFileName.toString(), format.toString(), gamma)
        println("Done! Your image has been saved to ${System.getProperty("user.dir")}/${outputFileName}")
    }
}

class Render : CliktCommand(name = "render") {
    private val width by option(
        "--width", "-w",
        help = "Image width"
    ).int().default(480)
    private val height by option(
        "--height", "-h",
        help = "Image height"
    ).int().default(480)
    private val angleDeg by option(
        "--angle-deg",
        help = "Angle of camera rotation (CCW) with respect to z axis in DEG"
    ).float().default(0.0F)
    private val algorithm by option(
        "--algorithm", "-a",
        help = "Renderer algorithm (pt is for Path Tracer)"
    ).choice("onoff", "flat", "pt").default("pt")
    private val pfmoutput by option(
        "--pfm-o", "--hdr-o", "--pfmoutput",
        help = "File name for pfm output"
    ).default("renderedimage.pfm")
    private val ldroutput by option(
        "--ldr-o", "--ldroutput",
        help = "File name for ldr output"
    ).default("renderedimage.png")
    private val factor by option("--factor", help = "Tone mapping factor").float().default(0.2F)
    private val luminosity by option("--luminosity", help = "The required average luminosity").float().default(0.1F)
    private val gamma by option("--gamma", help = "Gamma value").float().default(1.0F)
    private val nR by option(
        "--nr", "-n",
        help = "Number of rays for evaluating integral"
    ).int().default(10)
    private val maxDepth by option(
        "--maxDepth", "-Md",
        help = "Max number of reflections per ray"
    ).int().default(3)
    private val rrTrigger by option(
        "--rrTrigger", "-rr",
        help = "Depth value after which Russian Roulette is activated"
    ).int().default(2)
    private val format by option(help = "Image format value - MUST correspond to ldr output name").choice(
        "BMP", "bmp", "jpeg", "wbmp",
        "png", "JPG", "PNG", "jpg", "WBMP", "JPEG"
    ).default("png")
    private val AAgrid by option("--AAgrid", "--AA", "--aa", "-A", help = "number of divisions in each pixel's side to perform antialiasing").int()
    @kotlin.ExperimentalUnsignedTypes
    private val initState by option("--initState", help = "initial state of random number generator").convert { it.toULong() }.default(42UL)
    @kotlin.ExperimentalUnsignedTypes
    private val initSeq by option("--initSeq", help= "initial sequence of random number generator").convert { it.toULong() }.default(54UL)

    val filename by option(
        "--inputfile", "--file",
        help = "File describing the scene to render"
    ).required()
    val variables : Map<String, String> by option("--declare-float", "-D").associate()
    @kotlin.ExperimentalUnsignedTypes
    override fun run() {
        val map : MutableMap<String, Float> = mutableMapOf<String,Float>()
        for (i in variables.keys) {
            map[i]=variables[i]!!.toFloat()
        }
        val stream = InStream(stream = FileReader(filename), fileName = filename)
        val scene = stream.parseScene(map)

        val im = HdrImage(width, height)
        val pcg = PCG(initState, initSeq)

        //-----------------------

        val computeColor = when (algorithm) {
            "onoff" -> OnOffRenderer(scene.world).computeRadiance()
            "flat" -> FlatRenderer(scene.world).computeRadiance()
            "pt" -> PathTracer(
                world = scene.world,
                pcg = pcg,
                nRays = nR,
                maxDepth = maxDepth,
                rrTrigger = rrTrigger
            ).computeRadiance()
            else -> throw RuntimeException()
        }

        if (scene.camera == null) {
            print("no camera defined. A default camera will be used")}
        ImageTracer(
            im,
            scene.camera ?: PerspectiveCamera(T = Transformation().translation(-VECX + VECZ), dist=1.0F, AR=width/height.toFloat())
        ).fireAllRays(computeColor, AAgrid, pcg)

        //Save HDR Image
        im.saveHDRImg(pfmoutput)
        echo("PFM Image has been saved to ${System.getProperty("user.dir")}/${pfmoutput}")

        //Tone Mapping
        echo("Applying tone mapping...")
        im.normalizeImg(factor = factor, luminosity = luminosity)
        im.clampImg()
        im.saveLDRImg(ldroutput, format, gamma)
        echo("LDR Image has been saved to ${System.getProperty("user.dir")}/${ldroutput}")
    }
}

fun main(args: Array<String>) = KTracer().subcommands(Demo(), Conversion(), Render()).main(args)


