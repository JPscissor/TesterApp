package ru.jpscissor.frprototype.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.Question


object Globals {
    var selectedID by mutableIntStateOf(0)
    var currentQuestionIndex by mutableIntStateOf(0)
}


@Composable
fun TestScreen(onBack: () -> Unit, questions: List<Question>) {

    val currentQuestion = questions[Globals.currentQuestionIndex]
    val questionsNumber by remember { mutableIntStateOf(100) }
    val answers = currentQuestion.answers.map { it.text }

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
                qn = Globals.currentQuestionIndex + 1,
                qt = currentQuestion.question
            )

            Spacer(Modifier.weight(0.5f))

            AnswersList(answers)

            Spacer(Modifier.height(24.dp))

            Checker()

            Spacer(Modifier.weight(1f))

            NavPanel(Globals.currentQuestionIndex+1, questionsNumber, questions)

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

fun ifAnswerClick(id: Int): Boolean {
    if (id == Globals.selectedID) return true
    else return false
}


@Composable
fun AnswersList(answers: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        itemsIndexed(answers) { index, answer ->
            AnswerItem(
                id = index + 1,
                answerText = answer
            )
        }
    }
}

@Composable
fun AnswerItem(id: Int, answerText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable { if (Globals.selectedID != id) Globals.selectedID = id else Globals.selectedID = 0},
        colors = CardDefaults.cardColors(
            containerColor = if (!ifAnswerClick(id)) MaterialTheme.colorScheme.onBackground else Color.White,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = if (ifAnswerClick(id)) CardDefaults.cardElevation(12.dp) else CardDefaults.cardElevation()
    ) {
        Text(
            text = "$id. $answerText",
            modifier = Modifier.fillMaxSize().padding(10.dp),
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = if (!ifAnswerClick(id)) MaterialTheme.colorScheme.tertiary else Color(0xff4C4C4C)
            )
        )
    }
}

@Composable
fun NavPanel(num: Int, qNum: Int, questions: List<Question>) {
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
                    if (Globals.currentQuestionIndex > 0) {
                        Globals.currentQuestionIndex--
                    }
                }
            ) {
                Image(painter = painterResource(R.drawable.arrow_back), contentDescription = "")
            }

            Spacer(Modifier.weight(1f))

            Text(
                "$num / $qNum",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background
            )

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {
                    if (Globals.currentQuestionIndex < questions.size - 1) {
                        Globals.currentQuestionIndex++
                    }
                }
            ) {
                Image(painter = painterResource(R.drawable.arrow_forward), contentDescription = "")
            }

            }
        }
    }


@Composable
fun Checker() {

    Row(
        Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.weight(1f))

        Card(
            modifier = Modifier.width(85.dp).height(35.dp).clickable {  },
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


}


//@Composable
//@Preview
//fun TestPreview() {
//    FRprototypeTheme {
//        TestScreen { {}}
//    }
//}