package year2024.day03

import application.Day
import utils.ImportUtils
import java.util.function.Consumer
import java.util.regex.Pattern

/**
 * See https://adventofcode.com/2024/day/4
 */
class Day03 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day03/input_test_01.txt"

    override val loggingEnabled: Boolean
        get() = false

    override fun part1(input: List<String>): String {
        // val importInput = ImportUtils.readAsList(FILE_PATH);
        val matches = input.flatMap { findAllMatches(it) }.toList();
        val sum = matches.stream()
            .mapToInt { p: Pair<Int, Int> -> p.first * p.second }
            .sum()

        return sum.toString()
    }

    private fun findAllMatches(text: String): List<Pair<Int, Int>> {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        return regex.findAll(text).map { match ->
            val (x, y) = match.destructured
            x.toInt() to y.toInt()
        }.toList()
    }

    override fun part2(input: List<String>): String {
        // val importInput = ImportUtils.readAsList(FILE_PATH);
        val longString = input.joinToString { it }
        return findAllMatchesForPart2(longString).sumOf { (x, y) -> x * y }.toString()
    }

    private fun findAllMatchesForPart2(text: String): List<Pair<Int, Int>> {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")
        val matches = mutableListOf<Pair<Int, Int>>()
        var enabled = true

        regex.findAll(text).forEach { match ->
            val value = match.value
            log("$enabled: $value")

            when (value) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> if (enabled) {
                    val (x, y) = match.destructured
                    matches.add(x.toInt() to y.toInt())
                }
            }
        }

        return matches
    }

}
