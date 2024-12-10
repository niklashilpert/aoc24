package day4

import java.io.File

class WordSearch(val lines: List<String>) {
    private val height = lines.size
    private val width = lines[0].length

    operator fun get(x: Int, y: Int): Char {
        return lines[y][x]
    }

    private fun countStraightWords(word: String, searchWidth: Int, searchHeight: Int, swapXY: Boolean): Int {
        var wordCount = 0
        for (y in 0..<searchHeight) {
            var indexInWord = 0
            for (x in 0..<searchWidth) {
                val char = if (swapXY) { this[y,x] } else { this[x,y] }

                if (char == word[indexInWord]) {
                    indexInWord++
                } else {
                    indexInWord = 0
                    if (char == word[indexInWord]) {
                        indexInWord++
                    }
                }
                if (indexInWord == word.length) {
                    wordCount++
                    indexInWord = 0
                }
            }
        }
        println("Straight: $wordCount")
        return wordCount
    }

    private fun countDiagonalWords(word: String, searchWidth: Int, searchHeight: Int, searchDown: Boolean): Int {
        var wordCount = 0
        val yDirection = if (searchDown) { 1 } else { -1 }
        val lastX = searchWidth - word.length
        val firstY = if (searchDown) { 0 } else { word.length - 1 }
        val lastY = if (searchDown) { searchHeight - word.length } else { searchHeight - 1 }

        for (y in firstY..lastY) {
            for (x in 0..lastX) {
                var charsOfWordFound = 0
                for (i in word.indices) {
                    val directionalI = i * yDirection
                    if (this[x + i, y + directionalI] == word[i]) {
                        charsOfWordFound++
                    }
                }
                if (charsOfWordFound == word.length) {
                    wordCount++
                }
            }
        }
        println("Diagonal: $wordCount")
        return wordCount
    }

    fun countWordOccurrances(word: String): Int {
        val reversedWord = word.reversed()
        var sum = 0
        sum += countStraightWords(word, width, height, false)
        sum += countStraightWords(word, height, width, true)
        sum += countStraightWords(reversedWord, width, height, false)
        sum += countStraightWords(reversedWord, height, width, true)
        sum += countDiagonalWords(word, width, height, true)
        sum += countDiagonalWords(word, width, height, false)
        sum += countDiagonalWords(reversedWord, width, height, true)
        sum += countDiagonalWords(reversedWord, width, height, false)
        return sum
    }

    fun countXMaxes(): Int {
        var count = 0
        for (x in 0..width-3) {
            for (y in 0..height-3) {
                if (isXMas(x, y, 'M', 'M', 'S', 'S', 'A') ||
                    isXMas(x, y, 'M', 'S', 'M', 'S', 'A') ||
                    isXMas(x, y, 'S', 'M', 'S', 'M', 'A') ||
                    isXMas(x, y, 'S', 'S', 'M', 'M', 'A')) {
                    count++
                }
            }
        }
        return count
    }

    fun isXMas(x: Int, y: Int, tl: Char, tr: Char, bl: Char, br: Char, center: Char): Boolean {
        return this[x,y] == tl && this[x+2,y] == tr && this[x+1,y+1] == center && this[x,y+2] == bl && this[x+2,y+2] == br
    }
}

fun main() {
    val search = readWordSearch()
    println(search.countWordOccurrances("XMAS"))
    println(search.countXMaxes())
}


fun readWordSearch(): WordSearch {
    val lines = File("src/day4/input.txt").readLines()
    return WordSearch(lines)
}