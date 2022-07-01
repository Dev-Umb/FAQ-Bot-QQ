package umb.ink.ktor.service

import umb.ink.ktor.data.model.Question

interface QAService {
    fun findQuestionByName(name: String): Question

    fun findQuestionById(id: Int): Question

    fun findQuestionByKey(key: String): List<Question>
}