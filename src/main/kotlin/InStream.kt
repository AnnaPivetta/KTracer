import java.io.*
import kotlin.test.assertTrue


/**
 * Lexer and Parser
 *
 * This class is deputed to the reading and translating of input files for scenes that
 * have to be rendered
 *
 */
class InStream(
    private var stream: InputStreamReader,
    fileName: String = "",
    private val tab: Int = 4
) {
    //Stream initialization
    init {
        if (fileName != "") stream = FileReader(fileName)
    }

    //Dictionary for mapping Shapes KW to its parser
    private val shape2Parser = mapOf<KeywordEnum, (Scene) -> Shape>(
        KeywordEnum.SPHERE to ::parseSphere,
        KeywordEnum.PLANE to ::parsePlane,
        KeywordEnum.BOX to ::parseBox,
        KeywordEnum.CYLINDER to ::parseCylinder,
        KeywordEnum.HYPERBOLOID to :: parseHyperboloid,
        KeywordEnum.CSGUNION to ::parseCSGUnion,
        KeywordEnum.CSGDIFFERENCE to ::parseCSGDifference,
        KeywordEnum.CSGINTERSECTION to ::parseCSGIntersection
    )

    private val name2color = mapOf<String, Color>(
        "white" to WHITE.copy(),
        "black" to BLACK.copy(),
        "navy" to NAVY.copy(),
        "skyblue" to SKYBLUE.copy(),
        "pink" to PINK.copy(),
        "darkred" to DARKRED.copy(),
        "olive" to OLIVE.copy(),
        "green" to GREEN.copy(),
        "darkcyan" to DARKCYAN.copy(),
        "brown" to BROWN.copy(),
        "silver" to SILVER.copy(),
        "crimson" to CRIMSON.copy(),
        "tomato" to TOMATO.copy(),
        "gold" to GOLD.copy(),
        "limegreen" to LIMEGREEN.copy(),
        "darkorange" to DARKORANGE.copy(),
        "purple" to PURPLE.copy(),
        "red" to RED.copy(),
        "grey" to GRAY.copy(), "gray" to GRAY.copy(),
        "darkgrey" to DARKGRAY.copy(), "darkgray" to DARKGRAY.copy(),
        "dimgrey" to DIMGRAY.copy(), "dimgray" to DIMGRAY.copy(),
        "darkbrown" to DARKBROWN.copy(),
        "saddlebrown" to SADDLEBROWN.copy()
    )

    //Variables for read/unread and location
    var location = SourceLocation(fileName, 1, 1)
    private var savedLoc = location.copy()
    private var savedChar: Char? = null
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
            if (ch == '#') while (this.readChar() !in listOf(null, '\n', '\r' )) continue
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

    fun readToken(): Token {
        if (savedToken != null) {
            val result: Token = savedToken!!
            savedToken = null
            return result
        }

        skipWhiteAndComment()
        // At this point we're sure that ch does *not* contain a whitespace character
        //Neither it's part of a comment
        val ch = readChar() ?: return StopToken(location = location)

        // At this point we must check what kind of token begins with the "ch" character (which has been
        // put back in the stream with unreadChar). First, we save the position in the stream
        val tokenLoc = location.copy()

        //One-character symbol, like '(' or ','
        return if (ch in SYMBOLS) SymbolToken(tokenLoc, ch)

        //A literal string (used for file names)
        else if (ch == '"') this.parseStringToken(tokenLoc)

        // A floating-point number
        else if (ch.isDigit() || ch in listOf('+', '-', '.')) parseFloatToken(ch.toString(), tokenLoc)

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

    private fun expectSymbols(symbols: List<Char>) : Char {
        val token = readToken()
        if (token !is SymbolToken) {
            throw GrammarError(token.location, "got $token instead of symbol")
        }
        if ( token.symbol !in symbols) {
            throw GrammarError(token.location, "Expected one of the ${symbols} instead of $token")
        }
        return token.symbol
    }

    private fun expectKeywords(keywords: List<KeywordEnum>): KeywordEnum {
        val token = readToken()
        if (token !is KeywordToken) {
            throw GrammarError(token.location, "Got $token instead of keyword")
        }
        if (token.keyword !in keywords) {
            throw GrammarError(
                token.location,
                "Expected one of the keywords ${keywords} instead of $token"
            )
        }
        return token.keyword
    }

    private fun expectKeywordsOrIdentifier(keywords: List<KeywordEnum>): Any {
        return when (val token = readToken()) {
            is KeywordToken -> {
                if (token.keyword !in keywords) throw GrammarError(
                    token.location,
                    "Expected one of the keywords ${keywords} instead of $token"
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
        val token = readToken()
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
        val token = readToken()
        val result : Color = if (token is LiteralNumberToken) {
            val red = token.value
            expectSymbol(',')
            val green = expectNumber(scene)
            expectSymbol(',')
            val blue = expectNumber(scene)
            Color(red, green, blue)
        } else if (token is IdentifierToken) {
            if (token.identifier !in name2color.keys) throw (GrammarError(token.location,"you must specify an allowed color between <>"))
            else name2color[token.identifier]!!
        } else throw (GrammarError(token.location,"you must specify a color between <>"))
        expectSymbol('>')
        return result
    }

    private fun parsePigment(scene: Scene): Pigment {
        val keyword = expectKeywords(listOf(KeywordEnum.UNIFORM, KeywordEnum.CHECKERED, KeywordEnum.IMAGE,
            KeywordEnum.MARBLE, KeywordEnum.WOOD, KeywordEnum.LAVA))
        expectSymbol('(')
        val result: Pigment
        when (keyword) {
            KeywordEnum.UNIFORM -> {
                val color = parseColor(scene)
                result = UniformPigment(color)
            }
            KeywordEnum.CHECKERED -> {
                val c1 = parseColor(scene)
                expectSymbol(',')
                val c2 = parseColor(scene)
                expectSymbol(',')
                val nSteps = expectNumber(scene).toInt()
                result = CheckeredPigment(color1 = c1, color2 = c2, numOfSteps = nSteps)
            }
            KeywordEnum.IMAGE -> {
                val fileName = expectString()
                val img = HdrImage()
                img.readImg(fileName)
                result = ImagePigment(image = img)
            }
            KeywordEnum.MARBLE ->{
                val t = expectSymbols(listOf(')', '<'))
                if (t == ')') {
                    unreadChar(')')
                    result = MarblePigment()
                }
                else {
                    unreadChar('<')
                    val c1 = parseColor(scene)
                    expectSymbol(',')
                    val c2 = parseColor(scene)
                    expectSymbol(',')
                    val xPeriod = expectNumber(scene)
                    expectSymbol(',')
                    val yPeriod = expectNumber(scene)
                    expectSymbol(',')
                    val turbPower = expectNumber(scene)
                    expectSymbol(',')
                    val octaves = expectNumber(scene).toInt()
                    result = MarblePigment(
                        c1 = c1,
                        c2=c2,
                        xPeriod = xPeriod,
                        yPeriod=yPeriod,
                        turbPower=turbPower,
                        octaves=octaves
                    )
                }
                                 }
            KeywordEnum.WOOD -> {
                val t = expectSymbols(listOf(')', '<'))
                if (t == ')') {
                    unreadChar(')')
                    result = WoodPigment()
                }
                else {
                    unreadChar('<')
                    val c1 = parseColor(scene)
                    expectSymbol(',')
                    val c2 = parseColor(scene)
                    expectSymbol(',')
                    val xyPeriod = expectNumber(scene)
                    expectSymbol(',')
                    val turbPower = expectNumber(scene)
                    expectSymbol(',')
                    val octaves = expectNumber(scene).toInt()
                    result = WoodPigment(
                        c1 = c1,
                        c2=c2,
                        xyPeriod = xyPeriod,
                        turbPower=turbPower,
                        octaves=octaves
                    )
                }
            }
            KeywordEnum.LAVA -> {
                val scale = expectNumber(scene)
                expectSymbol(',')
                val octaves = expectNumber(scene).toInt()
                result = LavaPigment(scale, octaves)
            }
            else -> throw (RuntimeException("This line should be unreachable"))
        }
        expectSymbol(')')
        return result
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
                    result *= result.scaling(parseVector(scene))
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

    fun parseCamera(scene: Scene): Camera {
        expectSymbol('(')
        val keyword = expectKeywords(listOf(KeywordEnum.PERSPECTIVE, KeywordEnum.ORTHOGONAL))
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(',')
        val aspectRatio = expectNumber(scene)
        expectSymbol(',')
        val distance = expectNumber(scene)
        expectSymbol(')')

        when (keyword) {
            KeywordEnum.PERSPECTIVE -> return PerspectiveCamera(dist = distance, AR = aspectRatio, T = transformation)
            KeywordEnum.ORTHOGONAL -> return OrthogonalCamera(AR = aspectRatio, T = transformation)
            else -> throw (RuntimeException("This line should be unreachable"))
        }
    }

    fun parseScene(variables: MutableMap<String, Float>): Scene {
        val scene = Scene()
        scene.floatVariables = variables
        scene.overriddenVariables = variables.keys
        while (true) {
            val what = readToken()
            if (what is StopToken) break
            if (what !is KeywordToken && what !is IdentifierToken) {
                throw GrammarError(what.location, "got $what instead of expected keyword")
            }
            if (what is KeywordToken && what.keyword == KeywordEnum.FLOAT) {
                val variableName = expectIdentifier()
                val variableLocation = location
                expectSymbol('(')
                val variableValue = expectNumber(scene)
                expectSymbol(')')
                if (variableName in scene.floatVariables && variableName !in scene.overriddenVariables) {
                    throw GrammarError(source = variableLocation, "variable $variableName cannot be redefined")
                }
                if (variableName !in scene.overriddenVariables) {
                    scene.floatVariables[variableName] = variableValue
                }
            } else if ((what is KeywordToken && what.keyword in shape2Parser.keys) || what is IdentifierToken) {
                unreadToken(what)
                scene.world.add(parseShape(scene))

            } else if (what is KeywordToken && what.keyword == KeywordEnum.CAMERA) {
                if (scene.camera != null) {
                    throw GrammarError(
                        what.location,
                        "One camera already defined. You cannot define more than one camera"
                    )
                }
                scene.camera = parseCamera(scene)
            } else if (what is KeywordToken && what.keyword == KeywordEnum.MATERIAL) {
                val pair = parseMaterial(scene)
                scene.materials[pair.first] = pair.second
            } else if (what is KeywordToken && what.keyword == KeywordEnum.SHAPE) {
                val name = expectIdentifier()
                scene.shapeVariables[name] = parseShape(scene)
            }
        }
    return scene

}

private fun parseShape(scene: Scene): Shape {
    val s = expectKeywordsOrIdentifier(
        listOf(
            KeywordEnum.SPHERE,
            KeywordEnum.PLANE,
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
            else {
                //The identified shape can be transformed, therefore
                // now transformation must be taken into account
                expectSymbol('(')
                val transformation = parseTransformation(scene)
                expectSymbol(')')

                transformation * scene.shapeVariables[s.identifier]!!
            }
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

    private fun parseHyperboloid(scene: Scene): Hyperboloid {
        expectSymbol('(')
        val minZ = expectNumber(scene)
        expectSymbol(',')
        val maxZ = expectNumber(scene)
        expectSymbol(',')
        val materialName = expectIdentifier()
        if (materialName !in scene.materials) {
            throw GrammarError(location, "unknown material $materialName")
        }
        expectSymbol(',')
        val transformation = parseTransformation(scene)
        expectSymbol(')')

        return Hyperboloid(minZ = minZ, maxZ = maxZ, T = transformation, material = scene.materials[materialName]!!)
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

