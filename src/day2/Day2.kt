package day2

import java.io.FileReader
import kotlin.math.abs

fun main() {
    val reports = loadReports()
    println(countSafeReports1(reports))
    println(countSafeReports2(reports))
}

fun loadReports(): List<List<Int>> {
    val reportList = mutableListOf<MutableList<Int>>()
    val input = FileReader("src/day2/input.txt")
    input.readLines().forEach { line ->
        reportList += line.split(" ").map { it.toInt() }.toMutableList()
    }
    return reportList
}

fun countSafeReports1(reports: List<List<Int>>): Int {
    val sum = reports.fold(0) {sum, levels ->
        sum + if (isSafeReport(levels)) { 1 } else { 0 }
    }
    return sum
}

fun countSafeReports2(reports: List<List<Int>>): Int {
    val sum = reports.fold(0) {sum, levels ->
        var isSafe = isSafeReport(levels)
        for (ignoreIndex in levels.indices) {
            isSafe = isSafe or isSafeReport(levels.filterIndexed { index, _ -> ignoreIndex != index })
        }
        sum + if (isSafe) { 1 } else { 0 }
    }
    return sum
}

fun isSafeReport(levels: List<Int>): Boolean {
    var isAscendingSafe = true
    var isDescendingSafe = true
    for (i in 0..<levels.size-1) {
        if (abs(levels[i] - levels[i+1]) in 1..3) {
            if (levels[i] >= levels[i+1]) {
                isAscendingSafe = false
            } else if (levels[i] <= levels[i+1]) {
                isDescendingSafe = false
            }
        } else {
            isAscendingSafe = false
            isDescendingSafe = false
        }
    }
    return isAscendingSafe || isDescendingSafe
}