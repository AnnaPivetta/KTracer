import org.junit.Test

import org.junit.Assert.*
import java.io.*
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class InStreamTest {

    @Test
    fun inputTest() {

        val stream = InStream(InputStreamReader("abc   \nd\nef".byteInputStream()))

        assertTrue( stream.location.line == 1)
        assertTrue( stream.location.col == 1)

        assertTrue( stream.readChar() == 'a')
        assertTrue( stream.location.line == 1)
        assertTrue( stream.location.col == 2)

        stream.unreadChar('A')
        assertTrue( stream.location.line == 1)
        assertTrue( stream.location.col == 1)

        assertTrue( stream.readChar() == 'A')
        assertTrue( stream.location.line == 1)
        assertTrue( stream.location.col == 2)

        assertTrue( stream.readChar() == 'b')
        assertTrue( stream.location.line == 1)
        assertTrue( stream.location.col == 3)

        assertTrue( stream.readChar() == 'c')
        assertTrue( stream.location.line == 1)
        assertTrue( stream.location.col == 4)

        stream.skipWhiteAndComment()

        assertTrue( stream.readChar() == 'd')
        assertTrue( stream.location.line == 2)
        assertTrue( stream.location.col == 2)

        assertTrue( stream.readChar() == '\n')
        assertTrue( stream.location.line == 3)
        assertTrue( stream.location.col == 1)

        assertTrue( stream.readChar() == 'e')
        assertTrue( stream.location.line == 3)
        assertTrue( stream.location.col == 2)

        assertTrue( stream.readChar() == 'f')
        assertTrue( stream.location.line == 3)
        assertTrue( stream.location.col == 3)

        assertTrue( stream.readChar() == null)

    }

    @Test
    fun readToken() {
        val instructions = InputStreamReader("""
        # This is a comment
        # This is another comment
        new material sky_material(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0>
        ) # Comment at the end of the line
        """.byteInputStream())
        val stream = InStream(instructions)
        var token = stream.readToken()
        assertTrue(token is KeywordToken)
        assertTrue(token.toString() == KeywordEnum.NEW.toString())
        token = stream.readToken()
        assertTrue(token is KeywordToken)
        assertTrue(token.toString() == KeywordEnum.MATERIAL.toString())
        token = stream.readToken()
        assertTrue(token is IdentifierToken)
        assertTrue(token.toString() == "sky_material")
        token = stream.readToken()
        assertTrue(token is SymbolToken)
        assertTrue(token.toString() == "(")
        token = stream.readToken()
        assertTrue(token is KeywordToken)
        assertTrue(token.toString() == KeywordEnum.DIFFUSE.toString())
        token = stream.readToken()
        assertTrue(token is SymbolToken)
        assertTrue(token.toString() == "(")
        token = stream.readToken()
        assertTrue(token is KeywordToken)
        assertTrue(token.toString() == KeywordEnum.IMAGE.toString())
        token = stream.readToken()
        assertTrue(token is SymbolToken)
        assertTrue(token.toString() == "(")
        token = stream.readToken()
        assertTrue(token is StringToken)
        assertTrue(token.toString() == "my file.pfm")
        token = stream.readToken()
        assertTrue(token is SymbolToken)
        assertTrue(token.toString() == ")")
        token = stream.readToken()
        assertTrue(token is SymbolToken)
        assertTrue(token.toString() == ")")
        token = stream.readToken()
        assertTrue(token is SymbolToken)
        assertTrue(token.toString() == ",")
        token = stream.readToken()
        assertTrue(token is SymbolToken)
        assertTrue(token.toString() == "<")
        for (i in 1..8) { token = stream.readToken() }
        assertTrue(token is StopToken)
    }

    @Test
    fun testParser() {
        val content = InputStreamReader("""
        float clock(150)
    
        material sky_material(
            diffuse(uniform(<0, 0, 0>)),
            uniform(<0.7, 0.5, 1>)
        )
    
        # Here is a comment
    
        material ground_material(
            diffuse(checkered(<0.3, 0.5, 0.1>,
                              <0.1, 0.2, 0.5>, 4)),
            uniform(<0, 0, 0>)
        )
    
        material sphere_material(
            specular(uniform(<0.5, 0.5, 0.5>)),
            uniform(<0, 0, 0>)
        )
    
        plane (sky_material, translation([0, 0, 100]) * rotation_y(clock))
        plane (ground_material, identity)
    
        sphere(sphere_material, translation([0, 0, 1]))
    
        camera(perspective, rotation_z(30) * translation([-4, 0, 1]), 1.0, 2.0)
        """.byteInputStream())
        val stream = InStream(content)
        val map : MutableMap<String, Float> = mutableMapOf<String,Float>()
        val scene = stream.parseScene(map)
        assertTrue(scene.floatVariables.size == 1)
        assertTrue("clock" in scene.floatVariables.keys)
        assertTrue(scene.floatVariables["clock"]!! == 150.0F)
        assertTrue(scene.materials.size == 3)
        assertTrue("sky_material" in scene.materials)
        assertTrue("ground_material" in scene.materials)
        val sphere_material = scene.materials["sphere_material"]
        val sky_material = scene.materials["sky_material"]
        val ground_material = scene.materials["ground_material"]

        assertTrue(sky_material!!.brdf is DiffuseBRDF)
        assertTrue(sky_material.brdf.p is UniformPigment)
        assertTrue(sky_material.brdf.p.getColor(vec = Vector2d()).isClose(Color(0.0F, 0.0F, 0.0F)))
        assertTrue(ground_material!!.brdf is DiffuseBRDF )
        assertTrue(ground_material.brdf.p is CheckeredPigment)
        assertTrue(ground_material.brdf.p.getColor(Vector2d(2.0F, 2.0F)).isClose(Color(0.3F, 0.5F, 0.1F)))
        assertTrue(ground_material.brdf.p.getColor(Vector2d(2.0F, 0.9F)).isClose(Color(0.1F, 0.2F, 0.5F)))

        assertTrue(sphere_material!!.brdf is SpecularBRDF)
        assertTrue(sphere_material!!.brdf.p is UniformPigment)
        assertTrue(sphere_material.brdf.p.getColor(Vector2d(0.0F, 0.0F)).isClose(Color(0.5F, 0.5F, 0.5F)))

        assertTrue(sky_material.emittedRad is UniformPigment)
        assertTrue(sky_material.emittedRad.getColor(Vector2d(0.0F, 0.0F)).isClose(Color(0.7F, 0.5F, 1.0F)))
        assertTrue(ground_material.emittedRad is UniformPigment)
        assertTrue(ground_material.emittedRad.getColor(Vector2d(0.0F, 0.0F)).isClose(Color(0.0F, 0.0F, 0.0F)))
        assertTrue(sphere_material.emittedRad is UniformPigment)
        assertTrue(sphere_material.emittedRad.getColor(Vector2d(0.0F, 0.0F)).isClose(Color(0.0F, 0.0F, 0.0F)))

        assertTrue(scene.world.shapes.size == 3)
        assertTrue(scene.world.shapes[0] is Plane)
        assertTrue(scene.world.shapes[0].T.isClose(Transformation().translation(Vector(0.0F, 0.0F, 100.0F))
                                                        *Transformation().rotationY(150.0F)))
        assertTrue(scene.world.shapes[1] is Plane)
        assertTrue(scene.world.shapes[1].T.isClose(Transformation()))
        assertTrue(scene.world.shapes[2] is Sphere)
        assertTrue(scene.world.shapes[2].T.isClose(Transformation().translation(Vector(0.0F, 0.0F, 1.0F))))

        assertTrue(scene.camera is PerspectiveCamera)
    }

    @Test
    fun parser_undefMaterial() {
        val content = InputStreamReader("""
        plane(this_material_does_not_exist, identity)
        """.byteInputStream())
        val stream = InStream(content)
        val map : MutableMap<String, Float> = mutableMapOf<String,Float>()

        assertFailsWith<GrammarError> { val scene = stream.parseScene(map) }

    }

    @Test
    fun parser_doubleCamera() {
        val content = InputStreamReader("""
        camera(perspective, rotation_z(30) * translation([-4, 0, 1]), 1.0, 1.0)
        camera(orthogonal, identity, 1.0, 1.0)
        """.byteInputStream())
        val stream = InStream(content)
        val map : MutableMap<String, Float> = mutableMapOf<String,Float>()

        assertFailsWith<GrammarError> { val scene = stream.parseScene(map) }
    }
}