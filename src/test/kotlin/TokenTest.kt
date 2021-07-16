import org.junit.Assert.*
import org.junit.Test

class TokenTest{
    @Test
    fun keyword(){
        val token = KeywordToken(keyword = KeywordEnum.FLOAT)
        assertTrue(token is KeywordToken)
        assertTrue(token.keyword.toString()=="FLOAT")
    }

    @Test
    fun identifier(){
        val token = IdentifierToken(identifier = "id")
        assertTrue(token is Token)
        assertTrue(token.identifier== "id")
    }

    @Test
    fun symbol(){
        val a : Char = '9'
        val token =SymbolToken(symbol = a)
        assertTrue(token is Token)
        assertTrue(token.symbol == '9')
    }

    @Test
    fun number(){
        val token = LiteralNumberToken(value = 3.0F)
        assertTrue(token is LiteralNumberToken)
        assertTrue(token.value == 3.0F)
    }

    @Test
    fun string(){
        val token =StringToken(string = "Hello, world")
        assertTrue(token is StringToken)
        assertTrue(token.string == "Hello, world")
    }










}