data class Token(var type: Int, val text: String? = null)

enum class TokenTypes(){
    TK_IDENTIFIER,
    TK_NUMBER,
    TK_OPERATOR,
    TK_PONCTUATION,
    TK_ASSIGN,
}