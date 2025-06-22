package org.example

import java.io.File

data class Word(
    val original: String,
    val translate: String,
    val correctAnswersCount: Int = 0,
)

fun main() {

    val wordsFile = File("Words.txt")
    val dictionary: MutableList<Word> = mutableListOf()

    val lines: List<String> = wordsFile.readLines()
    for (line in lines) {
        val splitLine = line.split("|")
        val correctCount = splitLine.getOrNull(2)?.toIntOrNull() ?: 0
        val word = Word(original = splitLine[0], translate = splitLine[1], correctAnswersCount = correctCount)
        dictionary.add(word)
    }
    println(dictionary)
}