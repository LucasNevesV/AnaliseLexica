package parser

import Scanner
import Token
import exception.SemanticException
import exception.SyntaxException
import generateCode.Symbols

class Arithmetic(val scanner: Scanner) {
    init {
        arithmetic()
    }

    private fun arithmetic() {
        if (Symbols.currentSymbol?.type == TokenTypes.TK_NUMBER.toString()) {
            expectNumberOrIdentifier()
        } else if (Symbols.currentSymbol?.type == TokenTypes.TK_FLOAT.toString()) {
            expectFloatOrIdentifier()
        }else{
            scanner.nextToken()
        }

        arithmeticLoop()
    }

    private fun arithmeticLoop() {
        val token = scanner.nextToken()
        if (token != null && checkCompatibility()) {
            expectArithmeticOperator()
            if (Symbols.currentSymbol?.type == TokenTypes.TK_NUMBER.toString()) {
                expectNumberOrIdentifier()
            } else if (Symbols.currentSymbol?.type == TokenTypes.TK_FLOAT.toString()) {
                expectFloatOrIdentifier()
            }else{
                scanner.nextToken()
            }
            arithmeticLoop()
        }
    }

    fun expectArithmeticOperator() {
        if (TokenSingleton.type != TokenTypes.TK_ARITHMETIC_OPERATOR && TokenSingleton.type != TokenTypes.TK_RELATIONAL_OPERATOR) {
            throw SyntaxException(
                "Operator Expected, found " + TokenSingleton.type + " (" + TokenSingleton.text + ")", scanner.term
            )
        }
    }

    private fun expectNumberOrIdentifier() {
        scanner.nextToken()
        if (TokenSingleton.type != TokenTypes.TK_IDENTIFIER && TokenSingleton.type != TokenTypes.TK_NUMBER) {
            throw SemanticException(
                "identifier or int expected, found ${TokenSingleton.type} '${scanner.term}'",
                scanner.term
            )
        }

        if (TokenSingleton.type == TokenTypes.TK_IDENTIFIER) {
            val sameSymbol =
                Symbols.value.find {
                    it.name == TokenSingleton.text.orEmpty() &&
                            it.scope <= Symbols.currentScope &&
                            it.type == "int"
                }

            if (sameSymbol == null){
                throw SemanticException(
                    "identifier of type 'int' expected, found '${scanner.term}'",
                    scanner.term
                )
            }
        }
    }

    private fun expectFloatOrIdentifier() {
        scanner.nextToken()
        if (TokenSingleton.type != TokenTypes.TK_IDENTIFIER && TokenSingleton.type != TokenTypes.TK_FLOAT) {
            throw SemanticException(
                "identifier or float expected, found ${TokenSingleton.type} '${scanner.term}'",
                scanner.term
            )
        }

        if (TokenSingleton.type == TokenTypes.TK_IDENTIFIER) {
            val sameSymbol =
                Symbols.value.find {
                    it.name == TokenSingleton.text.orEmpty() &&
                            it.scope <= Symbols.currentScope &&
                            it.type == "float"
                }

            if (sameSymbol == null){
                throw SemanticException(
                    "identifier of type 'float' expected, found '${scanner.term}'",
                    scanner.term
                )
            }
        }
    }

    private fun checkCompatibility(): Boolean {
        return TokenSingleton.type == TokenTypes.TK_IDENTIFIER || TokenSingleton.type == TokenTypes.TK_NUMBER ||
                (TokenSingleton.type == TokenTypes.TK_ARITHMETIC_OPERATOR && TokenSingleton.text != "=")
    }
}