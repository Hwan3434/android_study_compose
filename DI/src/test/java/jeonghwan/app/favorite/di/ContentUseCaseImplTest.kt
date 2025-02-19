package jeonghwan.app.favorite.di

import io.mockk.coEvery
import io.mockk.mockk
import jeonghwan.app.favorite.di.impl.ContentUseCaseImpl
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.model.ImageEntity
import jeonghwan.app.favorite.domain.model.MovieEntity
import jeonghwan.app.favorite.domain.model.QueryEntity
import jeonghwan.app.favorite.domain.model.QuerySort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContentUseCaseImplTest {

    private val dummyQuery = QueryEntity(
        query = "kotlin",
        sort = QuerySort.ACCURACY,
        page = 1,
        size = 10
    )

    // Dummy LocalDateTime 생성 (예: 2025-02-17 10:00:00, 2025-02-17 09:00:00 등)
    private val now = LocalDateTime(2025, Month.FEBRUARY, 17, 10, 0, 0)
    private val oneHourAgo: LocalDateTime = LocalDateTime(2025, Month.FEBRUARY, 17, 9, 0, 0)
    private val twoHoursAgo: LocalDateTime = LocalDateTime(2025, Month.FEBRUARY, 17, 8, 0, 0)

    @Test
    fun `getContent sorts content by dateTime descending with multiple items`() = runTest {
        // 더미 ImageEntity 생성 (2개)
        val dummyImageEntityLatest = ImageEntity(
            dateTime = now,
            thumbnail = "https://example.com/thumbnail_latest.jpg",
            imageUrl = "https://example.com/image_latest.jpg",
            displaySiteName = "Example Site",
            docUrl = "https://example.com/latest",
            width = 640,
            height = 480,
            collection = "blog"
        )
        val dummyImageEntityOld = ImageEntity(
            dateTime = twoHoursAgo,
            thumbnail = "https://example.com/thumbnail_old.jpg",
            imageUrl = "https://example.com/image_old.jpg",
            displaySiteName = "Example Site",
            docUrl = "https://example.com/old",
            width = 640,
            height = 480,
            collection = "blog"
        )

        // 더미 MovieEntity 생성 (1개)
        val dummyMovieEntity = MovieEntity(
            dateTime = oneHourAgo,
            title = "Example Movie",
            playTime = 120,
            thumbnail = "https://example.com/movie_thumbnail.jpg",
            url = "https://example.com/movie",
            author = "Example Author"
        )

        // 목 객체 생성
        val imageRepo = mockk<ImageRepositoryInterface>()
        val movieRepo = mockk<MovieRepositoryInterface>()

        // 성공 케이스 목킹: 이미지 저장소에서 두 개의 이미지, 영화 저장소에서 한 개의 동영상 반환
        coEvery { imageRepo.getImage(dummyQuery) } returns Result.success(listOf(dummyImageEntityLatest, dummyImageEntityOld))
        coEvery { movieRepo.getMovie(dummyQuery) } returns Result.success(listOf(dummyMovieEntity))

        val useCase = ContentUseCaseImpl(imageRepo, movieRepo)
        val result = useCase.getContent(dummyQuery)

        assertTrue(result.isSuccess)
        val contentList = result.getOrNull()
        assertEquals(3, contentList?.size)
        // 내림차순 정렬: 가장 최신의 dateTime이 첫 번째에 위치해야 함.
        contentList?.let {
            assertTrue(it[0].dateTime >= it[1].dateTime)
            assertTrue(it[1].dateTime >= it[2].dateTime)
            // 추가적으로, 가장 최신은 dummyImageEntityLatest, 두 번째는 dummyMovieEntity, 세 번째는 dummyImageEntityOld여야 함.
            assertEquals(dummyImageEntityLatest, it[0])
            assertEquals(dummyMovieEntity, it[1])
            assertEquals(dummyImageEntityOld, it[2])
        }
    }

    @Test
    fun `getContent returns success when both image and movie repositories succeed`() = runTest {
        // 성공 케이스: image 및 movie 모두 성공적인 결과 반환
        val dummyImageEntity = ImageEntity(
            dateTime = now,
            thumbnail = "https://example.com/thumbnail.jpg",
            imageUrl = "https://example.com/image.jpg",
            displaySiteName = "Example Site",
            docUrl = "https://example.com",
            width = 640,
            height = 480,
            collection = "blog"
        )
        val dummyMovieEntity = MovieEntity(
            dateTime = oneHourAgo,
            title = "Example Movie",
            playTime = 120,
            thumbnail = "https://example.com/thumbnail.jpg",
            url = "https://example.com",
            author = "Example Author"
        )

        val imageRepo = mockk<ImageRepositoryInterface>()
        val movieRepo = mockk<MovieRepositoryInterface>()

        coEvery { imageRepo.getImage(dummyQuery) } returns Result.success(listOf(dummyImageEntity))
        coEvery { movieRepo.getMovie(dummyQuery) } returns Result.success(listOf(dummyMovieEntity))

        val useCase = ContentUseCaseImpl(imageRepo, movieRepo)
        val result = useCase.getContent(dummyQuery)

        assertTrue(result.isSuccess)
        val contentList = result.getOrNull()
        assertNotNull(contentList)
        // 정렬: 최신 순, now > oneHourAgo
        assertEquals(2, contentList!!.size)
        assertEquals(dummyImageEntity, contentList[0])
        assertEquals(dummyMovieEntity, contentList[1])
    }

    @Test
    fun `getContent returns failure when image repository fails`() = runTest {
        // 이미지 저장소 실패, 영화 저장소 성공
        val dummyMovieEntity = MovieEntity(
            dateTime = oneHourAgo,
            title = "Example Movie",
            playTime = 120,
            thumbnail = "https://example.com/thumbnail.jpg",
            url = "https://example.com",
            author = "Example Author"
        )

        val imageRepo = mockk<ImageRepositoryInterface>()
        val movieRepo = mockk<MovieRepositoryInterface>()

        coEvery { imageRepo.getImage(dummyQuery) } returns Result.failure(Exception("Image error"))
        coEvery { movieRepo.getMovie(dummyQuery) } returns Result.success(listOf(dummyMovieEntity))

        val useCase = ContentUseCaseImpl(imageRepo, movieRepo)
        val result = useCase.getContent(dummyQuery)

        assertTrue(result.isFailure)
        val ex = result.exceptionOrNull()
        assertNotNull(ex)
        // image 에러 메시지만 포함되어야 함.
        assertTrue(ex!!.message?.contains("Image error") == true)
    }

    @Test
    fun `getContent returns failure when movie repository fails`() = runTest {
        // 영화 저장소 실패, 이미지 저장소 성공
        val dummyImageEntity = ImageEntity(
            dateTime = now,
            thumbnail = "https://example.com/thumbnail.jpg",
            imageUrl = "https://example.com/image.jpg",
            displaySiteName = "Example Site",
            docUrl = "https://example.com",
            width = 640,
            height = 480,
            collection = "blog"
        )

        val imageRepo = mockk<ImageRepositoryInterface>()
        val movieRepo = mockk<MovieRepositoryInterface>()

        coEvery { imageRepo.getImage(dummyQuery) } returns Result.success(listOf(dummyImageEntity))
        coEvery { movieRepo.getMovie(dummyQuery) } returns Result.failure(Exception("Movie error"))

        val useCase = ContentUseCaseImpl(imageRepo, movieRepo)
        val result = useCase.getContent(dummyQuery)

        assertTrue(result.isFailure)
        val ex = result.exceptionOrNull()
        assertNotNull(ex)
        // movie 에러 메시지만 포함되어야 함.
        assertTrue(ex!!.message?.contains("Movie error") == true)
    }

    @Test
    fun `getContent returns failure with combined error when both repositories fail`() = runTest {
        // 이미지와 영화 저장소 둘 다 실패함
        val imageException = Exception("Image error")
        val movieException = Exception("Movie error")

        val imageRepo = mockk<ImageRepositoryInterface>()
        val movieRepo = mockk<MovieRepositoryInterface>()

        coEvery { imageRepo.getImage(dummyQuery) } returns Result.failure(imageException)
        coEvery { movieRepo.getMovie(dummyQuery) } returns Result.failure(movieException)

        val useCase = ContentUseCaseImpl(imageRepo, movieRepo)
        val result = useCase.getContent(dummyQuery)

        assertTrue(result.isFailure)
        val ex = result.exceptionOrNull()
        assertNotNull(ex)
        // 두 에러 메시지 모두 포함되어야 함
        assertTrue(ex!!.message?.contains("Image error") == true)
        assertTrue(ex.message?.contains("Movie error") == true)
    }
}
