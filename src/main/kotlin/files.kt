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
            1 -> {
                println("Выбран пункт \"Учить слова\"")
                val notLearnedList = dictionary.filter { it.correctAnswersCount < NOT_CORRECT_ANSWERS_COUNT }
                if (notLearnedList.isEmpty()) {
                    println("Все слова из словаря выучены\n")
                    continue
                }
                val shuffledWords = notLearnedList.shuffled()
                val questionWords = shuffledWords.take(VARIANTS_COUNT)
                val correctAnswer = questionWords.random()
                val options = questionWords.shuffled()
                println("Как переводится слово ${correctAnswer.original}?")
                options.forEachIndexed { index, word ->
                    println("${index + 1} - ${word.translate}")
                }
                val userInput = readln().toIntOrNull()
                if (userInput != null && userInput in 1..options.size) {
                    if (options[userInput - 1] == correctAnswer) {
                        println("Верно!\n")
                    } else println("К сожалению, это не так...\n")
                } else println("Пожалуйста, вводите число от 1 до ${options.size}\n")
            }

            2 -> {
                println("Выбран пункт \"Статистика\"")
                val totalCount = dictionary.size
                val learnedWords = dictionary.filter { it.correctAnswersCount >= NOT_CORRECT_ANSWERS_COUNT }
                val learnedCount = learnedWords.size
                val percent = (learnedCount.toDouble() / totalCount) * 100
                println("Выучено $learnedCount из $totalCount | ${"%.0f".format(percent)}%\n")
            }

            0 -> {
                println("Выбран пункт \"Выход\"")
                break
            }

            else -> println("Введите \"1\", \"2\" или \"0\"")
        }
    }
}

const val NOT_CORRECT_ANSWERS_COUNT = 3
const val VARIANTS_COUNT = 4