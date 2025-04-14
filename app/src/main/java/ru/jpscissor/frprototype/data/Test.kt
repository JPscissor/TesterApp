package ru.jpscissor.frprototype.data

import android.content.Context
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Answer(
    val text: String,
    val isCorrect: Boolean
)

@Serializable
data class Question(
    val question: String,
    val answers: List<Answer>
)

@Serializable
data class Test(
    val title: String,
    val questions: List<Question>
)


fun readTestFromJson( context: Context, resourceId: Int ): String {
    val inputStream = context.resources.openRawResource(resourceId)
    return inputStream.bufferedReader().use { it.readText() }
}

fun parseTestFromJson( context: Context, resourceId: Int): Test {
    val testString = readTestFromJson(context, resourceId)
    return Json.decodeFromString<Test>(testString)
}
