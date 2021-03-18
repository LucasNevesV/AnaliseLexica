data class Token(var type: TokenTypes, val text: String? = null)

enum class TokenTypes(private val description: String) {
    TK_IDENTIFIER("IDENTIFIER"),
    TK_NUMBER("NUMBER"),
    TK_RELATIONAL_OPERATOR("RELATIONAL OPERATOR"),
    TK_ARITHMETIC_OPERATOR("ARITHMETIC OPERATOR"),
    TK_RESERVED_WORD("RESERVED WORD"),
    TK_SPECIAL_CHAR("SPECIAL CHAR");

    override fun toString(): String {
        return description
    }
}