package ru.jpscissor.frprototype.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.parsePersonFromJson

@Composable
fun SampleScreen() {

    val person = parsePersonFromJson(LocalContext.current, R.raw.check)

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
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = " Привет, меня зовут ${person.name} ",
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }


    }
}