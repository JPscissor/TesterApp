package ru.jpscissor.frprototype.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.Answer
import ru.jpscissor.frprototype.data.Test
import ru.jpscissor.frprototype.data.calculateCorrectAnswers
import ru.jpscissor.frprototype.data.clearSelectedAnswers
import ru.jpscissor.frprototype.data.loadTestFromJson
import ru.jpscissor.frprototype.data.saveTestToJson
import ru.jpscissor.frprototype.ui.theme.FRprototypeTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestScreen(onBack: () -> Unit, test: Test, context: Context) {

    var isTestCompleted by remember { mutableStateOf(false) }

    var modifiedTest by remember { mutableStateOf(test) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val questionsCount = modifiedTest.questions.size
    val currentQuestion = modifiedTest.questions[currentQuestionIndex]

    var showCompleteDialog by remember { mutableStateOf(false) }
    var showJumpToQuestionDialog by remember { mutableStateOf(false) }

    var isFirstUnansweredFound by remember { mutableStateOf(false) }

    val firstUnansweredIndex = if (!isFirstUnansweredFound) {
        modifiedTest.questions.indexOfFirst { question ->
            question.selectedAnswerIndex.isEmpty()
        }
    } else {
        -1
    }

    @Suppress("KotlinConstantConditions")
    if (firstUnansweredIndex != -1 && !isFirstUnansweredFound) {
        currentQuestionIndex = firstUnansweredIndex
        isFirstUnansweredFound = true
    }

    var isChecked by remember { mutableStateOf(false) }

    if (isChecked) {
        @Suppress("KotlinConstantConditions")
        LaunchedEffect(isChecked) {
            delay(2000)
            isChecked = false
        }
    }

    if (!isTestCompleted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Question
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp, max = LocalConfiguration.current.screenHeightDp.dp / 2)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = currentQuestion.question,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(end = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            // Answers
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    itemsIndexed(currentQuestion.answers) { index, answer ->
                        AnswerCard(
                            answer = answer,
                            isSelected = currentQuestion.selectedAnswerIndex.contains(index),
                            isChecked = isChecked,
                            onClick = {
                                val updated = currentQuestion.selectedAnswerIndex.toMutableList().apply {
                                    if (contains(index)) remove(index) else add(index)
                                }
                                modifiedTest = modifiedTest.copy(
                                    questions = modifiedTest.questions.toMutableList().apply {
                                        this[currentQuestionIndex] =
                                            currentQuestion.copy(selectedAnswerIndex = updated)
                                    }
                                )
                            }
                        )
                    }
                }

                // CheckButton
                CheckButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    isChecked = true
                }
            }

            BottomPanel(
                currentQuestionIndex = currentQuestionIndex,
                questionsCount = modifiedTest.questions.size,
                onSaveAndNext = {
                    saveTestToJson(context, test.id, modifiedTest)
                    isChecked = false

                    if (currentQuestionIndex + 1 < questionsCount) {
                        currentQuestionIndex++
                    } else {
                        isTestCompleted = true
                    }
                },
                onComplete = { showCompleteDialog = true },
                onPrev = {
                    if (currentQuestionIndex > 0)
                        currentQuestionIndex--
                    isChecked = false
                },
                onJump = { showJumpToQuestionDialog = true }
            )
        }

        CompleteDialog(
            visible = showCompleteDialog,
            onConfirm = {
                isTestCompleted = true
            },
            onCancel = {
                showCompleteDialog = false
            }
        )

        JumpToQuestionDialog(
            visible = showJumpToQuestionDialog,
            onDismiss = { showJumpToQuestionDialog = false },
            onConfirm = { questionNumber ->
                currentQuestionIndex = questionNumber - 1
            },
            maxQuestionCount = modifiedTest.questions.size
        )
    }

    // If test completed
    else {
        var progress by remember { mutableFloatStateOf(0.0f) }
        val correctAnswers: Int = calculateCorrectAnswers(modifiedTest)

        progress = ((100 / questionsCount.toFloat()) * (correctAnswers)) * 0.01f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(top = 16.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.padding(6.dp),
                    painter = painterResource(R.drawable.complete_title),
                    contentDescription = "Test completed"
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "$correctAnswers / ${modifiedTest.questions.size}",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(20.dp))

                CustomLinearProgress(
                    progress = progress,
                    modifier = Modifier,
                    backgroundColor = Color(0xff303030),
                    progressColor = Color(0xff63a878),
                    height = 15.dp
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.points) + " " + (progress * 100).toInt(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
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
                            text = when {
                                progress < 0.6f -> stringResource(R.string.do_not_give_up)
                                progress < 0.9f -> stringResource(R.string.nice_try)
                                else -> stringResource(R.string.well_done)
                            },
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
                        contentDescription = "Snorlax",
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
}

@Composable
private fun AnswerCard(
    answer: Answer,
    isSelected: Boolean,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    val minCardSize = 70.dp

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minCardSize),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected && isChecked && answer.isCorrect -> Color(0xff8EFF92)
                isSelected && isChecked && !answer.isCorrect -> Color(0xffFF7373)
                isSelected -> MaterialTheme.colorScheme.tertiary
                isChecked && answer.isCorrect -> Color(0xff8EFF92)
                else -> MaterialTheme.colorScheme.onBackground
            },
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .heightIn(min = minCardSize),
            contentAlignment = Alignment.CenterStart
        )    {
            Text(
                text = answer.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 6.dp, top = 8.dp, bottom = 8.dp),
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onBackground
                    isChecked -> MaterialTheme.colorScheme.background
                    else -> MaterialTheme.colorScheme.tertiary
                }
            )
        }
    }
}

