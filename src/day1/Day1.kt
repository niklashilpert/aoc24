package day1

import java.io.FileReader
import kotlin.math.abs

fun main() {
    val lists = loadLists()
    val leftList = lists.first.sorted()
    val rightList = lists.second.sorted()
    var sum1 = 0
    for (i in leftList.indices) {
        sum1 += abs(leftList[i] - rightList[i])
    }
    println(sum1)

    var sum2 = 0
    for (i in leftList.indices) {
        sum2 += leftList[i] * rightList.count { it == leftList[i] }
    }
    println(sum2)
}

fun loadLists(): Pair<List<Int>, List<Int>> {
    val input = FileReader("src/day1/input.txt")
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()
    input.readLines().forEach { line ->
        val parts = line.split("   ")
        leftList.add(parts[0].toInt())
        rightList.add(parts[1].toInt())
    }
    return leftList to rightList
}