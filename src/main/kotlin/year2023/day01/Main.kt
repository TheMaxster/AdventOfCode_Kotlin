package year2023.day01

import year2023.BigDecimalExtensions.sum
import year2023.ImportUtils
import java.math.BigDecimal

const val filePath: String = "src/main/resources/year2023/day01/input.txt"

fun main() {
    part1()
    part2()
}

private fun part1() {
    // Check the relative path.
    println("Regular path: " + System.getProperty("user.dir"))

    val inputList: List<String> = ImportUtils.readAsList(filePath)
    val bdNumbers: MutableList<BigDecimal> = mutableListOf()

    inputList.forEach { line ->
        val number = line.findFirstAndLastNumber()
        println("$line => $number")
        bdNumbers.add(BigDecimal(number))
    }

    val globalResult = bdNumbers.sum().toString()
    println("Our result is: $globalResult")
}


private fun String.findLastNumber(): String {
    return reversed().findFirstNumber()
}

private fun String.findFirstNumber(): String {
    return find { it.isDigit() }?.toString() ?: "0"
}

private fun String.findFirstAndLastNumber(): String {
    return findFirstNumber() + findLastNumber()
}

private fun part2() {

    val pointsTable = mapOf(
        "1" to "1",
        "2" to "2",
        "3" to "3",
        "4" to "4",
        "5" to "5",
        "6" to "6",
        "7" to "7",
        "8" to "8",
        "9" to "9",
        "10" to "10",
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    val inputList: List<String> = ImportUtils.readAsList(filePath)
    val bdNumbers = mutableListOf<BigDecimal>()

    inputList.forEach { line ->
        val tmpMap = mutableMapOf<Int, String>()

        for ((key, value) in pointsTable) {
            var index = -1
            do {
                index += 1
                index = line.indexOf(key, index)
                if (index != -1) {
                    tmpMap[index] = value
                }
            } while (index != -1)
        }

        val min = tmpMap.keys.minOrNull()
        val firstNumber2 = tmpMap[min]

        val max = tmpMap.keys.maxOrNull()
        val lastNumber2 = tmpMap[max]

        val number = "$firstNumber2$lastNumber2"
        println("$line => $number")
        bdNumbers.add(BigDecimal(number))
    }

    val globalResult = bdNumbers.sum().toString()
    println("Our result is: $globalResult")
}
