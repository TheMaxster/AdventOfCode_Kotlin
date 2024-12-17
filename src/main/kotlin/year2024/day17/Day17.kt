package year2024.day17

import application.Day
import utils.ImportUtils
import kotlin.math.pow

/**
 * See https://adventofcode.com/2024/day/17
 */
class Day17 : Day() {
    override val loggingEnabled: Boolean
        get() = false

    // val FILE_PATH: String = "src/main/resources/year2024/day17/input_test_01.txt";
    val FILE_PATH: String = "src/main/resources/year2024/day17/input_test_02.txt"

    private val registerRegex = Regex("Register [A-C]: (\\d+)")

    data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val usedInput = input
        val (a, b, c, program) = parseInput(usedInput)
        val output = runProgram(a, b, c, program)
        return output.joinToString(",")
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val usedInput = input
        val (_, b, c, program) = parseInput(usedInput)
        return findValueOfA(1, b, c, program).toString()
    }

    private fun parseInput(input: List<String>): Quadruple<Long, Long, Long, List<Long>> {
        val registers = input.take(3).mapNotNull { registerRegex.find(it)?.groupValues?.get(1)?.toLong() }
        val program = input[4].substringAfter("Program: ").split(",").map { it.toLong() }
        return Quadruple(registers[0], registers[1], registers[2], program)
    }

    private fun runProgram(aStart: Long, bStart: Long, cStart: Long, program: List<Long>): List<Long> {
        var (a, b, c) = Triple(aStart, bStart, cStart)
        val output = mutableListOf<Long>()
        var pointer = 0

        while (pointer < program.size) {
            val opcode = program[pointer].toInt()
            val operand = program[pointer + 1]

            when (opcode) {
                0 -> a /= 2.0.pow(getComboValue(operand, a, b, c).toDouble()).toLong()
                1 -> b = b xor operand
                2 -> b = getComboValue(operand, a, b, c) % 8
                3 -> if (a != 0L) {
                    pointer = operand.toInt()
                    continue
                }

                4 -> b = b xor c
                5 -> output.add(getComboValue(operand, a, b, c) % 8)
                6 -> b = a / 2.0.pow(getComboValue(operand, a, b, c).toDouble()).toLong()
                7 -> c = a / 2.0.pow(getComboValue(operand, a, b, c).toDouble()).toLong()
                else -> throw IllegalArgumentException("Unknown opcode: $opcode")
            }
            pointer += 2
        }
        return output
    }

    // Helper to compute the value of combo operands
    private fun getComboValue(operand: Long, a: Long, b: Long, c: Long): Long {
        return when (operand.toInt()) {
            in 0..3 -> operand
            4 -> a
            5 -> b
            6 -> c
            else -> throw IllegalArgumentException("Invalid combo operand: $operand")
        }
    }

    private fun findValueOfA(aStart: Long, b: Long, c: Long, program: List<Long>): Long {
        var a = aStart
        while (true) {
            val output = runProgram(a, b, c, program)
            when {
                output == program -> return a

                // If our output is too short, we try again with a double "a"
                output.size < program.size -> a *= 2

                // If our output is too long, we try again with a half "a"
                output.size > program.size -> a /= 2

                // Key Insight: every nth digit increments at every 8^n th step.
                else -> { // output.size == program.size
                    for (i in program.indices.reversed()) {
                        if (output[i] != program[i]) {
                            a += 8.0.pow(i).toLong()
                            break
                        }
                    }
                }
            }
        }

    }
}
