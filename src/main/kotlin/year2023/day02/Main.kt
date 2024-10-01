package year2023.day02

import year2023.ImportUtils

//val filePath: String = "src/main/resources/year2023/day02/input_test_01.txt"
const val filePath: String = "src/main/resources/year2023/day02/input.txt"

private enum class BallConfiguration(val maxAmount: Int) {
    RED(12),
    GREEN(13),
    BLUE(14)
}

fun main() {

    val lines: List<String> = ImportUtils.readAsList(filePath)

    // Map for saving id (key) and the game result map (value)
    val resultMap = HashMap<Int, Map<String, Int>>()
    lines.stream().forEach { line: String -> processGame(line, resultMap) }

    println("ResultMap: $resultMap")

    // Part 1:
    val validIdNumberList: List<Int> = calculatePart1(resultMap)
    println("ValidIdNumberList: $validIdNumberList")
    val result = validIdNumberList.sum()
    println("Solution Part 1: $result")


    // Part 2:
    val productList: List<Int> = calculatePart2(resultMap)
    println("ProductList: $productList")
    val result2 = productList.sum()
    println("Solution Part 2: $result2")

}

fun calculatePart1(resultMap: HashMap<Int, Map<String, Int>>): List<Int> {
    return resultMap.entries.stream()
        .filter { entry: Map.Entry<Int, Map<String, Int>> -> isGameValid(entry.value) }
        .map { it.key }
        .toList()
}

private fun isGameValid(colorMapOfGame: Map<String, Int?>): Boolean {
    for (value in BallConfiguration.entries) {
        val valueName = value.name.lowercase()
        val isGameValid = colorMapOfGame.containsKey(valueName)
                && colorMapOfGame[valueName] != null && colorMapOfGame[valueName]!! <= value.maxAmount
        if (!isGameValid) {
            return false
        }
    }
    return true
}

fun processGame(
    line: String,
    resultMap: MutableMap<Int, Map<String, Int>>
) {
    // Split the line into id and the actual game results.
    val parts = line.split(":")
    if (parts.size == 2) {
        val gameIdArray = parts[0].split("Game ")
        val gameId = gameIdArray[1].toInt()

        // println("Game " + gameId + ": ");

        // Map to save the game results of each turn.
        val maxColorMapOfGame = mutableMapOf<String, Int>()

        val games = parts[1].split(";")
        for (game in games) {

            // Map for saving of the appearance of each color for the specific turn.
            val colorMapOfTurn = processGameSection(game)

            // Save only the maximum appearance of each color in the map.
            for ((turnKey, turnValue) in colorMapOfTurn) {
                val currentMax = maxColorMapOfGame[turnKey]
                if (currentMax == null || currentMax < turnValue) {
                    maxColorMapOfGame[turnKey] = turnValue
                }
            }
        }

        resultMap[gameId] = maxColorMapOfGame

    } else {
        // We should never get here.
        println("Invalid game data format: $line")
    }
}

fun processGameSection(section: String): Map<String, Int> {
    val colors = section.trim().split(",")
    val colorMap = mutableMapOf<String, Int>()

    for (color in colors) {
        val colorInfo = color.trim().split(" ")
        if (colorInfo.size == 2) {
            val colorName = colorInfo[1].lowercase()
            val count = colorInfo[0].toInt()
            colorMap[colorName] = count
        } else {
            // We should never get here.
            println("Invalid color format: $color")
        }
    }

    // Output the color counts for the current game section
    println(colorMap)

    return colorMap
}

fun calculatePart2(resultMap: HashMap<Int, Map<String, Int>>): List<Int> {
    return resultMap.values.stream()
        .map<Int> { obj: Map<String, Int> -> getProductOfNecessaryBalls(obj) }
        .toList()
}

fun getProductOfNecessaryBalls(maxColorMapOfGame: Map<String, Int>): Int {
    return maxColorMapOfGame.values.stream()
        .mapToInt { value: Int? -> value ?: 1 }
        .reduce(1) { x: Int, y: Int -> x * y }
}
