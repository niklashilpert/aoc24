package day6

import java.awt.Dimension
import java.awt.Point
import java.io.File

operator fun Point.plus(dir: Direction) = Point(x + dir.x, y + dir.y)
operator fun Point.minus(dir: Direction) = Point(x - dir.x, y - dir.y)

class Map(private val rows: List<CharArray>) {
    private val dimension = readDimension()
    val obstacles = readObstacles()
    val initialGuardPosition = readGuardPosition()
    val initialDirection = Direction.UP

    private fun createStepsMap(): HashMap<Direction, MutableList<Point>> {
        return hashMapOf<Direction, MutableList<Point>>().also {
            Direction.entries.forEach { entry ->
                it[entry] = mutableListOf()
            }
        }
    }

    fun countPossibleLoops(): Int {
        var loopCount = 0
        val positions = getVisitedCellsUntilMapExit(initialGuardPosition, initialDirection, obstacles)!!.distinct()
        for (position in positions) {
                val stepsUntilMapExit = getVisitedCellsUntilMapExit(
                    initialGuardPosition,
                    initialDirection,
                    obstacles + position
                )
                if (stepsUntilMapExit == null) {
                    loopCount += 1
                }
        }
        return loopCount
    }

    fun getVisitedCellsUntilMapExit(startPosition: Point, startDirection: Direction, obstacles: Set<Point>): Set<Point>? {
        val stepsMap = createStepsMap()
        var currentPosition = startPosition
        var currentDirection = startDirection
        val pointList = mutableSetOf<Point>()
        while(isInMap(currentPosition)) {
            val nextPosition = currentPosition + currentDirection
            val positionWasVisitedTwice =  stepsMap[currentDirection]!!.contains(nextPosition)
            if (positionWasVisitedTwice) {
                return null
            }
            if (nextPosition in obstacles) {
                currentDirection = currentDirection.rotatedRight()
            } else {
                pointList += currentPosition
                currentPosition += currentDirection
            }

            stepsMap[currentDirection]!!.add(currentPosition)

        }
        return pointList
    }

    private fun isInMap(position: Point): Boolean {
        return position.x in 0..<dimension.width && position.y in 0..<dimension.height
    }

    operator fun get(pos: Point) = this[pos.x, pos.y]
    operator fun get(x: Int, y: Int) = rows[y][x]
    operator fun set(pos: Point, value: Char) { this[pos.x, pos.y] = value }
    operator fun set(x: Int, y: Int, value: Char) { rows[y][x] = value }

    private fun readDimension(): Dimension {
        return Dimension(rows[0].size, rows.size)
    }

    private fun readGuardPosition(): Point {
        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (char == '^') {
                    return Point(x, y)
                }
            }
        }
        return Point(-1, -1)
    }

    private fun readObstacles(): Set<Point> {
        val obstacles = mutableSetOf<Point>()
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

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    LEFT(-1, 0),
    DOWN(0, 1),
    RIGHT(1, 0);

    fun rotatedRight(): Direction {
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
    val map = Map(readInput().map { it.toCharArray() })

    val visitedCells = map.getVisitedCellsUntilMapExit(map.initialGuardPosition, map.initialDirection, map.obstacles)!!.size
    println(visitedCells)
    val loopCount = map.countPossibleLoops()
    println(loopCount)
}

fun readInput(): List<String> {
    return File("src/day6/input.txt").readLines()
}
