package year2024.day11

import application.Day
import utils.ImportUtils
import java.util.*

/**
 * See https://adventofcode.com/2024/day/11
 */
class Day11 : Day() {

    override val loggingEnabled: Boolean
        get() = false

    val FILE_PATH: String = "src/main/resources/year2024/day11/input_test_01.txt"


    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val parsedInput = parseInput(input)
        log("Parsed Input: $parsedInput")
        val numberOfBlinks = 25
        return calculate(parsedInput, numberOfBlinks).toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val parsedInput = parseInput(input)
        log("Parsed Input: $parsedInput")
        val numberOfBlinks = 75
        return calculate(parsedInput, numberOfBlinks).toString()
    }

    private fun parseInput(input: List<String>): List<Long> {
      return  input[0].split(" ").map { it.toLong() }
    }

    private fun calculate(
        initialStones: List<Long>,
        blinks: Int
    ): Long {
        var nextStones: List<Long> = ArrayList(initialStones)


        // Initialise the map of occurences.
        var occurrences = initialStones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

        // Now do the blinks.
        for (i in 0..<blinks) {
            val nextOccurrences: MutableMap<Long, Long> = HashMap()
            for ((key, value) in occurrences) {
                nextStones = applyStoneRules(key)
                nextStones.forEach { st ->
                    nextOccurrences[st] = nextOccurrences.getOrDefault(st, 0L) + value
                }
            }
            occurrences = nextOccurrences
        }

        log(occurrences.toString())

        return occurrences.values.stream().mapToLong { l: Long? -> l!! }.sum()
    }

    private fun applyStoneRules(stone: Long): List<Long> {
        return when {
            stone == 0L -> listOf(1L)
            stone.toString().length % 2 == 0 -> splitIfEven(stone)
            else -> listOf(stone * 2024L)
        }
    }

    private fun splitIfEven(l: Long): List<Long> {
        val result: MutableList<Long> = ArrayList()
        val s = l.toString()
        if (s.length % 2 == 0) {
            val mid = s.length / 2
            return listOf(s.substring(0, mid).toLong(), s.substring(mid).toLong())
        }
        return result
    }

}
