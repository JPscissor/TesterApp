package ru.jpscissor.frprototype.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.Test
import ru.jpscissor.frprototype.data.saveTestToJson


@Composable
fun TestScreen(onBack: () -> Unit, test: Test, context: Context) {

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var modifiedTest by remember { mutableStateOf(test) }

    val currentQuestion = modifiedTest.questions[currentQuestionIndex]

    var isChecked by remember { mutableStateOf(false) }

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

            Question(
                qn = currentQuestionIndex + 1,
                qt = currentQuestion.question
            )

            Spacer(Modifier.weight(0.5f))

            //--------------------------------

            currentQuestion.answers.forEachIndexed { index, answer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
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
                        containerColor = if (currentQuestion.selectedAnswerIndex == index) { if (isChecked) {
                            if ( answer.isCorrect ){Color.Green}
                            else {Color.Red} }
                        else MaterialTheme.colorScheme.tertiary }
                        else if (isChecked) { if ( answer.isCorrect ){Color.Green} else {Color.Red} }
                        else { MaterialTheme.colorScheme.onBackground }
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
                            color = if (currentQuestion.selectedAnswerIndex == index) { MaterialTheme.colorScheme.onBackground }
                            else if (isChecked) MaterialTheme.colorScheme.background
                            else { MaterialTheme.colorScheme.tertiary }
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

            Spacer(Modifier.weight(1f))

            //--------------------------------

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickable {
                        if (currentQuestionIndex < modifiedTest.questions.size - 1) {
                            saveTestToJson(context, test.id, modifiedTest)
                            isChecked = false
                            currentQuestionIndex++
                        }
                    },
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
                        text = if (currentQuestionIndex == modifiedTest.questions.size) {stringResource(R.string.complete)}
                        else { stringResource(R.string.next) },
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
                        Image(painter = painterResource(R.drawable.arrow_back), contentDescription = "")
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
                        Image(painter = painterResource(R.drawable.arrow_forward), contentDescription = "")
                    }

                }
            }

            //--------------------------------

        }

    }

}


@Composable
fun Question(qn: Int, qt: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.size(38.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(5.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$qn",
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }

        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = qt,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.tertiary
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
