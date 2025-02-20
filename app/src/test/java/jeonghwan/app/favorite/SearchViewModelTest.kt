package jeonghwan.app.favorite

import androidx.paging.PagingData
import androidx.paging.map
import io.mockk.coEvery
import io.mockk.mockk
import jeonghwan.app.favorite.domain.model.ContentPagingResult
import jeonghwan.app.favorite.domain.model.ContentQueryEntity
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.QuerySort
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import jeonghwan.app.favorite.ui.screen.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
@FlowPreview
class SearchViewModelTest {

    // Mocking dependencies
    private lateinit var contentUseCase: ContentUseCaseInterface
    private lateinit var viewModel: SearchViewModel

    // Coroutine TestDispatcher 설정
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // MockK로 ContentUseCase를 Mocking
        contentUseCase = mockk()

        // 코루틴 디스패처 설정
        Dispatchers.setMain(testDispatcher)

        // ViewModel 초기화
        viewModel = SearchViewModel(contentUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()  // Dispatcher 리셋
    }

    @Test
    fun `updateQuery updates uiState correctly`() = runTest {
        // Given
        val newQuery = "new query"

        // When
        viewModel.updateQuery(newQuery)

        // Then
        assertEquals(viewModel.uiState.value.query, newQuery)
    }

    @Test
    fun `pagingData emits correct data when query changes`() = runTest {
        // Given
        val query = "search query"

        val result = ImageEntity(
            displaySiteName = "site",
            imageUrl = "url",
            thumbnail = "thumbnail",
            width = 100,
            height = 100,
            docUrl = "docUrl",
            dateTime = LocalDateTime(2021, 8, 1, 12, 34, 56),
            collection = "collection",
        )

        coEvery {
            contentUseCase.getContent(
                query = ContentQueryEntity(
                    query = query,
                    sort = QuerySort.ACCURACY,
                    moviePage = 1,
                    imagePage = 1,
                    size = 10
                ),
            )
        } returns Result.success(
            ContentPagingResult(
                data = listOf(result),
                isImageLastPage = false,
                isMovieLastPage = false
            )
        )

        // When
        viewModel.updateQuery(query)

        // Then
        val job = launch {
            viewModel.pagingData.collectLatest { data ->
                data.map {
                    assertEquals(result, it)
                }
            }
        }

        job.cancel()  // Collecting 끝내기
    }
}
