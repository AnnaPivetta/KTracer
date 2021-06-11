import org.junit.Test

import org.junit.Assert.*
import java.io.*
import java.util.stream.Stream

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

}