package jeonghwan.app.favorite.ui.common.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "검색어를 입력하세요.",
    duration: Long = 1000L,
    onDebounce: (String) -> Unit
) {
    // TextField를 위한 상태
    var text by rememberSaveable { mutableStateOf("") }

    // TextField 구성
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text(hint) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    )

    // 텍스트 입력이 2초 동안 중단되면 onDebounce 호출
    LaunchedEffect(text) {
        snapshotFlow { text }
            .distinctUntilChanged()
            .debounce(duration)
            .collect { latestText ->
                onDebounce(latestText)
            }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar(
        modifier = Modifier,
        onDebounce = { value ->
            // 디버깅이나 로그에 출력
            println("Debounced value: $value")
        }
    )
}