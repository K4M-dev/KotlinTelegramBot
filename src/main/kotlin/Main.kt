package org.example

import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

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

var dictionary = loadDictionary()

fun main() {

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
                val correctAnswerId = options.indexOf(correctAnswer) + OPTION_FOR_INDEX
                println("Как переводится слово ${correctAnswer.original}?")
                options.forEachIndexed { index, word ->
                    println("${index + 1} - ${word.translate}")
                }
                println(
                    """
                    ----------
                    0 - Меню
                """.trimIndent()
                )
                val userInput = readln().toIntOrNull()
                if (userInput == 0) {
                    println("Выбран пункт \"Меню\"")
                } else if (userInput != null && userInput in 1..options.size) {
                    if (userInput == correctAnswerId) {
                        println("Верно!\n")
                        correctAnswer.correctAnswersCount++
                        saveDictionary(dictionary)
                    } else println("Неправильно, ${correctAnswer.original} - это ${correctAnswer.translate}\n")
                } else println("Пожалуйста, вводите число от 1 до ${options.size}\n")
            }

            2 -> {
                println("Выбран пункт \"Статистика\"")
                val totalCount = dictionary.size
                val learnedWords = dictionary.filter { it.correctAnswersCount >= NOT_CORRECT_ANSWERS_COUNT }
                val learnedCount = learnedWords.size
                val percent = (learnedCount.toDouble() / totalCount) * OPTION_FOR_PERCENTS
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

fun saveDictionary(dictionary: List<Word>) {
    val wordsFile = File("Words.txt")
    wordsFile.printWriter().use { out ->
        dictionary.forEach { word ->
            out.println("${word.original}|${word.translate}|${word.correctAnswersCount}")
        }
    }
}

const val NOT_CORRECT_ANSWERS_COUNT = 3
const val VARIANTS_COUNT = 4
const val OPTION_FOR_INDEX = 1
const val OPTION_FOR_PERCENTS = 100