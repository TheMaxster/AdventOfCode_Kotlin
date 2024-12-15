package year2024.day15

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
import utils.ImportUtils
import java.util.*

/**
 * See https://adventofcode.com/2024/day/15
 */
class Day15 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day15/input_test_01.txt"
    // val FILE_PATH: String = "src/main/resources/year2024/day15/input_test_02.txt";
    // val FILE_PATH: String = "src/main/resources/year2024/day15/input_test_03.txt";


    override val loggingEnabled: Boolean
        get() = false


    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val parsedInput = parseInput(input)
        val map: Array<Array<String>> = parsedInput.first
        val instructions: List<String> = parsedInput.second

        logMap(map)
        log(instructions.toString())

        val boxes = calculate(map, instructions)
        log(boxes.toString())

        return boxes.sumOf { it.x * 100 + it.y }.toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val parsedInput = parseInput(input)
        val map: Array<Array<String>> = parsedInput.first
        val instructions: List<String> = parsedInput.second

        val scaledUpMap = scaleUpMap(map) as Array<Array<String>>
        logMap(scaledUpMap)
        log(instructions.toString())

        val boxes = calculateForPart2(scaledUpMap, instructions)
        log(boxes.toString())

        return boxes.sumOf { it.x * 100 + it.y }.toString()
    }

    private fun scaleUpMap(map: Array<Array<String>>): Array<Array<String?>> {
        val scaledUpMap = Array(map.size) { arrayOfNulls<String>(2 * map[0].size) }

        for (i in map.indices) {
            for (j in map[i].indices) {
                when (map[i][j]) {
                    "#" -> {
                        scaledUpMap[i][2 * j] = "#"
                        scaledUpMap[i][2 * j + 1] = "#"
                    }

                    "O" -> {
                        scaledUpMap[i][2 * j] = "["
                        scaledUpMap[i][2 * j + 1] = "]"
                    }

                    "." -> {
                        scaledUpMap[i][2 * j] = "."
                        scaledUpMap[i][2 * j + 1] = "."
                    }

                    "@" -> {
                        scaledUpMap[i][2 * j] = "@"
                        scaledUpMap[i][2 * j + 1] = "."
                    }
                }
            }
        }

        return scaledUpMap
    }

    private fun calculate(map: Array<Array<String>>, instructions: List<String>): List<Coordinate> {
        val boxes = AocArrayUtils.findAllOccurences(map, "O").map { listOf(it) }
        val start = AocArrayUtils.findAllOccurences(map, "@").first()
        val frame = AocArrayUtils.findAllOccurences(map, "#")

        instructions.forEach { instruction ->
            move(start, boxes, frame, parseDirection(instruction))
            if (loggingEnabled) {
                log("Instruction: $instruction")
                createAndLogMap(start, boxes, frame, map)
            }
        }

        return boxes.flatten()
    }

    private fun calculateForPart2(map: Array<Array<String>>, instructions: List<String>): List<Coordinate> {
        val boxes = AocArrayUtils.findAllOccurences(map, "[")
        val start = AocArrayUtils.findAllOccurences(map, "@").first()
        val frames = AocArrayUtils.findAllOccurences(map, "#")
        val part2Boxes = boxes.map { listOf(it, Coordinate(it.x, it.y + 1)) }

        instructions.forEach { instruction ->
            move(start, part2Boxes, frames, parseDirection(instruction))
            if (loggingEnabled) {
                log("Instruction: $instruction")
                createAndLogMap(start, part2Boxes, frames, map)
            }
        }

        return boxes
    }

    private fun parseDirection(instruction: String): Pair<Int, Int> {
        return when (instruction) {
            "<" -> 0 to -1
            ">" -> 0 to 1
            "v" -> 1 to 0
            "^" -> -1 to 0
            else -> 0 to 0
        }
    }

    private fun move(
        start: Coordinate,
        boxes: List<List<Coordinate>>,
        frames: List<Coordinate>,
        direction: Pair<Int, Int>
    ) {
        val coordsToCheck: Queue<List<Coordinate>> = ArrayDeque()
        val objectsToMove = mutableSetOf(listOf(start))
        coordsToCheck.add(listOf(start))

        while (coordsToCheck.isNotEmpty()) {
            val obj = coordsToCheck.poll()
            for (coord in obj) {
                val newX = coord.x + direction.first
                val newY = coord.y + direction.second

                if (frames.any { it.x == newX && it.y == newY }) return

                boxes.forEach { box ->
                    if (box.contains(Coordinate(newX, newY)) && objectsToMove.add(box)) {
                        coordsToCheck.add(box)
                    }
                }
            }
        }

        objectsToMove.flatten().forEach {
            it.x += direction.first
            it.y += direction.second
        }
    }

    private fun createAndLogMap(
        start: Coordinate,
        boxes: List<List<Coordinate>>,
        frame: List<Coordinate>,
        map: Array<Array<String>>
    ) {
        val tmpMap = Array(map.size) { Array(map[0].size) { "." } }

        boxes.flatten().forEach { tmpMap[it.x][it.y] = "O" }
        frame.forEach { tmpMap[it.x][it.y] = "#" }
        tmpMap[start.x][start.y] = "@"

        logMap(tmpMap)
    }

    private fun parseInput(importInput: List<String>): Pair<Array<Array<String>>, List<String>> {
        val mapLines = importInput.filter { !it.matches("[<>v^]+".toRegex()) }
        val instructions = importInput.filter { it.matches("[<>v^]+".toRegex()) }.joinToString("")

        return ImportUtils.convertListToArray(mapLines) to instructions.split("").filter { it.isNotEmpty() }
    }

}