@Composable
private fun CheckButton(
    modifier: Modifier = Modifier,
    onCheck: () -> Unit
) {
    Surface(
        onClick = onCheck,
        modifier = modifier
            .width(85.dp)
            .height(40.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.check),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomPanel(
    currentQuestionIndex: Int,
    questionsCount: Int,
    onSaveAndNext: () -> Unit,
    onComplete: () -> Unit,
    onPrev: () -> Unit,
    onJump: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {
        // Next / complete
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .combinedClickable(
                    onClick = { if (currentQuestionIndex < questionsCount - 1) onSaveAndNext() else onComplete() },
                    onLongClick = { onComplete() }
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(40.dp),
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(if (currentQuestionIndex + 1 == questionsCount) R.string.complete else R.string.next),
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Questions navigation
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(40.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment  = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPrev) {
                    Image(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Previous question"
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = "${currentQuestionIndex + 1} / $questionsCount",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.clickable { onJump() }
                )

                Spacer(Modifier.weight(1f))

                IconButton(onClick = onSaveAndNext) {
                    Image(
                        painter = painterResource(R.drawable.arrow_forward),
                        contentDescription = "Next question"
                    )
                }
            }
        }
    }
}

@Composable
private fun CompleteDialog(
    visible: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = null,
            text = {
                Text(
                    text = stringResource(R.string.complete_question),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
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
                    onClick = onCancel,
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

@Composable
private fun JumpToQuestionDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    maxQuestionCount: Int
) {
    if (visible) {
        var inputText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(R.string.jump_to),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                TextField(
                    value = inputText,
                    onValueChange = { newValue ->
                        inputText = newValue.filter { it.isDigit() }
                    },
                    label = { Text(stringResource(R.string.enter_question_number)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val questionNumber = inputText.toIntOrNull()
                        if (questionNumber != null && questionNumber in 1..maxQuestionCount) {
                            onConfirm(questionNumber)
                        }
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            modifier = Modifier.width(IntrinsicSize.Max)
        )
    }
}

@Composable
private fun CustomLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    progressColor: Color = Color.Blue,
    height: Dp = 8.dp
) {
    val density = LocalDensity.current
    val cornerRadius = with(density) { height.toPx() / 2 }

    Canvas(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .height(height)
    ) {
        val width = size.width
        val progressWidth = width * progress.coerceIn(0f, 1f)

        drawRoundRect(
            color = backgroundColor,
            size = size,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )

        drawRoundRect(
            color = progressColor,
            size = Size(progressWidth, size.height),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TestScreenPreview() {
    FRprototypeTheme {
        val context = LocalContext.current

        val testResources = listOf(R.raw.mdk0101)
        val tests = remember {
            testResources.mapNotNull { resourceId ->
                try {
                    loadTestFromJson(context, resourceId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }

        val test = tests[0]

        TestScreen(
            onBack = {},
            test = test,
            context = context
        )
    }
}