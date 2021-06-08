import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int

import java.io.FileInputStream

class KTracer : CliktCommand() {
    override fun run() = Unit
}

class Demo : CliktCommand(name = "demo") {
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
    override fun run() {

        //Set the World
        val world = World()
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
                T = Transformation().translation(0.5F * VECZ - 2.0F * VECY) *
                        Transformation().scaling((Vector(3.0F, 1.0F, 1.0F))),
                material = Material(
                    SpecularBRDF(UniformPigment(SILVER.copy()))
                )
            )
        )

        //a white sphere behind the camera
        world.add(
            Sphere(
                T = Transformation().scaling(Vector(0.4F, 0.4F, 0.4F)) *
                        Transformation().translation(-VECX * 12.0F + VECZ * 6.0F),
                material = Material(
                    DiffuseBRDF(UniformPigment(WHITE.copy())), UniformPigment(WHITE.copy())
                )
            )

        )

        val funkyCube = CSGDifference(
            Box(
                T = Transformation().translation(VECY + 0.5F * VECZ),
                material = Material(DiffuseBRDF(UniformPigment(TOMATO)))
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

        val ar = width.toFloat() / height.toFloat()
        val obsPos = Transformation().translation(-2.0F * VECX + VECZ)
        val camera = if (orthogonal) OrthogonalCamera(AR = ar, T = obsPos)
        else PerspectiveCamera(AR = ar, T = obsPos)
        val im = HdrImage(width, height)
        val computeColor = when (algorithm) {
            "onoff" -> OnOffRenderer(world).computeRadiance()
            "flat" -> FlatRenderer(world).computeRadiance()
            "pt" -> PathTracer(
                world = world,
                nRays = nR,
                maxDepth = maxDepth,
                rrTrigger = rrTrigger
            ).computeRadiance()
            else -> throw RuntimeException()
        }
        ImageTracer(im, camera).fireAllRays(computeColor, AAgrid)

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

class Render : CliktCommand(name = "KTracer") {
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
        val computeColor = when (algorithm) {
            "onoff" -> OnOffRenderer(world).computeRadiance()
            "flat" -> FlatRenderer(world).computeRadiance()
            "pt" -> PathTracer(
                world = world,
                nRays = nR,
                maxDepth = maxDepth,
                rrTrigger = rrTrigger
            ).computeRadiance()
            else -> throw RuntimeException()
        }
        ImageTracer(im, camera).fireAllRays(computeColor, AAgrid)

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

class Conversion : CliktCommand(name = "pfm2ldr") {
    private val inputPFMFileName by option("--input", "-i")
    private val factor by option().float().default(0.2F)
    private val luminosity by option(help = "The required average luminosity").float()
    private val gamma by option().float().default(1.0F)
    private val format by option().choice("BMP", "bmp", "jpeg", "wbmp", "png", "JPG", "PNG", "jpg", "WBMP", "JPEG")
    private val outputFileName by option("--output", "-o")

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

fun main(args: Array<String>) = KTracer().subcommands(Demo(), Conversion(), Render()).main(args)


