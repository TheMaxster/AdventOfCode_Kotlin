package year2024.day14

import application.Day
import utils.ImportUtils
import java.util.regex.Pattern

/**
 * See https://adventofcode.com/2024/day/14
 */
class Day14 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day14/input_test_01.txt"

    override val loggingEnabled: Boolean
        get() = false


    val TEST_WIDTH: Int = 11
    val TEST_HEIGHT: Int = 7

    val WIDTH: Int = 101 // TEST_WIDTH;
    val HEIGHT: Int = 103 // TEST_HEIGHT;

    val PATTERN: Pattern = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)")

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val numberOfSeconds = 100
        val robots = parseInput(input)
        val newRobots = calculateNewPositions(robots, WIDTH, HEIGHT, numberOfSeconds)

        val medianWidth = (WIDTH - 1) / 2
        val medianHeight = (HEIGHT - 1) / 2

        val sectorResults = calculateSectors(newRobots, medianWidth, medianHeight)
        val result = sectorResults.reduce { acc, value -> acc * value }

        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val robots = parseInput(input)

        var numberOfSeconds = 0
        var maxSectorResult = 0
        var newRobots = robots

        while (maxSectorResult < robots.size / 2) {
            numberOfSeconds++
            newRobots = calculateNewPositions(robots, WIDTH, HEIGHT, numberOfSeconds)

            val medianWidth = (WIDTH - 1) / 2
            val medianHeight = (HEIGHT - 1) / 2
            maxSectorResult = calculateSectors(newRobots, medianWidth, medianHeight).maxOrNull() ?: 0
        }

        // Visual check
        val map = Array(HEIGHT) { Array(WIDTH) { "." } }
        newRobots.forEach { robot -> map[robot.y][robot.x] = "#" }
        logMap(map)

        return numberOfSeconds.toString()
    }

    private fun parseInput(input: List<String>): List<Robot> {
        return input.mapIndexedNotNull { id, line ->
            val matcher = PATTERN.matcher(line)
            if (matcher.matches()) {
                val x = matcher.group(1).toInt()
                val y = matcher.group(2).toInt()
                val dx = matcher.group(3).toInt()
                val dy = matcher.group(4).toInt()
                Robot(id + 1, x, y, dx, dy)
            } else null
        }
    }

    private fun calculateSectors(
        robots: List<Robot>,
        medianWidth: Int,
        medianHeight: Int
    ): List<Int> {
        var sector1 = 0
        var sector2 = 0
        var sector3 = 0
        var sector4 = 0

        for (newRobot in robots) {
            val x: Int = newRobot.x
            val y: Int = newRobot.y
            if (x < medianWidth && y < medianHeight) sector1++
            if (x > medianWidth && y < medianHeight) sector2++
            if (x < medianWidth && y > medianHeight) sector3++
            if (x > medianWidth && y > medianHeight) sector4++
        }

        return listOf(sector1, sector2, sector3, sector4)
    }

    private fun calculateNewPositions(
        robots: List<Robot>,
        width: Int,
        height: Int,
        seconds: Int
    ): List<Robot> {
        return robots.map { robot ->
            val newX = (robot.x + seconds * robot.dx).mod(width)
            val newY = (robot.y + seconds * robot.dy).mod(height)
            Robot(robot.id, newX, newY, robot.dx, robot.dy).also {
                log("Seconds $seconds: Id: ${it.id} -> (${it.x}, ${it.y})")
            }
        }
    }


    data class Robot(
        var id: Int,
        var x: Int,
        var y: Int,
        var dx: Int,
        var dy: Int
    )

}
