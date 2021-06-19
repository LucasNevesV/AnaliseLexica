package parser

import Scanner
import TokenSingleton
import TokenTypes
import exception.SyntaxException
import generateCode.Symbols

class ConditionalOperation(private val scanner: Scanner) {
    init {
        conditional()
    }

    private fun conditional(){
        val operatorOne = Arithmetic(scanner).expression

        val operator = expectRelationalOperator()
        val operatorTwo = Arithmetic(scanner).expression

        Symbols.addIf(operator, operatorOne, operatorTwo)
    }

    private fun checkWhile(): Boolean {
        return TokenSingleton.text == "while"
    }

    private fun expectRelationalOperator(): String? {
        if (TokenSingleton.type != TokenTypes.TK_RELATIONAL_OPERATOR || TokenSingleton.text == "=") {
            throw SyntaxException("'relational operator' expected, found '${scanner.term}'", scanner.term)
        }
        return TokenSingleton.text
    }
}