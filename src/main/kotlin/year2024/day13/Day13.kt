package year2024.day13

import application.Day
import utils.ImportUtils
import java.util.regex.Pattern
import kotlin.math.floor

/**
 * See https://adventofcode.com/2024/day/13
 */
class Day13 : Day() {
    override val loggingEnabled: Boolean
        get() = false

    val FILE_PATH: String = "src/main/resources/year2024/day13/input_test_01.txt"

    val COST_A: Int = 3
    val COST_B: Int = 1

    val PATTERN_BUTTON_A: Pattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)")
    val PATTERN_BUTTON_B: Pattern = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)")
    val PATTERN_PRIZE: Pattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)")

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val inputs = parseInput(input)

        val offset = 0L
        val limit: Long = 100

        val results = inputs
            .mapNotNull { data: InputData -> solveEquations(data, offset, limit) }
            .toList()

        val totalCostA = COST_A * results.sumOf { it.first }
        val totalCostB = COST_B * results.sumOf { it.second }

        return (totalCostA + totalCostB).toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val inputs = parseInput(input)

        val offset = 10000000000000L
        val limit = Long.MAX_VALUE

        val results = inputs
            .mapNotNull { data: InputData -> solveEquations(data, offset, limit) }
            .toList()

        val totalCostA = COST_A * results.sumOf { it.first }
        val totalCostB = COST_B * results.sumOf { it.second }

        return (totalCostA + totalCostB).toString()
    }

    private fun solveEquations(
        inputData: InputData,
        offset: Long,
        limit: Long
    ): Pair<Long, Long>? {
        // We have to solve two equations of kind: a1*x+b1*y=c1, where a and b and c are known.
        // We can set them equal so that we only have to solve x.

        val Ax: Long = inputData.buttonA.first
        val Ay: Long = inputData.buttonA.second
        val Bx: Long = inputData.buttonB.first
        val By: Long = inputData.buttonB.second
        val prizeX: Long = inputData.prize.first + offset
        val prizeY: Long = inputData.prize.second + offset

        val xMul = ((prizeX * By) - (prizeY * Bx)) / ((Ax * By) - (Ay * Bx))
        log("x: $xMul")

        if (!isLong(xMul.toDouble()) || xMul > limit || xMul <= 0) {
            return null
        }

        val yMul = (prizeX - Ax * xMul) / Bx
        log("y: $yMul")

        if (!isLong(yMul.toDouble()) || yMul > limit || yMul <= 0) {
            return null
        }

        // Double check
        val firstCheck = xMul * Ax + yMul * Bx == prizeX
        val secondCheck = xMul * Ay + yMul * By == prizeY
        if (!firstCheck || !secondCheck) {
            return null
        }

        return Pair(xMul, yMul)
    }

    private fun isLong(number: Double): Boolean {
        return number == floor(number)
    }

    private fun parseInput(input: List<String>): List<InputData> {
        return input.chunked(4).map { chunk ->
            val buttonA = parseCoordinate(chunk[0], PATTERN_BUTTON_A)
            val buttonB = parseCoordinate(chunk[1], PATTERN_BUTTON_B)
            val prize = parseCoordinate(chunk[2], PATTERN_PRIZE)
            InputData(buttonA, buttonB, prize)
        }
    }

    private fun parseCoordinate(line: String, pattern: Pattern): Pair<Long, Long> {
        val matcher = pattern.matcher(line)
        if (matcher.find()) {
            return Pair(matcher.group(1).toLong(), matcher.group(2).toLong())
        }
        throw IllegalArgumentException("Invalid line format: $line")
    }

    data class InputData (
        var buttonA: Pair<Long, Long>,
        var buttonB: Pair<Long, Long>,
        var prize: Pair<Long, Long>
    )

}
