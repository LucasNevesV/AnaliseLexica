package generateCode

data class Symbol(val name: String, val type: String, val scope: Int, val value: String? = "")

object Symbols {
    val value: MutableList<Symbol> = mutableListOf<Symbol>()
    var currentScope: Int = -1
    var currentSymbol: Symbol? = null

    var finalCode: StringBuilder = StringBuilder()

    fun addSymbol(symbol: Symbol) {
        value += symbol

        finalCode.appendLine("t${value.size - 1} = ${symbol.value}")
    }
}