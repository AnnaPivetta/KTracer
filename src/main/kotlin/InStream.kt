import java.io.InputStreamReader
import kotlin.test.assertTrue


/**
 * Lexer and Parser
 *
 * This class is deputed to the reading and translating of input files for scenes that
 * have to be rendered
 *
 *
 */
class InStream(
    private val stream: InputStreamReader,
    fileName: String = "",
    private val tab: Int = 4
) {
    private val shape2Parser = mapOf<KeywordEnum, (Scene) -> Shape>(
        KeywordEnum.SPHERE to ::parseSphere,
        KeywordEnum.BOX to ::parseBox,
        KeywordEnum.CYLINDER to ::parseCylinder,
        KeywordEnum.CSGUNION to ::parseCSGUnion,
        KeywordEnum.CSGDIFFERENCE to ::parseCSGDifference,
        KeywordEnum.CSGINTERSECTION to ::parseCSGIntersection
    )

    var location = SourceLocation(fileName, 1, 1)
    private var savedChar: Char? = null
    private var savedLoc = location.copy()
    private var savedToken: Token? = null

    /**
     * Update `location` after having read `ch` from the stream
     */
    private fun updatePos(ch: Char?) {

        when (ch) {
            null -> return
            '\n' -> {
                location.line += 1
                location.col = 1
            }
            '\t' -> location.col += tab
            else -> location.col += 1
        }
    }

    /**
     * Read a new character from the stream
     */
    fun readChar(): Char? {
        val ch: Char?
        if (savedChar != null) {
            ch = savedChar
            savedChar = null
        } else {
            val r = stream.read()
            ch = if (r != -1) r.toChar() else null
        }

        savedLoc = location.copy()
        this.updatePos(ch)
        return ch
    }

    /**
     * Push a character back to the stream
     */

    fun unreadChar(ch: Char) {
        assertTrue(savedChar == null)
        savedChar = ch
        location = savedLoc.copy()
    }

    /**
     * Keep reading characters until a non-whitespace character is found
     * or a comment
     */
    fun skipWhiteAndComment() {
        var ch: Char? = this.readChar() ?: return
        while (ch in WHITESPACE || ch == '#') {
            // It's a comment! Keep reading until the end of the line (include the case "", the end-of-file)
            if (ch == '#') while (this.readChar() !in listOf(null, '\n', '\t')) continue

            ch = readChar()
            if (ch == null) return
        }
        //Put the non-whitespace character back
        this.unreadChar(ch!!)
    }

    private fun parseStringToken(tokenLoc: SourceLocation): StringToken {
        var token = ""
        var ch: Char?
        while (true) {
            ch = this.readChar()
            if (ch == '"') break

            if (ch == null) throw GrammarError(tokenLoc, "Unterminated string")

            token += ch
        }
        return StringToken(tokenLoc, token)
    }

    private fun parseFloatToken(firstChar: String, tokenLoc: SourceLocation): LiteralNumberToken {
        var token = firstChar
        var ch: Char?
        while (true) {
            ch = this.readChar()
            if (ch == null) break
            if (!(ch.isDigit() || ch == '.' || ch in listOf('e', 'E'))) {
                this.unreadChar(ch)
                break
            }
            token += ch
        }
        val value: Float
        try {
            value = token.toFloat()
        } catch (e: NumberFormatException) {
            throw GrammarError(tokenLoc, "$token is an invalid floating-point number")
        }
        return LiteralNumberToken(tokenLoc, value)

    }

    private fun parseKeywordOrIdentifierToken(firstChar: String, tokenLoc: SourceLocation): Token {
        var token = firstChar
        var ch: Char?

        while (true) {
            ch = this.readChar()
            if (ch == null) break
            //Digits are ok after the first character
            if (!(ch.isLetterOrDigit() || ch == '_')) {
                this.unreadChar(ch)
                break
            }

            token += ch
        }

        // If it is a keyword, it must be listed in the KEYWORDS dictionary
        // If we got KeyError, it is not a keyword and thus it must be an identifier
        return if (KEYWORDS.containsKey(token)) KeywordToken(tokenLoc, KEYWORDS[token]!!)
        else IdentifierToken(tokenLoc, token)
    }

    /**
     * Read a token from the stream
     * Raise :class:`.ParserError` if a lexical error is found."""
     */

    private fun readToken(): Token {
        if (savedToken != null) {
            val result: Token = savedToken!!
            savedToken = null
            return result
        }

        this.skipWhiteAndComment()
        // At this point we're sure that ch does *not* contain a whitespace character
        //Neither it's part of a comment
        val ch = this.readChar() ?: return StopToken(location = location)

        // At this point we must check what kind of token begins with the "ch" character (which has been
        // put back in the stream with unreadChar). First, we save the position in the stream
        val tokenLoc = location.copy()

        //One-character symbol, like '(' or ','
        return if (ch in SYMBOLS) SymbolToken(tokenLoc, ch)

        //A literal string (used for file names)
        else if (ch == '"') this.parseStringToken(tokenLoc)

        // A floating-point number
        else if (ch.isDigit() || ch in listOf('+', '-', '.')) this.parseFloatToken(ch.toString(), tokenLoc)

        //If it begins with an alphabetic character, it must either be a keyword or a identifier
        else if (ch.isLetter() || ch == '_') this.parseKeywordOrIdentifierToken(ch.toString(), tokenLoc)

        //We got some weird character, like '@` or `&`
        else throw GrammarError(location, "Invalid character $ch")
    }

    /**
     * Make as if `token` were never read from Input File
     */
    private fun unreadToken(token: Token) {
        assertTrue(savedToken == null)
        savedToken = token
    }

    private fun expectSymbol(symbol: Char) {
        val token = readToken()
        if (token !is SymbolToken || token.symbol != symbol) {
            throw GrammarError(token.location, "got $token instead of $symbol")
        }
    }

    private fun expectKeywords(keywords: List<KeywordEnum>): KeywordEnum {
        val token = readToken()
        if (token !is KeywordToken) {
            throw GrammarError(token.location, "Got $token instead of keyword")
        }
        if (token.keyword !in keywords) {
            throw GrammarError(
                token.location,
                "Expected one of the keywords ${KeywordEnum.values()} instead of $token"
            )
        }
        return token.keyword
    }

    private fun expectKeywordsOrIdentifier(keywords: List<KeywordEnum>): Any {
        return when (val token = readToken()) {
            is KeywordToken -> {
                if (token.keyword !in keywords) throw GrammarError(
                    token.location,
                    "Expected one of the keywords ${KeywordEnum.values()} instead of $token"
                )
                else token.keyword
            }
            is IdentifierToken -> token
            else -> throw GrammarError(token.location, "Got '$token' not listed in $keywords")
        }
    }

    /**
     * Read a token from `input file` and check that it is either a literal number or a variable in `scene`.
     *
     * @return the [Float] number read
     */
    private fun expectNumber(scene: Scene): Float {
        val token = this.readToken()
        val vName: String
        when (token) {
            is LiteralNumberToken -> return token.value
            is IdentifierToken -> vName = token.identifier
            else -> throw GrammarError(token.location, "Got '$token' instead of a number")
        }
        if (vName in scene.floatVariables.keys) return scene.floatVariables[vName]!!
        else throw GrammarError(token.location, "Unknown variable '$vName'")
    }

    /**
     * Read a token from `input file` and check that it is a literal string.
     *
     * @return the literal [String] read
     */
    private fun expectString(): String {
        val token = this.readToken()
        if (token !is StringToken) throw GrammarError(token.location, "Got '$token' instead of a string")
        else return token.string
    }

    /**
     * Read a token from `input file` and check that it is an identifier.
     *
     * @return A [String] that is the name of Identifier
     */
    private fun expectIdentifier(): String {
        val token = this.readToken()
        if (token !is IdentifierToken) throw GrammarError(token.location, "Got '$token' instead of an identifier")
        else return token.identifier
    }


    private fun parseVector(scene: Scene): Vector {
        expectSymbol('[')
        val x = expectNumber(scene)
        expectSymbol(',')
        val y = expectNumber(scene)
        expectSymbol(',')
        val z = expectNumber(scene)
        expectSymbol(']')
        return Vector(x, y, z)
    }

    private fun parsePoint(scene: Scene): Point {
        expectSymbol('(')
        val x = expectNumber(scene)
        expectSymbol(',')
        val y = expectNumber(scene)
        expectSymbol(',')
        val z = expectNumber(scene)
        expectSymbol(')')
        return Point(x, y, z)
    }

    private fun parseBRDF(scene: Scene): BRDF {
        val keyword = expectKeywords(listOf(KeywordEnum.DIFFUSE, KeywordEnum.SPECULAR))
        expectSymbol('(')
        val pigment = parsePigment(scene)
        expectSymbol(')')
        return when (keyword) {
            KeywordEnum.DIFFUSE -> DiffuseBRDF(p = pigment)
            KeywordEnum.SPECULAR -> SpecularBRDF(p = pigment)
            else -> throw (RuntimeException("This line should be unreachable"))
        }
    }

    private fun parseColor(scene: Scene): Color {
        expectSymbol('<')
        val red = expectNumber(scene)
        expectSymbol(',')
        val green = expectNumber(scene)
        expectSymbol(',')
        val blue = expectNumber(scene)
        expectSymbol('>')
        return Color(red, green, blue)
    }

    private fun parsePigment(scene: Scene): Pigment {
        val keyword = expectKeywords(listOf(KeywordEnum.UNIFORM, KeywordEnum.CHECKERED, KeywordEnum.IMAGE))
        expectSymbol('(')
        when (keyword) {
            KeywordEnum.UNIFORM -> return UniformPigment(color = parseColor(scene))
            KeywordEnum.CHECKERED -> {
                val c1 = parseColor(scene)
                expectSymbol(',')
                val c2 = parseColor(scene)
                expectSymbol(',')
                val nSteps = expectNumber(scene).toInt()
                return CheckeredPigment(color1 = c1, color2 = c2, numOfSteps = nSteps)
            }
            KeywordEnum.IMAGE -> {
                val fileName = expectString()
                val img = HdrImage()
                img.readImg(fileName)
                return ImagePigment(image = img)
            }
            else -> throw (RuntimeException("This line should be unreachable"))
        }
    }

    private fun parseMaterial(scene: Scene): Pair<String, Material> {
        val name = expectIdentifier()
        expectSymbol('(')
        val brdf = parseBRDF(scene)
        expectSymbol(',')
        val emittedRadiance = parsePigment(scene)
        expectSymbol(')')

        return Pair(name, Material(brdf = brdf, emittedRad = emittedRadiance))
    }

    private fun parseTransformation(scene: Scene): Transformation {
        var result = Transformation()

        while (true) {
            val tKW = expectKeywords(
                listOf(
                    KeywordEnum.IDENTITY,
                    KeywordEnum.TRANSLATION,
                    KeywordEnum.ROTATION_X,
                    KeywordEnum.ROTATION_Y,
                    KeywordEnum.ROTATION_Z,
                    KeywordEnum.SCALING,
                )
            )
            when (tKW) {
                KeywordEnum.IDENTITY -> {
                } // Do nothing
                KeywordEnum.TRANSLATION -> {
                    expectSymbol('(')
                    result *= result.translation(parseVector(scene))
                    expectSymbol(')')
                }
                KeywordEnum.ROTATION_X -> {
                    expectSymbol('(')
                    result *= result.rotationX(expectNumber(scene))
                    expectSymbol(')')
                }
                KeywordEnum.ROTATION_Y -> {
                    expectSymbol('(')
                    result *= result.rotationY(expectNumber(scene))
                    expectSymbol(')')
                }
                KeywordEnum.ROTATION_Z -> {
                    expectSymbol('(')
                    result *= result.rotationZ(expectNumber(scene))
                    expectSymbol(')')
                }
                KeywordEnum.SCALING -> {
                    expectSymbol('(')
                    result *= result.translation(parseVector(scene))
                    expectSymbol(')')
                }
                else -> throw (RuntimeException("This line should be unreachable"))
            }
            //We must peek the next token to check if there is another transformation that is being
            //chained or if the sequence ends.Thus, this is a LL(1) parser.
            val nextKW = readToken()
            if (nextKW !is SymbolToken || nextKW.symbol != '*') {
                //Pretend you never read this token and put it back !
                unreadToken(nextKW)
                break
            }
        }
        return result
    }

    private fun parseShape(scene: Scene): Shape {
        val s = expectKeywordsOrIdentifier(
            listOf(
                KeywordEnum.SPHERE,
                KeywordEnum.BOX,
                KeywordEnum.CYLINDER,
                KeywordEnum.CSGUNION,
                KeywordEnum.CSGDIFFERENCE,
                KeywordEnum.CSGINTERSECTION
            )
        )

        return when (s) {
            is KeywordEnum -> shape2Parser[s]!!(scene)
            is IdentifierToken -> {
                if (s.identifier !in scene.shapeVariables.keys) throw GrammarError(
                    s.location,
                    "Unknown variable '${s.identifier}'"
                )
                else scene.shapeVariables[s.identifier]!!
            }
            else -> throw (RuntimeException("This line should be unreachable"))
        }
    }

    private fun parseSphere(scene: Scene): Sphere {
        expectSymbol('(')
        val materialName = expectIdentifier()
        if (materialName !in scene.materials) {
            throw GrammarError(location, "unknown material $materialName")
        }
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')

        return Sphere(T = transformation, material = scene.materials[materialName]!!)
    }

    private fun parsePlane(scene: Scene): Plane {
        expectSymbol('(')
        val materialName = expectIdentifier()
        if (materialName !in scene.materials) {
            throw GrammarError(location, "unknown material $materialName")
        }
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')
        return Plane(T = transformation, material = scene.materials[materialName]!!)
    }

    private fun parseBox(scene: Scene): Box {
        expectSymbol('(')
        val min = parsePoint(scene)
        expectSymbol(',')
        val max = parsePoint(scene)
        expectSymbol(',')
        val materialName = expectIdentifier()
        if (materialName !in scene.materials) {
            throw GrammarError(location, "unknown material $materialName")
        }
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')

        return Box(min = min, max = max, T = transformation, material = scene.materials[materialName]!!)
    }

    private fun parseCylinder(scene: Scene): Cylinder {
        expectSymbol('(')
        val materialName = expectIdentifier()
        if (materialName !in scene.materials) {
            throw GrammarError(location, "unknown material $materialName")
        }
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')

        return Cylinder(T = transformation, material = scene.materials[materialName]!!)
    }

    private fun parseCSGUnion(scene: Scene): CSGUnion {
        expectSymbol('(')
        val s1 = parseShape(scene)
        expectSymbol(',')
        val s2 = parseShape(scene)
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')

        return CSGUnion(s1 = s1, s2 = s2, T = transformation)

    }

    private fun parseCSGDifference(scene: Scene): CSGDifference {
        expectSymbol('(')
        val s1 = parseShape(scene)
        expectSymbol(',')
        val s2 = parseShape(scene)
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')

        return CSGDifference(s1 = s1, s2 = s2, T = transformation)

    }

    private fun parseCSGIntersection(scene: Scene): CSGIntersection {
        expectSymbol('(')
        val s1 = parseShape(scene)
        expectSymbol(',')
        val s2 = parseShape(scene)
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')

        return CSGIntersection(s1 = s1, s2 = s2, T = transformation)

    }

}

