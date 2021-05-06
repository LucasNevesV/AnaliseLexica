package parser

import Scanner
import Token
import exception.SyntaxException

class Declaration(private var token: Token?, private val scanner: Scanner) {

    lateinit var typeDeclaration: String

    init {
        declaration()
    }

    private fun declaration(loop:Boolean = false){
        if (!loop) expectVariableTypeDeclaration()

        expectIdentifier()
        expectNextAttrOperatorOrSemicolonOrComma()

        if (isAttributionOperator()) {
            Arithmetic(token, scanner)
        }else if(isComma()){
            declaration(true)
        }
        //expectSemicolon()
    }

    private fun expectSemicolon() {
        if (token!!.text != ";") {
            throw SyntaxException("';' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectVariableTypeDeclaration(){
        val tokenText = token!!.text
        if (!check()){
            throw SyntaxException("'type Declaration Expected' expected, found '${scanner.term}'", scanner.term)
        }
        typeDeclaration = tokenText!!

    }

    private fun expectIdentifier(){
        token = scanner.nextToken()
        if (token!!.type != TokenTypes.TK_IDENTIFIER){
            throw SyntaxException("'identifier Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectNextAttrOperatorOrSemicolonOrComma(){
        token = scanner.nextToken()
        if (!isAttributionOperator() && !isComma() && !isSemicolon()){
            throw SyntaxException("'attribution operator Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectCurrentAttrOperatorOrSemicolonOrComma(){
        token = scanner.nextToken()
        if (!isAttributionOperator() && !isComma() && !isSemicolon()){
            throw SyntaxException("'attribution operator, semicolon or comma Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun isComma(): Boolean {
        return token!!.text == ","
    }

    private fun isSemicolon(): Boolean {
        return token!!.text == ";"
    }

    private fun isAttributionOperator(): Boolean {
        return token!!.text == "="
    }

    private fun check(): Boolean {
        val tokenText = token!!.text
        return tokenText == "int" ||  tokenText =="float" || tokenText == "char"
    }
}