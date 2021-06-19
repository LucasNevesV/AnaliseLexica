data class Token(var type: TokenTypes, val text: String? = null)

object TokenSingleton{
    var type: TokenTypes? = null
    var text: String? = null
    var token: Token? = null

    fun init(token: Token?){
        this.token = token
        this.type = token?.type
        this.text = token?.text
    }
}

enum class TokenTypes(private val description: String) {
    TK_IDENTIFIER("IDENTIFIER"),
    TK_NUMBER("int"),
    TK_FLOAT("float"),
    TK_RELATIONAL_OPERATOR("RELATIONAL OPERATOR"),
    TK_ARITHMETIC_OPERATOR("ARITHMETIC OPERATOR"),
    TK_RESERVED_WORD("RESERVED WORD"),
    TK_SPECIAL_CHAR("SPECIAL CHAR");

    override fun toString(): String {
        return description
    }
}