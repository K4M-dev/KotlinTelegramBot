package org.example

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun Question.asConsoleString(): String {
    val variants = this.variants
        .mapIndexed { index, word -> "${index + OPTION_FOR_INDEX} - ${word.translate}" }
        .joinToString(separator = "\n")
    return this.correctAnswer.original + "\n" + variants + "\n0 - Меню"
}

fun main() {

    val trainer = LearnWordsTrainer()

    while (true) {
        val question = trainer.getNextQuestion()

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
                if (question == null) {
                    println("Все слова из словаря выучены\n")
                    continue
                } else {
                    println("Как переводится слово ${question.correctAnswer.original}?")
                    println(question.asConsoleString())
                    val userInput = readln().toIntOrNull()
                    if (userInput == 0) {
                        println("Выбран пункт \"Меню\"")
                    } else if (userInput in 1..question.variants.size) {
                        if (trainer.checkAnswer(userInput?.minus(OPTION_FOR_INDEX))) {
                            println("Верно!\n")
                        } else println("Неправильно, ${question.correctAnswer.original} - это ${question.correctAnswer.translate}\n")
                    } else println("Пожалуйста, вводите число от 1 до ${question.variants.size}\n")
                }
            }

            2 -> {
                println("Выбран пункт \"Статистика\"")
                val statistics = trainer.getStatistics()
                println("Выучено ${statistics.learnedCount} из ${statistics.totalCount} | ${"%.0f".format(statistics.percent)}%\n")
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
const val OPTION_FOR_INDEX = 1
const val OPTION_FOR_PERCENTS = 100