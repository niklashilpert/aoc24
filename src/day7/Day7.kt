package day7

import java.io.File
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun main() {
    val input = File("src/day7/input.txt").readLines()

    var valueSum1 = 0L
    var valueSum2 = 0L
    input.forEach { line ->
        valueSum1 += eval(line, listOf('+', '*'))
        valueSum2 += eval(line, listOf('+', '*', '|'))
    }
    println("Part1: $valueSum1")
    println("Part2: $valueSum2")
}

/**
 * Checks whether the equation can be fulfilled with the plus and multiply operators.
 * Returns the test value if true and 0 otherwise.
 */
fun eval(equation: String, possibleOperators: List<Char>): Long {
    val (testValueString, valuesString) = equation.split(": ")
    val testValue = testValueString.toLong()
    val values = valuesString.split(" ").map { it.toLong() }
    if (testOperatorRecursive(testValue, values, mutableListOf(), possibleOperators)) {
        return testValue
    } else {
        return 0
    }
}

fun testOperatorRecursive(expectedResult: Long, values: List<Long>, operators: List<Char>, possibleOperators: List<Char>): Boolean {
    if (operators.size < values.size-1) {
        var result = false
        possibleOperators.forEach { operator ->
            result = result or testOperatorRecursive(expectedResult, values, operators + operator, possibleOperators)
        }
        return result
    } else {
        var result = values[0]
        for (i in operators.indices) {
            if (operators[i] == '+') {
                result += values[i+1]
            } else if (operators[i] == '*') {
                result *= values[i+1]
            } else {
                result *= 10.0.pow(floor(log10(values[i + 1].toDouble()))+1).toLong()
                result += values[i+1]
            }
        }
        return (expectedResult == result)
    }
}

