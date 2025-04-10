package ru.jpscissor.frprototype.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.ui.theme.FRprototypeTheme

@Composable
fun TestScreen(onBack: () -> Unit) {

    var number by remember { mutableIntStateOf(value = 1) }
    var questionText by remember { mutableStateOf("Text is missing") }
    val questionsNumber by remember { mutableIntStateOf(100) }

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

            Question(number, questionText)

            Spacer(Modifier.weight(1f))

            Answers(answerText = " Ну тут какой-то отвтик типо есть, типо проверяем ")

            Spacer(Modifier.weight(1f))

            NavPanel(number, questionsNumber)

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

        Spacer(Modifier.width(8.dp))

        Text(
            text = qt,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.tertiary
        )

    }
}


@Composable
fun Answers(answerText: String) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(34.dp)
    ) {
        items(4) { index ->
            AnswerItem(answerText)
        }
    }

}

@Composable
fun AnswerItem(answerText: String) {
    Card(
        modifier = Modifier.fillMaxWidth().height(75.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Text(
            text = answerText,
            modifier = Modifier.fillMaxSize().padding(8.dp),
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        )
    }
}

@Composable
fun NavPanel(num: Int, qNum: Int) {
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
                onClick = {}
            ) { Image(painter = painterResource(R.drawable.arrow_back), contentDescription = "") }

            Spacer(Modifier.weight(1f))

            Text(
                "$num / $qNum",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background
            )

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {}
            ) { Image(painter = painterResource(R.drawable.arrow_forward), contentDescription = "") }

            }
        }
    }


@Composable
@Preview
fun TestPreview() {
    FRprototypeTheme {
        TestScreen { {} }
    }
}