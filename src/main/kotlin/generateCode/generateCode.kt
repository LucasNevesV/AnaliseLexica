package generateCode

import parser.Arithmetic

data class Symbol(val name: String, val type: String, val scope: Int, var value: String? = "", var code: String? = "")

object Symbols {
    val value: MutableList<Symbol> = mutableListOf<Symbol>()
    var currentScope: Int = -1
    var count: Int = 0
    var currentSymbol: Symbol? = null

    val conditionStack = mutableListOf<String>()
    var conditionCount = 0

    var finalCode: StringBuilder = StringBuilder()

    fun updateSymbol(symbol: Symbol) {
        finalCode.appendLine("${symbol.code} = ${symbol.value}")
    }

    fun addExpression(symbol: Symbol, expression: String) {
        symbol.code = "t${count}"
        value += symbol
        finalCode.appendLine("t${count} = $expression")
        count++

        symbol.code = "t${count}"
        value += symbol
        return

        val expressionSplitMinus = expression.split('-')

        for (minusSplit in expressionSplitMinus) {
            var minus: String = minusSplit


            val expressionSplit1Plus = minusSplit.split('+')
            var plus: String = ""
            for (plusSplit in expressionSplit1Plus) {

                val expressionSplit2Div = plusSplit.split('/')
                for (divisionSplit in expressionSplit2Div) {
                    symbol.code = "t${count}"
                    value += symbol
                    finalCode.appendLine("t${count} = $divisionSplit")


                    val expressionSplit3Mult = divisionSplit.split('*')
                    if (expressionSplit3Mult.size > 1) {
                        plus = plusSplit.replace(divisionSplit, "t$count")
                        minus = minus.replace(divisionSplit, plus)
                    }
                    count++
                }
            }
            val paresPlus = plus.split("+")
            //for ()
        }
    }

//    fun test(symbol: Symbol, operator: String) {
//        symbol.code = "t${count}"
//        value += symbol
//        finalCode.appendLine("t${count} = $divisionSplit")
//
//        if (divisionSplit.split('*').size > 1){
//            plus = plusSplit.replace(divisionSplit, "t$count")
//            //minus = minus.replace(divisionSplit, plus)
//        }
//        count++
//    }

    fun addIf(operator: String?, operatorOne: String, operatorTwo: String) {
        val opositeOperator = getOpositeOperator(operator)
        finalCode.appendLine("if $operatorOne $opositeOperator $operatorTwo goto L$conditionCount")

        conditionStack.add("L$conditionCount")
        conditionCount++
    }

    fun endIf() {
        if (conditionStack.isNotEmpty()){
            val jump = conditionStack.removeLast()
            finalCode.appendLine("$jump:")
        }
    }

    fun getOpositeOperator(operator: String?): String {
        return operatorsMap[operator] ?: operatorsMap.values.first { it == operator }
    }

    private val operatorsMap = hashMapOf(
        ">" to "<=",
        "<" to ">=",
        "==" to "!="
    )

    fun addSymbol(symbol: Symbol, new: String) {
        symbol.code = "t${count}"
        value += symbol
        finalCode.appendLine("t${count} = $new")
        count++
    }
}