package ru.jpscissor.frprototype.screens

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.Test
import ru.jpscissor.frprototype.data.calculateCorrectAnswers
import ru.jpscissor.frprototype.data.clearSelectedAnswers
import ru.jpscissor.frprototype.data.saveTestToJson


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestScreen(onBack: () -> Unit, test: Test, context: Context) {

    var isTestComplete by remember { mutableStateOf(false) }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var modifiedTest by remember { mutableStateOf(test) }

    val currentQuestion = modifiedTest.questions[currentQuestionIndex]

    var isChecked by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isFirstUnansweredFound by remember { mutableStateOf(false) }

    val questionsNumber = modifiedTest.questions.size

    val firstUnansweredIndex = if (!isFirstUnansweredFound) {
        modifiedTest.questions.indexOfFirst { question ->
            question.selectedAnswerIndex == null
        }
    } else {
        -1
    }

    if (firstUnansweredIndex != -1 && !isFirstUnansweredFound) {
        currentQuestionIndex = firstUnansweredIndex
        isFirstUnansweredFound = true
    }

    if (!isTestComplete) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {

                Question(
                    qn = currentQuestionIndex + 1,
                    qt = currentQuestion.question
                )

                Spacer(Modifier.weight(1f)) // 1 spacer

                //--------------------------------

                currentQuestion.answers.forEachIndexed { index, answer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .padding(vertical = 4.dp)
                            .clickable {
                                modifiedTest = modifiedTest.copy(
                                    questions = modifiedTest.questions.toMutableList().apply {
                                        this[currentQuestionIndex] = currentQuestion.copy(
                                            selectedAnswerIndex = index
                                        )
                                    }
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentQuestion.selectedAnswerIndex == index) {
                                if (isChecked) {
                                    if (answer.isCorrect) {
                                        Color(0xff8EFF92)
                                    } else {
                                        Color(0xffFF7373)
                                    }
                                } else MaterialTheme.colorScheme.tertiary
                            } else if (isChecked) {
                                if (answer.isCorrect) {
                                    Color(0xff8EFF92)
                                } else {
                                    Color(0xffFF7373)
                                }
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = answer.text,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 8.dp),
                                color = if (currentQuestion.selectedAnswerIndex == index) {
                                    MaterialTheme.colorScheme.onBackground
                                } else if (isChecked) MaterialTheme.colorScheme.background
                                else {
                                    MaterialTheme.colorScheme.tertiary
                                }
                            )
                        }
                    }
                }

                //--------------------------------

                Spacer(Modifier.height(24.dp))

                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.weight(1f))

                    Card(
                        modifier = Modifier.width(85.dp).height(35.dp).clickable {
                            isChecked = true
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painterResource(R.drawable.check),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp)
                            )

                        }
                    }
                }

                if (isChecked) {
                    LaunchedEffect(isChecked) {
                        delay(2000)
                        isChecked = false
                    }
                }

                Spacer(Modifier.weight(1f)) // 2 spacer

                //--------------------------------


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .combinedClickable (
                            onClick = {
                                if (currentQuestionIndex < modifiedTest.questions.size - 1) {
                                    saveTestToJson(context, test.id, modifiedTest)
                                    isChecked = false
                                    currentQuestionIndex++
                                }
                                else {
                                    showDialog = true
                                }
                            },
                            onLongClick = {
                                showDialog = true
                            }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(40.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (currentQuestionIndex + 1 == modifiedTest.questions.size) {
                                stringResource(R.string.complete)
                            } else {
                                stringResource(R.string.next)
                            },
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

                Spacer(Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(40.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (currentQuestionIndex > 0) {
                                    currentQuestionIndex--
                                }
                            }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_back),
                                contentDescription = ""
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            "${currentQuestionIndex + 1} / ${modifiedTest.questions.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )

                        Spacer(Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                if (currentQuestionIndex < modifiedTest.questions.size - 1) {
                                    saveTestToJson(context, test.id, modifiedTest)
                                    isChecked = false
                                    currentQuestionIndex++
                                }
                            }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_forward),
                                contentDescription = ""
                            )
                        }

                    }
                }

                //--------------------------------

            }

        }

        //dialog

        if (showDialog) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = null,
                    text = {
                        Text(
                            text = stringResource(R.string.complete_question),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                isTestComplete = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(R.string.yes))
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(R.string.no))
                        }
                    },
                    modifier = Modifier.width(IntrinsicSize.Max)
                )
            }

        }

    } //IF closed
    else {

        var progress by remember { mutableStateOf(0.0f) }

        val correctAnswers: Int = calculateCorrectAnswers(modifiedTest)

        progress = ((100 / questionsNumber.toFloat()) * (correctAnswers)) * 0.01f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(top = 16.dp, bottom = 32.dp, start = 16.dp, end = 16.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.complete_title),
                        contentDescription = ""
                    )

                    Spacer(Modifier.height(36.dp))

                    Text(
                        text = stringResource(R.string.points) + " " + (progress * 100).toInt(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = " $correctAnswers / ${modifiedTest.questions.size} ",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(20.dp))

                    CustomLinearProgress(
                        progress = progress,
                        modifier = Modifier,
                        backgroundColor = MaterialTheme.colorScheme.onSurface,
                        progressColor = MaterialTheme.colorScheme.onBackground,
                        height = 15.dp
                    )

                    Spacer(Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text =  if(progress < 0.6f) stringResource(R.string.dont_giveup)
                                else if (progress < 0.9f) stringResource(R.string.nice_try)
                                else stringResource(R.string.well_done)
                                ,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                            Text(
                                text = stringResource(R.string.click_on_me),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }


                        Image(
                            painter = painterResource(R.drawable.snorlax),
                            contentDescription = "",
                            modifier = Modifier.size(215.dp).clickable {
                                onBack()
                                modifiedTest = clearSelectedAnswers(modifiedTest)
                                saveTestToJson(context, test.id, modifiedTest)
                            }
                        )
                    }
                }

            }
        }



    } // ELSE closed

}



@Composable
fun Question(qn: Int, qt: String) {
    Row(
        modifier = Modifier.fillMaxWidth().height(120.dp)
    ) {
        Spacer(Modifier.width(12.dp))

        Text(
            text = qt,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.tertiary
        )

    }
}


@Composable
fun CustomLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    progressColor: Color = Color.Blue,
    height: Dp = 8.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val width = size.width
        val progressWidth = width * progress.coerceIn(0f, 1f)

        drawRect(
            color = backgroundColor,
            size = size
        )

        drawRect(
            color = progressColor,
            size = Size(progressWidth, size.height)
        )
    }
}



//@Composable
//fun AnswersList(answers: List<Answer>) {
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(30.dp)
//    ) {
//        itemsIndexed(answers) { index, answer ->
//            AnswerItem(
//                id = index + 1,
//                answerText = answer.text
//            )
//        }
//    }
//}
//
//@Composable
//fun AnswerItem(id: Int, answerText: String) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(75.dp)
//            .clickable {  },
//        colors = CardDefaults.cardColors(
//            containerColor =  MaterialTheme.colorScheme.onBackground,
//            contentColor = Color.White
//        ),
//        shape = RoundedCornerShape(15.dp),
//        elevation = CardDefaults.cardElevation()
//    ) {
//        Text(
//            text = "$id. $answerText",
//            modifier = Modifier.fillMaxSize().padding(10.dp),
//            fontWeight = FontWeight.SemiBold,
//            style = TextStyle(
//                fontSize = 14.sp,
//                lineHeight = 16.sp,
//                color = Color(0xff4C4C4C)
//            )
//        )
//    }
//}
