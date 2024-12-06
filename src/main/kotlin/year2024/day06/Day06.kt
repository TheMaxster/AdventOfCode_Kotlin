package main.kotlin.year2024.day06

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
import utils.ImportUtils

/**
 * See https://adventofcode.com/2024/day/6
 */
class Day06 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day06/input_test_01.txt"

    override val loggingEnabled: Boolean
        get() = true

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val matrix = ImportUtils.convertListToArray(importInput)

        val startingPoint: Coordinate = AocArrayUtils.findAllOccurences(matrix, "^").first()
        log("Starting point: $startingPoint")

        val result = getPointsVisited(matrix, startingPoint)
        return result.pointsVisited.size.toString()
    }

    private fun getPointsVisited(
        matrix: Array<Array<String>>,
        startingPoint: Coordinate
    ): PathFindingResult {
        var reachedEnd = false
        var currentPoint: Coordinate = startingPoint
        var direction = Direction.NORTH
        val pointsVisited: MutableSet<Coordinate> = HashSet()
        pointsVisited.add(startingPoint)
        var stepsMade = 0
        var loopFound = false

        while (!reachedEnd) {

            // That is an assumption we make for part 2!
            if (stepsMade >= 2 * pointsVisited.size) {
                loopFound = true
                break
            }

            val nextPoint: Coordinate = nextCoordinate(currentPoint, direction)
            reachedEnd = !nextPointReachable(nextPoint, matrix)
            if (reachedEnd) {
                pointsVisited.add(currentPoint)
                stepsMade++
                continue
            }

            val nextPointString = matrix[nextPoint.x][nextPoint.y]
            if (nextPointString == "#") {
                direction = getNextDirection(direction)
            } else {
                pointsVisited.add(currentPoint)
                stepsMade++
                currentPoint = nextPoint
            }
        }
        return PathFindingResult(pointsVisited, loopFound)
    }

    private fun nextCoordinate(
        coord: Coordinate,
        dir: Direction
    ): Coordinate {
        return when (dir) {
            Direction.NORTH -> Coordinate(coord.x - 1, coord.y)
            Direction.EAST -> Coordinate(coord.x, coord.y + 1)
            Direction.SOUTH -> Coordinate(coord.x + 1, coord.y)
            Direction.WEST -> Coordinate(coord.x, coord.y - 1)
        }
    }

    private fun nextPointReachable(
        nextPoint: Coordinate,
        matrix: Array<Array<String>>
    ): Boolean {
        val matrixLength = matrix.size
        val matrixWidth = matrix[0].size
        return nextPoint.x in 0..<matrixWidth && nextPoint.y in 0 ..<matrixLength
    }

    private fun getNextDirection(direction: Direction): Direction {
        return when (direction) {
            Direction.NORTH -> Direction.EAST
            Direction.EAST -> Direction.SOUTH
            Direction.SOUTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
        }
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val matrix = ImportUtils.convertListToArray(input)

        val startingPoint: Coordinate = AocArrayUtils.findAllOccurences(matrix, "^").first()
        log("Starting point: $startingPoint")

        val result = getPointsVisited(matrix, startingPoint)
        val foundLoops = result.pointsVisited
            .filterNot { it == startingPoint }
            .count { coord ->
                val modifiedMatrix = matrix.map { it.copyOf() }.toTypedArray()
                modifiedMatrix[coord.x][coord.y] = "#"
                getPointsVisited(modifiedMatrix, startingPoint).loopFound
            }

        return foundLoops.toString()
    }

    private enum class Direction {
        NORTH, SOUTH, EAST, WEST
    }

    data class PathFindingResult(
        var pointsVisited: MutableSet<Coordinate>,
        var loopFound: Boolean
    )

}
