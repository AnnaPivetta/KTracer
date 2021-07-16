import org.junit.Test

import org.junit.Assert.*
import java.io.*

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
}