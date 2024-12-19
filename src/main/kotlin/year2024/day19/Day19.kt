package year2024.day19

import application.Day
import utils.ImportUtils
import java.util.*

/**
 * See https://adventofcode.com/2024/day/19
 */
class Day19 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day19/input_test_01.txt"

    override val loggingEnabled: Boolean
        get() = false

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val (towelPatterns, desiredPatterns) = parseInput(input)
        val cache = mutableMapOf<String, Long>()
        val possibilities = desiredPatterns.count { countPossible(it, towelPatterns, cache) > 0 }

        return possibilities.toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val (towelPatterns, desiredPatterns) = parseInput(input)
        val cache = mutableMapOf<String, Long>()
        val count = desiredPatterns.sumOf { countPossible(it, towelPatterns, cache) }

        return count.toString()
    }

    private fun countPossible(
        design: String,
        towels: List<String>,
        cache: MutableMap<String, Long>
    ): Long {
        if (cache.containsKey(design)) {
            return cache[design]!!
        }
        var count = 0L
        for (towel in towels) {
            if (design == towel) {
                count += 1
            } else if (design.startsWith(towel)) {
                count += countPossible(design.substring(towel.length), towels, cache)
            }
        }
        cache[design] = count
        return count
    }

    private fun parseInput(input: List<String>): Pair<List<String>, List<String>> {
        val towelPatterns = input.first().split(",").map { it.trim() }
        val desiredPatterns = input.drop(2) // Skip the first two lines
        return Pair(towelPatterns, desiredPatterns)
    }


}
