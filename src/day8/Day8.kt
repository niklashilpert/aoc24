package day8

import java.awt.Dimension
import java.awt.Point
import java.io.File

fun main() {
    val map = Map(File("src/day8/input.txt").readLines().map { it.toCharArray() })
    println(map.getAntinodeCount())
    println(map.getAntinodeCountWithResonance())
}

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)

class Map(private val rows: List<CharArray>) {
    private val bounds = getDimension()
    private val antennas = hashMapOf<Char, List<Point>>()

    init {
        for(char in getUniqueChars()) {
            if (char != '.') {
                antennas[char] = findOccurrences(char)
            }
        }
    }

    fun getAntinodeCount(): Int {
        return antennas.keys.fold(setOf()) { acc: Set<Point>, char: Char ->
            acc + getNeighboringAntinodes(char)
        }.size
    }

    fun getAntinodeCountWithResonance(): Int {
        return antennas.keys.fold(setOf()) { acc: Set<Point>, char: Char ->
            acc + getAntinodesWithResonance(char)
        }.size
    }

    private fun getNeighboringAntinodes(char: Char): Set<Point> {
        val antinodeSet = mutableSetOf<Point>()
        if (char in antennas.keys) {
            for (antenna1 in antennas[char]!!) {
                for (antenna2 in antennas[char]!!) {
                    if (antenna1 != antenna2) {
                        val direction = Point(antenna1.x - antenna2.x, antenna1.y - antenna2.y)
                        val potentialAntinodes = setOf(
                            antenna1 + direction,
                            antenna1 - direction,
                            antenna2 + direction,
                            antenna2 - direction
                        )
                        potentialAntinodes.forEach { antinode ->
                            if (!isOutOfBounds(antinode) && antinode !in listOf(antenna1, antenna2)) {
                                antinodeSet.add(antinode)
                            }
                        }
                    }
                }
            }
        }
        return antinodeSet
    }

    private fun getAntinodesWithResonance(char: Char): Set<Point> {
        val antinodeSet = mutableSetOf<Point>()
        if (char in antennas.keys) {
            for (antenna1 in antennas[char]!!) {
                for (antenna2 in antennas[char]!!) {
                    if (antenna1 != antenna2) {
                        val direction = Point(antenna1.x - antenna2.x, antenna1.y - antenna2.y)
                        var tempPos = antenna1
                        antinodeSet.addAll(listOf(antenna1, antenna2))
                        while(!isOutOfBounds(tempPos + direction)) {
                            tempPos += direction
                            antinodeSet.add(tempPos)
                        }

                        tempPos = antenna2
                        while(!isOutOfBounds(tempPos - direction)) {
                            tempPos -= direction
                            antinodeSet.add(tempPos)
                        }
                    }
                }
            }
        }
        return antinodeSet
    }

    private fun getUniqueChars(): List<Char> {
        val chars = mutableListOf<Char>()
        rows.forEach {row: CharArray ->
            row.forEach {c: Char ->
                if (c !in chars) {
                    chars.add(c)
                }
            }
        }
        return chars
    }

    private fun findOccurrences(char: Char): List<Point> {
        val occurrences = mutableListOf<Point>()
        rows.forEachIndexed {y: Int, row: CharArray ->
            row.forEachIndexed {x: Int, c: Char ->
                if (char == c) {
                    occurrences.add(Point(x, y))
                }
            }
        }
        return occurrences
    }

    operator fun get(pos: Point) = this[pos.x, pos.y]
    operator fun get(x: Int, y: Int) = rows[y][x]
    operator fun set(x: Int, y: Int, value: Char) {
        rows[y][x] = value
    }

    private fun isOutOfBounds(pos: Point): Boolean {
        return pos.x !in 0..<bounds.width || pos.y !in 0..<bounds.height
    }


    private fun getDimension(): Dimension {
        return Dimension(rows[0].size, rows.size)
    }
}