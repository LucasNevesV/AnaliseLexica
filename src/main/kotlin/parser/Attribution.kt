package parser

import Scanner
import Token
import exception.SyntaxException

class Attribution(private var token: Token?, private val scanner: Scanner) {
    init {
        attribution()
    }

    private fun attribution(loop:Boolean = false){
        if (!loop) expectIdentifier()

        expectNextAttrOperatorOrSemicolonOrComma()

        Arithmetic(token, scanner)

        expectCurrentAttrOperatorOrSemicolonOrComma()

        if(isComma()){
            attribution(true)
        }
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
}