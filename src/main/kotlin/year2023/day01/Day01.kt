package year2023.day01

import application.Day
import utils.BigDecimalExtensions.sum
import java.math.BigDecimal
import java.util.*

/**
 * See https://adventofcode.com/2023/day/1
 */
class Day01 : Day() {
    
    override val loggingEnabled: Boolean
        get() = true

    //    private static final String FILE_PATH = "resources/main.resources.year2023/day01/input.txt";

    override fun part1(input: List<String>): String {
        //        final List<String> input = ImportUtils.readAsList(FILE_PATH);

        val bdNumbers: MutableList<BigDecimal> = ArrayList()
        for (line in input) {
            // Solution for problem 1:
            val firstNumber = findFirstNumber(line)
            val lastNumber = findLastNumber(line)
            val number = firstNumber + lastNumber

            // log(line + " => " + number);
            bdNumbers.add(BigDecimal(number))
        }

        return bdNumbers.sum().toString();
    }

    override fun part2(input: List<String>): String {
        val pointsTable: MutableMap<String, String> = HashMap()
        pointsTable["1"] = "1"
        pointsTable["2"] = "2"
        pointsTable["3"] = "3"
        pointsTable["4"] = "4"
        pointsTable["5"] = "5"
        pointsTable["6"] = "6"
        pointsTable["7"] = "7"
        pointsTable["8"] = "8"
        pointsTable["9"] = "9"
        pointsTable["10"] = "10"
        pointsTable["one"] = "1"
        pointsTable["two"] = "2"
        pointsTable["three"] = "3"
        pointsTable["four"] = "4"
        pointsTable["five"] = "5"
        pointsTable["six"] = "6"
        pointsTable["seven"] = "7"
        pointsTable["eight"] = "8"
        pointsTable["nine"] = "9"

        //        final List<String> input = ImportUtils.readAsList(FILE_PATH);
        val bdNumbers: MutableList<BigDecimal> = ArrayList()

        for (line in input) {
            // Solution for problem 2:
            val positions = pointsTable.flatMap { (key, value) ->
                line.findAllIndexes(key).map { it to value }
            }.toMap()

            val min = Collections.min(positions.keys)
            val firstNumber2 = positions[min]

            val max = Collections.max(positions.keys)
            val lastNumber2 = positions[max]

            val number = firstNumber2 + lastNumber2

            //      log(line + " => " + number);
            bdNumbers.add(BigDecimal(number))
        }

        return bdNumbers.sum().toString()
    }

    companion object {
        private fun findLastNumber(text: String): String {
            return findFirstNumber(text.reversed())
        }

        private fun findFirstNumber(text: String): String {
            return text.firstOrNull { it.isDigit() }?.toString() ?: "0"
        }

        private fun String.findAllIndexes(substring: String): List<Int> {
            val indexes = mutableListOf<Int>()
            var index = indexOf(substring)
            while (index >= 0) {
                indexes.add(index)
                index = indexOf(substring, index + 1)
            }
            return indexes
        }
    }
}
