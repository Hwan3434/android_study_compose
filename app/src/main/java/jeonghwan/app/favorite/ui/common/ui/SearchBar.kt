package jeonghwan.app.favorite.ui.common.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jeonghwan.app.favorite.R
import jeonghwan.app.favorite.ui.screen.search.SearchUiState
import kotlinx.coroutines.time.delay
import java.time.Duration

@Composable
fun SearchBar(
    text: String,
    onValueChange: (String) -> Unit
) {
    DurationTextField(
        value = text,
        onDurationValueChange = onValueChange,
    )
}


@Composable
fun DurationTextField(
    value: String,
    onDurationValueChange: (String) -> Unit,
    duration: Long = 500L,
    placeholder: @Composable (() -> Unit)? = null
) {
    var currentText by remember { mutableStateOf(value) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = currentText,
        onValueChange = { newText ->
            currentText = newText
        },
        placeholder = placeholder,
    )

    LaunchedEffect(currentText) {
        delay(Duration.ofMillis(duration))
        onDurationValueChange(currentText)
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar(
        text = "hello",
        onValueChange = { value ->
            // 디버깅이나 로그에 출력
            println("Debounced value: $value")
        }
    )
}