package org.example

import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

data class Statistics(
    val totalCount: Int,
    val learnedCount: Int,
    val percent: Double,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordsTrainer(
    private val notCorrectAnswersCount: Int = 3,
    private val variantsCount: Int = 4,
) {

    private var question: Question? = null
    private var dictionary = loadDictionary()

    private fun loadDictionary(): List<Word> {
        try {
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
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException("Некорректный файл")
        }
    }

    private fun saveDictionary(dictionary: List<Word>) {
        val wordsFile = File("Words.txt")
        wordsFile.printWriter().use { out ->
            dictionary.forEach { word ->
                out.println("${word.original}|${word.translate}|${word.correctAnswersCount}")
            }
        }
    }

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedWords = dictionary.filter { it.correctAnswersCount >= notCorrectAnswersCount }
        val learnedCount = learnedWords.size
        val percent = (learnedCount.toDouble() / totalCount) * OPTION_FOR_PERCENTS

        return Statistics(totalCount, learnedCount, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < notCorrectAnswersCount }
        if (notLearnedList.isEmpty()) return null

        val needed = variantsCount
        val notLearnedSample = notLearnedList.shuffled().take(needed)
        val learnedSample = dictionary.filter { it.correctAnswersCount >= notCorrectAnswersCount }
            .shuffled()
            .take((needed - notLearnedSample.size).coerceAtLeast(0))

        val questionWords = (notLearnedSample + learnedSample).shuffled()
        val correctAnswer = notLearnedSample.random()
        question = Question(
            variants = questionWords,
            correctAnswer = correctAnswer,
        )
        return question
    }

    fun checkAnswer(userAnswerId: Int?): Boolean {
        return question?.let {
            val correctAnswerId = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswerId) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }
}
