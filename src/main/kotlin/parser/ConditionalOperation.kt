package parser

import Scanner
import Token
import exception.SyntaxException

class ConditionalOperation(private val scanner: Scanner) {
    init {
        conditional()
    }

    private fun conditional(){
        Arithmetic(scanner)

        expectRelationalOperator()
        Arithmetic(scanner)
    }

    private fun checkWhile(): Boolean {
        return TokenSingleton.text == "while"
    }

    private fun expectRelationalOperator(){
        if (TokenSingleton.type != TokenTypes.TK_RELATIONAL_OPERATOR || TokenSingleton.text == "=") {
            throw SyntaxException("'relational operator' expected, found '${scanner.term}'", scanner.term)
        }
    }
}