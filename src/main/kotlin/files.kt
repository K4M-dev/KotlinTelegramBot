package org.example

import java.io.File

data class Word(
    val original: String,
    val translate: String,
    val correctAnswersCount: Int = 0,
)

fun main() {

    fun loadDictionary(): List<Word> {

        val wordsFile = File("Words.txt")
        val dictionary: MutableList<Word> = mutableListOf()

        val lines: List<String> = wordsFile.readLines()
        for (line in lines) {
            val splitLine = line.split("|")
            val correctCount = splitLine.getOrNull(2)?.toIntOrNull() ?: 0
            val word = Word(original = splitLine[0], translate = splitLine[1], correctAnswersCount = correctCount)
            dictionary.add(word)
        }
        return dictionary
    }

    val dictionary = loadDictionary()

    while (true) {
        println(
            """
            1 - Учить слова
            2 - Статистика
            0 - Выход
        """.trimIndent()
        )

        val userAnswer = readln().toIntOrNull()
        when (userAnswer) {
            1 -> println("Выбран пункт \"Учить слова\"")
            2 -> println("Выбран пункт \"Статистика\"")
            0 -> {
                println("Выбран пункт \"Выход\"")
                break
            }

            else -> println("Введите \"1\", \"2\" или \"0\"")
        }
    }
}
