package ru.jpscissor.frprototype.data

data class Answer(
    val text: String,
    val isCorrect: Boolean
)

data class Question(
    val question: String,
    val answers: List<Answer>
)

data class Test(
    val title: String,
    val questions: List<Question>
)

fun getQuestions(): List<Question> {
    return listOf(
        Question(
            question = "Процесс установления и развития контактов между людьми, порождаемый потребностями совместной деятельности и включающий в себя обмен информацией.",
            answers = listOf(
                Answer("деятельность", false),
                Answer("самосознание", false),
                Answer("общение", true)
            )
        ),
        Question(
            question = "Функция общения, являющаяся формой существования и проявления творческой сущности человека.",
            answers = listOf(
                Answer("Личностно-формирующая", false),
                Answer("Креативная", true),
                Answer("Инструментальная", false)
            )
        ),
        Question(
            question = "Как называется сторона структуры общения, которая заключает в себе восприятие людьми друг друга.",
            answers = listOf(
                Answer("коммуникативная", false),
                Answer("интерактивная", false),
                Answer("перцептивная", true)
            )
        )
    )
}
