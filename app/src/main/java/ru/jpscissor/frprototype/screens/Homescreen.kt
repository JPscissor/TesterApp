package ru.jpscissor.frprototype.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.ui.theme.FRprototypeTheme

@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(top = 8.dp, bottom = 32.dp, start = 16.dp, end = 16.dp)
        ) {

            ApperPanel()

            Spacer(Modifier.height(64.dp))

            TestsList()

            Spacer(Modifier.weight(1f))

            BottomPanel()

        }

    }

}


@Composable
fun ApperPanel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.title_logo),
            contentDescription = "logo",
            modifier = Modifier.fillMaxHeight()
        )
    }
}



@Composable
fun TestsList() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(34.dp)
    ) {
        items(4) { index ->
            TestItem("Психология", 100)
        }
    }
}


@Composable
fun TestItem(title: String, quesNumber: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().height(87.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 24.sp
                )

                Text(
                    text = stringResource(R.string.questions) + ": $quesNumber",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.width(103.dp).height(46.dp),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(R.string.start),
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}



@Composable
fun BottomPanel() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(70.dp).padding(horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.shop_selected_icon),
                contentDescription = ""
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.folder_icon),
                contentDescription = ""
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.cog_icon),
                contentDescription = ""
            )
        }
    }
}


@Composable
@Preview
fun HomePreview() {
    FRprototypeTheme {
        HomeScreen()
    }
}