package ru.jpscissor.frprototype.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Answer(
    val text: String,
    val isCorrect: Boolean
)

@Serializable
data class Question(
    val question: String,
    val answers: List<Answer>,
    var selectedAnswerIndex: Int? = null
)

@Serializable
data class Test(
    val id: Int,
    val title: String,
    val questions: List<Question>
)


fun readTestFromJson( context: Context, resourceId: Int ): String {
    val inputStream = context.resources.openRawResource(resourceId)
    return inputStream.bufferedReader().use { it.readText() }
}

fun parseTestFromJson(context: Context, resourceId: Int): Test {
    val testString = readTestFromJson(context, resourceId)
    val test = Json.decodeFromString<Test>(testString)
    return test.copy(id = resourceId)
}

fun saveTestToJson(context: Context, resourceId: Int, test: Test) {
    val jsonString = Json.encodeToString(test)
    val file = File(context.filesDir, "test_$resourceId.json")
    file.writeText(jsonString)
}

fun loadTestFromJson(context: Context, resourceId: Int): Test {
    val file = File(context.filesDir, "test_$resourceId.json")
    return if (file.exists()) {
        val jsonString = file.readText()
        Json.decodeFromString<Test>(jsonString)
    } else {
        parseTestFromJson(context, resourceId)
    }
}