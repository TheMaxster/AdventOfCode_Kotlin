package year2024.day02

import application.Day
import utils.ImportUtils

/**
 * See https://adventofcode.com/2024/day/2
 */
class Day02 : Day() {
    override val loggingEnabled: Boolean
        get() = true

    val FILE_PATH: String = "src/main/resources/year2024/day02/input_test_01.txt"

    override fun part1(input: List<String>): String {
          val importInput: List<String> = ImportUtils.readAsList(FILE_PATH);
        val importArray: List<List<Int>> = ImportUtils.convertListToIntArray(input, " ");

        val countValidLevels: Int = importArray.count { isLevelValid(it) }
        return countValidLevels.toString()
    }

    private fun isLevelValid(levels: List<Int>): Boolean {
        val ascending = levels[1] - levels[0] > 0
        return if (ascending) {
            levels.zipWithNext().all { (a, b) -> b - a in 1..3 }
        } else {
            levels.zipWithNext().all { (a, b) -> b - a in -3..-1 }
        }
            //.also { log("${levels.toList()} $it") }
    }

    override fun part2(input: List<String>): String {
        val importInput: List<String> = ImportUtils.readAsList(FILE_PATH);
        val importArray: List<List<Int>> = ImportUtils.convertListToIntArray(input, " ")

        val countValidLevels: Int = importArray.count { isLevelValidWithSkip(it) }
        return countValidLevels.toString()
    }

    private fun isLevelValidWithSkip(levels: List<Int>): Boolean {
        return isLevelValid(levels) || levels.indices.any { skipIndex ->
            isLevelValid(createSubArray(levels, skipIndex))
        }
    }

    private fun createSubArray(level: List<Int>, skipIndex: Int): List<Int> {
        return level.filterIndexed { index, _ -> index != skipIndex }.toList()
    }

}
