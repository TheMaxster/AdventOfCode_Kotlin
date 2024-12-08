package year2024.day08

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
import utils.ImportUtils

/**
 * See https://adventofcode.com/2024/day/8
 */
class Day08 : Day() {

    override val loggingEnabled: Boolean
        get() = false

    val FILE_PATH: String = "src/main/resources/year2024/day08/input_test_01.txt"

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val map = ImportUtils.convertListToArray(input)
        logMap(map)

        val result = createModifiedMap(map, 1, false)
        logMap(result.newMap)

        return result.newCoords.size.toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val map = ImportUtils.convertListToArray(input)
        logMap(map)

        val result = createModifiedMap(map, 100, true) // We assume that 100 is enough to traverse the map.
        logMap(result.newMap)

        return java.lang.String.valueOf(result.newCoords.size)
    }


    private fun createModifiedMap(
        map: Array<Array<String>>,
        multiplierMaxSize: Int,
        includeAntennas: Boolean
    ): Result {
        val newCoords: MutableSet<Coordinate> = HashSet()
        val overview: Map<String, List<Coordinate>> = parseInput(map)
        val modifiedMap: Array<Array<String>> = map.copyOf()

        for ((key, coordinates) in overview) {
            val newCoordsForLetter: MutableSet<Coordinate> = HashSet()

            for (coord1 in coordinates) {
                for (coord2 in coordinates) {
                    for (multiplier in 1..multiplierMaxSize) {
                        if (coord1 === coord2) {
                            continue
                        }
                        val newCoordsTmp: List<Coordinate> = calculatePotentialCoords(coord1, coord2, multiplier)

                        for (newCoord in newCoordsTmp) {
                            if (includeAntennas || (!coord1.equals(newCoord) && !coord2.equals(newCoord))) {
                                if (AocArrayUtils.isWithinBounds(map, newCoord)) {
                                    newCoordsForLetter.add(newCoord)
                                    modifiedMap[newCoord.x][newCoord.y] = "#"
                                }
                            }
                        }
                    }
                }
            }
            log("$key: $newCoordsForLetter")
            newCoords.addAll(newCoordsForLetter)
        }
        return Result(modifiedMap, newCoords)
    }

    private fun calculatePotentialCoords(
        coord1: Coordinate,
        coord2: Coordinate,
        multiplier: Int
    ): List<Coordinate> {
        val distX: Int = multiplier * (coord1.x - coord2.x)
        val distY: Int = multiplier * (coord1.y - coord2.y)

        return java.util.List.of<Coordinate>(
            Coordinate(coord1.x + distX, coord1.y + distY),
            Coordinate(coord2.x + distX, coord2.y + distY),
            Coordinate(coord1.x - distX, coord1.y - distY),
            Coordinate(coord2.x - distX, coord2.y - distY)
        )
    }


    private fun parseInput(map: Array<Array<String>>): Map<String, List<Coordinate>> {
        return map.flatten()
            .filter { it != "." }
            .distinct()
            .associateWith { letter -> AocArrayUtils.findAllOccurences(map, letter) }
    }


    data class Result(
        var newMap: Array<Array<String>>,
        var newCoords: Set<Coordinate>
    )

}
