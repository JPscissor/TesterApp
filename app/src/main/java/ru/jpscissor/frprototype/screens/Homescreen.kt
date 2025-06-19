package ru.jpscissor.frprototype.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.Test
import ru.jpscissor.frprototype.data.loadTestFromJson
import ru.jpscissor.frprototype.ui.theme.FRprototypeTheme

@Composable
fun HomeScreen(context: Context, onNavigateToTest: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val testResources = listOf(R.raw.sample, R.raw.mdk0501, R.raw.mdk0101209, R.raw.mdk0104)

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

        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        ) {
            UpperPanel()
            Spacer(Modifier.height(40.dp))
            TestsList(tests, onNavigateToTest)
        }

        BottomPanel(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun UpperPanel() {
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
private fun TestsList(tests: List<Test>, navigate: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(tests) { test ->
            TestItem(
                title = test.title,
                quesNumber = test.questions.size,
                navigate = { navigate(test.id) }
            )
        }

        item{
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TestItem(title: String, quesNumber: Int, navigate: () -> Unit) {
    Card(
        onClick = navigate,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.questions) + ": $quesNumber",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun BottomPanel(modifier: Modifier = Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(40.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.shop_selected_icon),
                    contentDescription = null
                )
                Spacer(Modifier.weight(1f))
                val context = LocalContext.current
                Image(
                    painter = painterResource(R.drawable.folder_icon),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, R.string.coming_soon, Toast.LENGTH_SHORT).show()
                    },
                    contentDescription = null
                )
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.cog_icon),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, R.string.coming_soon, Toast.LENGTH_SHORT).show()
                    },
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview
fun HomePreview() {
    FRprototypeTheme {
        HomeScreen( LocalContext.current ) {}
    }
}