package day3

import java.io.FileReader

fun main() {
    val instructionMess = loadFile()
    println(addMultiplications(instructionMess))

    var instructionMess2 = instructionMess
        .replace("\n", "")

    while(true) {
        val start = instructionMess2.indexOf("don't()")
        var end = instructionMess2.indexOf("do()", start)
        if (end == -1) {
            end = instructionMess2.length
        }
        if (start == -1) {
            break
        }

        val strToReplace = instructionMess2.substring(start, end+4)
        println(strToReplace)
        instructionMess2 = instructionMess2.replace(strToReplace, "")
    }

    println(addMultiplications(instructionMess2))
}

fun loadFile(): String {
    return FileReader("src/day3/input.txt").readText()
}

fun addMultiplications(instructions: String): Int {
    val regex = Regex("mul\\((\\d?\\d?\\d),(\\d?\\d?\\d)\\)")
    val result = regex.findAll(instructions)
    var sum = 0
    for (match in result) {
        val x = Integer.parseInt(match.groupValues[1])
        val y = Integer.parseInt(match.groupValues[2])
        sum += x * y
    }
    return sum
}