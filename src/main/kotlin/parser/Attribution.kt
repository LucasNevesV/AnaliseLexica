package parser

import Scanner
import Token
import exception.SyntaxException

class Attribution(private val scanner: Scanner) {
    init {
        attribution()
    }

    private fun attribution(loop:Boolean = false){
        if (loop) expectIdentifier()

        expectNextAttrOperatorOrSemicolonOrComma()

        Arithmetic(scanner)

        expectCurrentAttrOperatorOrSemicolonOrComma()

        if(isComma()){
            attribution(true)
        }
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
}