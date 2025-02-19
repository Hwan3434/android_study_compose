package jeonghwan.app.favorite.data

import io.mockk.coEvery
import io.mockk.mockk
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.data.kakao.KakaoService
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.KakaoModel
import jeonghwan.app.favorite.datamodel.MetaModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KakaoDatasourceTest {
    private lateinit var kakaoService: KakaoService
    private lateinit var kakaoDatasource: KakaoDatasource

    @Before
    fun setup() {
        kakaoService = mockk()
        kakaoDatasource = KakaoDatasource(kakaoService)
    }

    @Test
    fun requestImage() = runTest {

        // 더미 Meta 데이터 생성
        val dummyMeta = MetaModel(
            totalCount = 422583,
            pageableCount = 3854,
            isEnd = false
        )
        // 더미 ImageDocumentModel 생성
        val dummyImageDocument = ImageDocumentModel(
            collection = "blog",
            thumbnailUrl = "https://search3.kakaocdn.net/argon/0x200_85_hr/8x1bJ9J9J9J",
            imageUrl = "https://search3.kakaocdn.net/argon/0x200_85_hr/8x1bJ9J9J9J",
            width = 640,
            height = 480,
            displaySiteName = "네이버블로그",
            docUrl = "http://blog.naver.com/abc",
            dateTime = "2021-08-01T12:34:56"
        )
        // KakaoModel<ImageDocumentModel> 더미 응답 생성
        val dummyResponse = KakaoModel(
            meta = dummyMeta,
            documents = listOf(dummyImageDocument)
        )

        // ApiService의 requestImage 메서드 목킹
        coEvery {
            kakaoService.requestImage(
                query = any(),
                sort = any(),
                page = any(),
                size = any()
            )
        } returns dummyResponse

        // 실제 KakaoDatasource의 requestImage 호출
        val response = kakaoDatasource.requestImage("kotlin", null, 1, 10)

        // 응답 검증
        assertNotNull(response)
        assertEquals(dummyMeta.totalCount, response.meta.totalCount)
        assertEquals(1, response.documents.size)
    }

    @Test
    fun requestMovie() = runTest {
        // 더미 Meta 데이터 생성
        val dummyMeta = MetaModel(
            totalCount = 6033,
            pageableCount = 800,
            isEnd = false
        )
        // 더미 MovieDocumentModel 생성
        val dummyMovieDocument = MovieDocumentModel(
            title = "영화 제목",
            thumbnail = "https://search3.kakaocdn.net/argon/0x200_85_hr/8x1bJ9J9J9J",
            url = "http://movie.naver.com/abc",
            dateTime = "2021-08-01T12:34:56",
            author = "작가",
            playTime = 120
        )
        // KakaoModel<MovieDocumentModel> 더미 응답 생성
        val dummyResponse = KakaoModel(
            meta = dummyMeta,
            documents = listOf(dummyMovieDocument)
        )

        // ApiService의 requestMovie 메서드 목킹
        coEvery {
            kakaoService.requestMovie(
                query = any(),
                sort = any(),
                page = any(),
                size = any()
            )
        } returns dummyResponse

        // 실제 KakaoDatasource의 requestMovie 호출
        val response = kakaoDatasource.requestMovie("kotlin", null, 1, 10)

        // 응답 검증
        assertNotNull(response)
        assertEquals(dummyMeta.totalCount, response.meta.totalCount)
        assertEquals(1, response.documents.size)
    }
}