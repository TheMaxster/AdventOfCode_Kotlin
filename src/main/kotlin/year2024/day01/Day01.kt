package year2024.day01

import application.Day
import utils.ImportUtils

/**
 * See https://adventofcode.com/2024/day/1
 */
class Day01 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day01/input_test_01.txt"

    override val loggingEnabled: Boolean
        get() = TODO("Not yet implemented")

    override fun part1(input: List<String>): String {
        val testInput = ImportUtils.readAsList(FILE_PATH)

        val inputToUse = input

        val numberPairs = parseInput(inputToUse)

        val leftNumbersSorted = numberPairs.first.sorted()
        val rightNumbersSorted = numberPairs.second.sorted()

        val differences: MutableList<Int> = ArrayList()
        for (i in inputToUse.indices) {
            differences.add(Math.abs(leftNumbersSorted.get(i) - rightNumbersSorted.get(i)))
        }

        return differences.sum().toString()
    }

    fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
        val leftNumbers: MutableList<Int> = ArrayList()
        val rightNumbers: MutableList<Int> = ArrayList()

        for (s in input) {
            val splitArray = s.split("   ").map { it.toInt() }
            leftNumbers.add(splitArray[0])
            rightNumbers.add(splitArray[1])
        }

        return Pair(leftNumbers, rightNumbers)
    }

    override fun part2(input: List<String>): String {
        val testInput = ImportUtils.readAsList(FILE_PATH)

        val inputToUse = input

        val numberPairs = parseInput(input)

        val similarityScores = numberPairs.first.map { left ->
            left * numberPairs.second.count { it == left }
        }

        return similarityScores.sum().toString()
    }

}
