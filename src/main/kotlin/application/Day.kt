package application

abstract class Day {
    protected fun log(logStatement: String?) {
        if (loggingEnabled) {
            println(logStatement)
        }
    }

    fun logMap(array: Array<Array<String>>) {
        if (!loggingEnabled) {
            return
        }

        for (row in array) {
            for (element in row) {
                print("$element ")
            }
            println() // New line
        }
        println(" ")
    }

    fun logDenseMap(array: Array<Array<String?>>) {
        if (!loggingEnabled) {
            return
        }

        for (row in array) {
            for (element in row) {
                print(element)
            }
            println() // New line
        }
    }

    abstract val loggingEnabled: Boolean

    //    public default abstract void setLoggingEnabled(final Boolean loggingEnabled);
    abstract fun part1(input: List<String>): String

    abstract fun part2(input: List<String>): String
}
