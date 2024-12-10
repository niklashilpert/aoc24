package day6

import java.awt.Dimension
import java.awt.Point
import java.io.File

operator fun Point.plus(dir: Direction) = Point(x + dir.x, y + dir.y)

class Map(private val rows: List<CharArray>) {
    private val bounds = getDimension()
    private val obstacles = getObstacles()
    private var guardPos = getGuardPosition()
    private var direction = Direction.UP

    init {
        this[guardPos.x, guardPos.y] = direction.char
    }

    fun countOccurrences(vararg chars: Char): Int {
        return rows.fold(0) { acc: Int, row: CharArray ->
            acc + row.fold(0) { rowAcc: Int, c: Char ->
                if (c in chars) {
                    rowAcc + 1
                } else {
                    rowAcc
                }
            }
        }
    }

    fun step(): Boolean {
        val potentialNextPos = guardPos + direction
        if (potentialNextPos.x !in 0..<bounds.width || potentialNextPos.y !in 0..<bounds.height) {
            return false
        } else {
            if (obstacles.contains(potentialNextPos)) {
                direction = direction.rotated()
            } else {
                guardPos += direction
                this[guardPos.x, guardPos.y] = direction.char
            }
            return true
        }
    }

    operator fun get(x: Int, y: Int) = rows[y][x]
    operator fun set(x: Int, y: Int, value: Char) {
        rows[y][x] = value
    }


    private fun getDimension(): Dimension {
        return Dimension(rows[0].size, rows.size)
    }

    private fun getGuardPosition(): Point {
        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (char == '^') {
                    return Point(x, y)
                }
            }
        }
        return Point(-1, -1)
    }

    private fun getObstacles(): List<Point> {
        val obstacles = ArrayList<Point>()
        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (char == '#') {
                    obstacles.add(Point(x, y))
                }
            }
        }
        return obstacles
    }
}

enum class Direction(val x: Int, val y: Int, val char: Char) {
    UP(0, -1, 'U'),
    LEFT(-1, 0, 'L'),
    DOWN(0, 1, 'D'),
    RIGHT(1, 0, 'R');

    fun rotated(): Direction {
        return if (this == UP) {
            RIGHT
        } else if (this == RIGHT) {
            DOWN
        } else if (this == DOWN) {
            LEFT
        } else {
            UP
        }
    }
}

fun main() {
    val input = readInput()
    val map = Map(input.map { it.toCharArray() })

    var keepLooping: Boolean
    do {
        keepLooping = map.step()
    } while(keepLooping)

    val xCount = map.countOccurrences(*Direction.entries.map { it.char }.toCharArray())
    println(xCount)
}

fun readInput(): List<String> {
    return File("src/day6/input.txt").readLines()
}
