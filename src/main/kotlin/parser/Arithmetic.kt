package parser

import Scanner
import exception.SemanticException
import exception.SyntaxException
import generateCode.Symbols

class Arithmetic(val scanner: Scanner, var expression: String = "") {
    init {
        arithmetic()
    }

    private fun addExpression(text: String?) {
        expression += text.orEmpty()
    }

    private fun arithmetic() {
        expression = ""
        if (Symbols.currentSymbol?.type == TokenTypes.TK_NUMBER.toString()) {
            val identifier = expectNumberOrIdentifier()
            if (identifier == null){
                addExpression(TokenSingleton.text)
            }else{
                addExpression(identifier)
            }
        } else if (Symbols.currentSymbol?.type == TokenTypes.TK_FLOAT.toString()) {
            val identifier = expectFloatOrIdentifier()
            if (identifier == null){
                addExpression(TokenSingleton.text)
            }else{
                addExpression(identifier)
            }
        } else {
            scanner.nextToken()
            addExpression(TokenSingleton.text)
        }

        arithmeticLoop()
    }

    private fun arithmeticLoop() {
        val token = scanner.nextToken()
        if (token != null && checkCompatibility()) {
            addExpression(TokenSingleton.text)
            expectArithmeticOperator()
            if (Symbols.currentSymbol?.type == TokenTypes.TK_NUMBER.toString()) {
                val identifier = expectNumberOrIdentifier()
                if (identifier == null){
                    addExpression(TokenSingleton.text)
                }else{
                    addExpression(identifier)
                }
            } else if (Symbols.currentSymbol?.type == TokenTypes.TK_FLOAT.toString()) {
                val identifier = expectFloatOrIdentifier()
                if (identifier == null){
                    addExpression(TokenSingleton.text)
                }else{
                    addExpression(identifier)
                }
            } else {
                scanner.nextToken()
                addExpression(TokenSingleton.text)
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

    private fun expectNumberOrIdentifier(): String? {
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

            if (sameSymbol == null) {
                throw SemanticException(
                    "identifier of type 'int' expected, found '${scanner.term}'",
                    scanner.term
                )
            }
            return sameSymbol.code
        }
        return null
    }

    private fun expectFloatOrIdentifier(): String? {
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

            if (sameSymbol == null) {
                throw SemanticException(
                    "identifier of type 'float' expected, found '${scanner.term}'",
                    scanner.term
                )
            }
            return sameSymbol.code
        }
        return null
    }

    private fun checkCompatibility(): Boolean {
        return TokenSingleton.type == TokenTypes.TK_IDENTIFIER || TokenSingleton.type == TokenTypes.TK_NUMBER ||
                (TokenSingleton.type == TokenTypes.TK_ARITHMETIC_OPERATOR && TokenSingleton.text != "=")
    }
}