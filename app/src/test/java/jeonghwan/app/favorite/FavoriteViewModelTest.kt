package jeonghwan.app.favorite

import androidx.paging.PagingData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jeonghwan.app.favorite.domain.model.MovieEntity
import jeonghwan.app.favorite.domain.usecase.FavoriteUsecaseInterface
import jeonghwan.app.favorite.ui.screen.favorite.FavoriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTestMockK {

    private lateinit var viewModel: FavoriteViewModel
    private val favoriteUseCase: FavoriteUsecaseInterface = mockk(relaxed = true)

    // StandardTestDispatcher로 테스트 스코프 생성
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Flow에 대한 기본 반환값 설정
        every { favoriteUseCase.flowFavorites() } returns flowOf(emptyList())
        every { favoriteUseCase.getPagedFavorites() } returns flowOf(PagingData.empty())

        viewModel = FavoriteViewModel(favoriteUseCase)
    }

    @Test
    fun `toggleFavorite called remove`() = testScope.runTest {
        val contentEntity = MovieEntity(
            title = "영화 제목",
            thumbnail = "영화 썸네일",
            url = "영화 URL",
            author = "영화 감독",
            dateTime = 0L,
            playTime = 0,
        )

        // When
        viewModel.toggleFavorite(contentEntity)

        // 코루틴 실행을 마무리하기 위해 dispatcher로 pending coroutine 작업들을 전부 실행합니다.
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: toggleFavorite 호출 시 remove가 호출되었는지 검증
        coVerify { favoriteUseCase.remove(contentEntity.getThumbnail()) }
    }
}
