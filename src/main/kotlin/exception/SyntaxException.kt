package exception

import java.lang.RuntimeException

class SyntaxException (override val message: String, val term: String) : RuntimeException()