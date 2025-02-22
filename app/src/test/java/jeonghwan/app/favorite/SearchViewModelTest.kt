package jeonghwan.app.favorite

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.FavoriteEntity
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import jeonghwan.app.favorite.domain.usecase.FavoriteUsecaseInterface
import jeonghwan.app.favorite.ui.screen.search.SearchViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

// ContentEntity의 간단한 임시 구현
private data class DummyContentEntity(
    private val thumbnail: String,
    private val dateTime: Long
) : ContentEntity {
    override fun getThumbnail(): String = thumbnail
    override fun getDateTime(): Long = dateTime

    // getDate, getTime은 내부 로직(예: 확장함수)로 처리된다고 가정
    override fun getDate(): String = ""
    override fun getTime(): String = ""
}

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val contentUseCase: ContentUseCaseInterface = mockk(relaxed = true)
    private val favoriteUseCase: FavoriteUsecaseInterface = mockk(relaxed = true)

    // 테스트용 디스패처 및 스코프
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { favoriteUseCase.flowFavorites() } returns flowOf(
            listOf(
                FavoriteEntity("thumb1", 1000L),
                FavoriteEntity("thumb2", 2000L)
            )
        )
        viewModel = SearchViewModel(contentUseCase, favoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testUpdateQuery() = testScope.runTest {
        // 초기 query는 빈 문자열이어야 합니다.
        assertEquals("", viewModel.uiState.value.query)

        // updateQuery를 호출하여 새로운 쿼리 값이 반영되는지 확인합니다.
        val newQuery = "Test Query"
        viewModel.updateQuery(newQuery)
        assertEquals(newQuery, viewModel.uiState.value.query)
    }

    @Test
    fun testToggleFavorite_removesWhenAlreadyFavorite() = testScope.runTest {
        val dummyThumbnail = "dummy_thumbnail"
        val dummyDateTime = 0L

        // ContentEntity의 임시 구현
        val testContentEntity = DummyContentEntity(dummyThumbnail, dummyDateTime)

        // suspend 함수인 isFavorite가 true를 반환하도록 설정 (이미 즐겨찾기인 경우)
        coEvery { favoriteUseCase.isFavorite(dummyThumbnail) } returns true

        viewModel.toggleFavorite(testContentEntity)
        testDispatcher.scheduler.advanceUntilIdle()

        // 즐겨찾기인 경우 remove 함수가 호출되어야 합니다.
        coVerify { favoriteUseCase.remove(dummyThumbnail) }
    }

    @Test
    fun testToggleFavorite_insertsWhenNotFavorite() = testScope.runTest {
        val dummyThumbnail = "dummy_thumbnail"
        val dummyDateTime = 0L

        val testContentEntity = DummyContentEntity(dummyThumbnail, dummyDateTime)

        // suspend 함수인 isFavorite가 false를 반환하도록 설정 (즐겨찾기가 아닐 경우)
        coEvery { favoriteUseCase.isFavorite(dummyThumbnail) } returns false

        viewModel.toggleFavorite(testContentEntity)
        testDispatcher.scheduler.advanceUntilIdle()

        // 즐겨찾기가 아닌 경우 insert 함수가 호출되어야 합니다.
        coVerify {
            favoriteUseCase.insert(match {
                it.getThumbnail() == dummyThumbnail && it.getDateTime() == dummyDateTime
            })
        }
    }

    @Test
    fun testFavoriteFlowEmission() = testScope.runTest {
        // SearchViewModel 생성 시, already 설정된 flowFavorites() 모의 데이터가 적용되어야 합니다.
        val favoriteSet = viewModel.favoriteFlow.first()
        val expected = setOf(
            FavoriteEntity("thumb1", 1000L),
            FavoriteEntity("thumb2", 2000L)
        )
        assertEquals(expected, favoriteSet)
    }
}