package day13

import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

fun main() {
    //Button A: X+23, Y+12
    val regexA = Regex("^Button A: X\\+(\\d+), Y\\+(\\d+)$")
    //Button B: X+23, Y+12
    val regexB = Regex("^Button B: X\\+(\\d+), Y\\+(\\d+)$")
    //Prize: X=1012, Y=2079
    val regexPrize = Regex("^Prize: X=(\\d+), Y=(\\d+)$")

    var tokens1 = 0
    var tokens2 = BigDecimal(0)

    var vectorA = 0L to 0L
    var vectorB = 0L to 0L
    File("src/day13/input.txt").readLines().forEach { line ->
        var match = regexA.matchEntire(line)
        if (match != null) {
            vectorA = match.groups[1]!!.value.toLong() to match.groups[2]!!.value.toLong()
        }

        match = regexB.matchEntire(line)
        if (match != null) {
            vectorB = match.groups[1]!!.value.toLong() to match.groups[2]!!.value.toLong()
        }

        match = regexPrize.matchEntire(line)
        if (match != null) {
            val resultNumbers1 = match.groups[1]!!.value.toLong() to match.groups[2]!!.value.toLong()
            val resultNumbers2 = match.groups[1]!!.value.toLong() + 10000000000000L to match.groups[2]!!.value.toLong() + 10000000000000L
            val result1 = calculateResult1(vectorA.first, vectorB.first, vectorA.second, vectorB.second, resultNumbers1.first, resultNumbers1.second)
            val tolerance1 = 0.001

            val roundedResult1 = result1.first.roundToInt() to result1.second.roundToInt()
            if (result1.first in roundedResult1.first-tolerance1..roundedResult1.first+tolerance1 &&
                result1.second in roundedResult1.second-tolerance1..roundedResult1.second+tolerance1) {
                tokens1 += (roundedResult1.first * 3 + roundedResult1.second)
            }


            val result2 = calculateResult2(vectorA.first, vectorB.first, vectorA.second, vectorB.second, resultNumbers2.first, resultNumbers2.second)
            val tolerance2 = BigDecimal(0.001)

            val roundedResult2 = result2.first.setScale(0, RoundingMode.HALF_EVEN) to result2.second.setScale(0, RoundingMode.HALF_EVEN)

            if (roundedResult2.first in result2.first-tolerance2..result2.first+tolerance2 &&
                roundedResult2.second in result2.second-tolerance2..result2.second+tolerance2) {
                tokens2 += (roundedResult2.first * BigDecimal(3) + roundedResult2.second.abs())
            }
        }
    }
    println("Tokens: $tokens1 ${tokens2.toBigInteger()}")
}

fun calculateResult1(x1: Long, y1: Long, x2: Long, y2: Long, r1: Long, r2: Long): Pair<Double, Double> {
    val reducedY2 = y2.toDouble() - y1 * (x2.toDouble() / x1)
    val reducedR2 = r2.toDouble() - r1 * (x2.toDouble() / x1)

    val reducedR1 = r1 - reducedR2 * (y1.toDouble() / reducedY2)
    val scaledR1 = reducedR1 / x1
    val scaledR2 = reducedR2 / reducedY2
    return scaledR1 to scaledR2
}

fun calculateResult2(x1: Long, y1: Long, x2: Long, y2: Long, r1: Long, r2: Long): Pair<BigDecimal, BigDecimal> {
    val x1B = BigDecimal(x1)
    val x2B = BigDecimal(x2)
    val y1B = BigDecimal(y1)
    val y2B = BigDecimal(y2)
    val r1B = BigDecimal(r1)
    val r2B = BigDecimal(r2)

    val reducedY2 = y2B - (y1B * (x2B.divide(x1B, 20, RoundingMode.HALF_EVEN)))
    val reducedR2 = r2B - (r1B * (x2B.divide(x1B, 20, RoundingMode.HALF_EVEN)))

    val reducedR1 = r1B - (reducedR2 * (y1B.divide(reducedY2, 20, RoundingMode.HALF_EVEN)))

    val scaledR1 = reducedR1 / x1B
    val scaledR2 = reducedR2 / reducedY2
    return scaledR1 to scaledR2
}

