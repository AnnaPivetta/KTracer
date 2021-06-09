import org.junit.Assert.*
import org.junit.Test

class TokenTest{
    @Test
    fun keyword(){
        /*var key : KeywordEnum = KeywordEnum.FLOAT
        val t = KeywordToken(keyword = key)
        //assertTrue(t.keyword==key)
        assertTrue(t is KeywordToken)*/
        val token = KeywordToken(keyword = KeywordEnum.FLOAT)
        //val keyword : KeywordEnum
        assertTrue(token is KeywordToken)
        //assertTrue(true)
    }

    @Test
    fun identifier(){

    }

    @Test
    fun symbol(){

    }

    @Test
    fun number(){

    }

    @Test
    fun string(){

    }










}