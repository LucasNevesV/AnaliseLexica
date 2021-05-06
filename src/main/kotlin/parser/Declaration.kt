package parser

import Scanner
import Token
import exception.SyntaxException

class Declaration(private val scanner: Scanner) {

    lateinit var typeDeclaration: String

    init {
        declaration()
    }

    private fun declaration(loop:Boolean = false){
        if (!loop) expectVariableTypeDeclaration()

        expectIdentifier()
        expectNextAttrOperatorOrSemicolonOrComma()

        if (isAttributionOperator()) {
            Arithmetic(scanner)
        }else if(isComma()){
            declaration(true)
        }
        //expectSemicolon()
    }

    private fun expectSemicolon() {
        if (TokenSingleton.text != ";") {
            throw SyntaxException("';' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectVariableTypeDeclaration(){
        val tokenText = TokenSingleton.text
        if (!check()){
            throw SyntaxException("'type Declaration Expected' expected, found '${scanner.term}'", scanner.term)
        }
        typeDeclaration = tokenText!!

    }

    private fun expectIdentifier(){
        scanner.nextToken()
        if (TokenSingleton.type != TokenTypes.TK_IDENTIFIER){
            throw SyntaxException("'identifier Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectNextAttrOperatorOrSemicolonOrComma(){
        scanner.nextToken()
        if (!isAttributionOperator() && !isComma() && !isSemicolon()){
            throw SyntaxException("'attribution operator Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectCurrentAttrOperatorOrSemicolonOrComma(){
        scanner.nextToken()
        if (!isAttributionOperator() && !isComma() && !isSemicolon()){
            throw SyntaxException("'attribution operator, semicolon or comma Expected' expected, found '${scanner.term}'", scanner.term)
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
        return tokenText == "int" ||  tokenText =="float" || tokenText == "char"
    }
}