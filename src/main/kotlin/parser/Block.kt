package parser

import Scanner
import exception.SemanticException
import exception.SyntaxException

data class Symbol(val name: String, val type: String, val scope: Int)
object Symbols {
    val value: MutableList<Symbol> = mutableListOf<Symbol>()
    var currentScope: Int = -1
    var currentSymbol: Symbol? = null
}

class Block(val scanner: Scanner) {

    init {
        block()
    }

    var jump: Boolean = false

    private fun block() {
        expectedOpenBrackets()
        Symbols.currentScope += 1

        while (!expectedClosingBrackets()) {
            val tokenText = TokenSingleton.text
            when {
                TokenSingleton.type == TokenTypes.TK_IDENTIFIER -> {

                    Symbols.currentSymbol =
                        Symbols.value.find { it.name == TokenSingleton.text && it.scope <= Symbols.currentScope }
                            ?: throw SemanticException(
                                "Variable '${TokenSingleton.text}' not declared",
                                TokenSingleton.text.orEmpty()
                            )
                    Attribution(scanner)
                }
                tokenText == "while" -> {
                    Loop(scanner)
                }
                tokenText == "if" -> {
                    ConditionalExpression(scanner)
                    jump = true
                }
                //Declaration
                tokenText == "int" || tokenText == "float" || tokenText == "char" -> {
                    Declaration(scanner)
                }
            }
        }
        Symbols.currentScope -= 1
    }

    private fun expectedOpenBrackets() {
        scanner.nextToken()
        if (TokenSingleton.text != "{") {
            throw SyntaxException("'{' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectedClosingBrackets(): Boolean {
        if (!jump) scanner.nextToken()
        jump = false
        TokenSingleton.text ?: throw SyntaxException("Closing '}' expected", scanner.term)
        return TokenSingleton.text == "}"
    }
}