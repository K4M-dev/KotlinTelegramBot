package org.example

import java.io.File

data class Statistics(
    val totalCount: Int,
    val learnedWords: List<Word>,
    val learnedCount: Int,
    val percent: Double,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordsTrainer {

    private var question: Question? = null
    private var dictionary = loadDictionary()

    private fun loadDictionary(): List<Word> {

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
        val learnedWords = dictionary.filter { it.correctAnswersCount >= NOT_CORRECT_ANSWERS_COUNT }
        val learnedCount = learnedWords.size
        val percent = (learnedCount.toDouble() / totalCount) * OPTION_FOR_PERCENTS

        return Statistics(totalCount, learnedWords, learnedCount, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < NOT_CORRECT_ANSWERS_COUNT }
        if (notLearnedList.isEmpty()) return null
        val shuffledWords = notLearnedList.shuffled()
        val questionWords = shuffledWords.take(VARIANTS_COUNT).shuffled()
        val correctAnswer = questionWords.random()
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
