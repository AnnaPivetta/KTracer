import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int

import java.io.FileInputStream
import kotlin.math.PI

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
    private val angleDeg by option(
        "--angle-deg",
        help = "Angle of camera rotation (CCW) with respect to z axis in DEG"
    ).float().default(0.0F)
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
                    CheckeredPigment(numOfSteps = 20)
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

        world.add(
            CSGDifference(
                Box(
                    T = Transformation().translation(VECY + 0.5F * VECZ),
                    material = Material(DiffuseBRDF(UniformPigment(CRIMSON)))
                ),
                Sphere(
                    T = Transformation().translation(0.5F * (VECY + VECZ)) *
                            Transformation().scaling(Vector(0.3F, 0.3F, 0.3F)),
                    material = Material(
                        DiffuseBRDF(UniformPigment(DARKCYAN.copy()))
                    )
                )
            )
        )

        val ar = width.toFloat() / height.toFloat()
        val T = Transformation()
        val cameraT = T.rotationZ(angle = angleDeg * PI.toFloat()/360F) * T.translation(-VECX + VECZ)
        val camera = if (orthogonal) OrthogonalCamera(AR = ar, T = cameraT)
        else PerspectiveCamera(AR = ar, T = cameraT)
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
    private val gamma by option(help = "Gamma correction value").float().default(1.0F)
    private val nR by option(
        "--nr", "-n",
        help = "Number of rays for evaluating integral"
    ).int().default(100)
    private val maxDepth by option(
        "--maxDepth", "-Md",
        help = "Max number of reflections per ray"
    ).int().default(10)
    private val rrTrigger by option(
        "--rrTrigger", "-rr",
        help = "Depth value after which Russian Roulette is activated"
    ).int().default(6)
    private val format by option(help = "Image format value - MUST correspond to ldr output name").choice(
        "BMP", "bmp", "jpeg", "wbmp",
        "png", "JPG", "PNG", "jpg", "WBMP", "JPEG"
    ).default("png")

    @kotlin.ExperimentalUnsignedTypes
    override fun run() {


        //Set 10 Spheres in the World
        val world = World()
        val vertices = arrayOf(-0.5F, 0.5F)
        for (x in vertices) {
            for (y in vertices) {
                for (z in vertices) {
                    world.add(
                        Sphere(
                            T = Transformation().translation(Vector(x, y, z)) *
                                    Transformation().scaling(Vector(0.1F, 0.1F, 0.1F))
                        )
                    )
                }
            }
        }

        //Place two other balls in the bottom/left part of the cube, so
        //that we can check if there are issues with the orientation of
        //the image
        world.add(
            Sphere(
                T = Transformation().translation(Vector(0.0F, 0.0F, -0.5F))
                        * Transformation().scaling(Vector(0.1F, 0.1F, 0.1F))
            )
        )
        world.add(
            Sphere(
                T = Transformation().translation(Vector(0.0F, 0.5F, 0.0F))
                        * Transformation().scaling(Vector(0.1F, 0.1F, 0.1F))
            )
        )
        val ar = width.toFloat() / height.toFloat()
        val camera = if (orthogonal) OrthogonalCamera(AR = ar, T = Transformation().translation(-2.0F * VECX))
        else PerspectiveCamera(AR = ar, T = Transformation().translation(-2.0F * VECX))
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
        ImageTracer(im, camera).fireAllRays(computeColor)

        //Save HDR Image
        im.saveHDRImg(pfmoutput)
        echo("PFM Image has been saved to ${System.getProperty("user.dir")}/${pfmoutput}")

        //Tone Mapping
        echo("Applying tone mapping...")
        im.normalizeImg(factor = factor)
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


