package org.example

fun Question.asConsoleString(): String {
    val variants = this.variants
        .mapIndexed { index, word -> "${index + OPTION_FOR_INDEX} - ${word.translate}" }
        .joinToString(separator = "\n")
    return this.correctAnswer.original + "\n" + variants + "\n\n0 - Меню"
}

fun main() {

    val trainer = try {
        LearnWordsTrainer()
    } catch (e: Exception) {
        println("Невозможно загрузить словарь")
        return
    }

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
                while (true) {
                    val question = trainer.getNextQuestion()
                if (question == null) {
                    println("Все слова из словаря выучены\n")
                    break
                } else {
                    println("Как переводится слово ${question.correctAnswer.original}?")
                    println(question.asConsoleString())
                    val userInput = readln().toIntOrNull()
                    if (userInput == 0) {
                        println("Выбран пункт \"Меню\"")
                        break
                    } else if (userInput in 1..question.variants.size) {
                        if (trainer.checkAnswer(userInput?.minus(OPTION_FOR_INDEX))) {
                            println("Верно!\n")
                        } else println("Неправильно, ${question.correctAnswer.original} - это ${question.correctAnswer.translate}\n")
                    } else println("Пожалуйста, вводите число от 1 до ${question.variants.size}\n")
                }
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

const val OPTION_FOR_INDEX = 1
const val OPTION_FOR_PERCENTS = 100