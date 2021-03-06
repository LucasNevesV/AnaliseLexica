package parser

import Scanner
import TokenSingleton
import TokenTypes
import exception.SemanticException
import exception.SyntaxException
import generateCode.Symbol
import generateCode.Symbols

class Declaration(private val scanner: Scanner) {

    private lateinit var typeDeclaration: String

    init {
        declaration()
    }

    private fun declaration(loop: Boolean = false) {
        if (!loop) expectVariableTypeDeclaration()

        expectIdentifier()
        val name = TokenSingleton.text
        expectNextAttrOperatorOrSemicolonOrComma()

        val sameSymbol =
            Symbols.value.find { it.name == name.orEmpty() && it.scope == Symbols.currentScope && it.type == typeDeclaration }
        if (sameSymbol != null) {
            throw SemanticException(
                "variable with the same name '${name.orEmpty()}' and type '${typeDeclaration}' already declared in current scope",
                name.orEmpty()
            )
        }

//        //region AddSymbol
//        val pos = scanner.pos
//        val token = TokenSingleton.token
//        scanner.nextToken()
//        Symbols.addSymbol(Symbol(name.orEmpty(), typeDeclaration, Symbols.currentScope, TokenSingleton.text))
//        Symbols.currentSymbol = Symbol(name.orEmpty(), typeDeclaration, Symbols.currentScope)
//        scanner.pos = pos
//        TokenSingleton.init(token)
//        //endregion AddSymbol

        if (isAttributionOperator()) {
            Symbols.currentSymbol = Symbol(name.orEmpty(), typeDeclaration, Symbols.currentScope)
            val value = Arithmetic(scanner).expression
            Symbols.addExpression(Symbol(name.orEmpty(), typeDeclaration, Symbols.currentScope, value), value)
        }

        if (isComma()) {
            declaration(true)
        }
        //expectSemicolon()
    }

    private fun expectSemicolon() {
        if (TokenSingleton.text != ";") {
            throw SyntaxException("';' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectVariableTypeDeclaration() {
        val tokenText = TokenSingleton.text
        if (!check()) {
            throw SyntaxException("'type Declaration Expected' expected, found '${scanner.term}'", scanner.term)
        }
        typeDeclaration = tokenText!!

    }

    private fun expectIdentifier(): String {
        scanner.nextToken()
        if (TokenSingleton.type != TokenTypes.TK_IDENTIFIER) {
            throw SyntaxException("'identifier Expected' expected, found '${scanner.term}'", scanner.term)
        }

        return TokenSingleton.type.toString()
    }

    private fun expectNextAttrOperatorOrSemicolonOrComma() {
        scanner.nextToken()
        if (!isAttributionOperator() && !isComma() && !isSemicolon()) {
            throw SyntaxException("'attribution operator Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectCurrentAttrOperatorOrSemicolonOrComma() {
        scanner.nextToken()
        if (!isAttributionOperator() && !isComma() && !isSemicolon()) {
            throw SyntaxException(
                "'attribution operator, semicolon or comma Expected' expected, found '${scanner.term}'",
                scanner.term
            )
        }
    }

    private fun isComma(): Boolean {
        return TokenSingleton.text == ","
    }

    private fun isSemicolon(): Boolean {
        return TokenSingleton.text == ";"
    }

    private fun isAttributionOperator(): Boolean {
        return TokenSingleton.text == "="
    }

    private fun check(): Boolean {
        val tokenText = TokenSingleton.text
        return tokenText == "int" || tokenText == "float" || tokenText == "char"
    }
}