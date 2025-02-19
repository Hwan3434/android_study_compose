package jeonghwan.app.favorite.di
import io.mockk.coEvery
import io.mockk.mockk
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.KakaoModel
import jeonghwan.app.favorite.datamodel.MetaModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import jeonghwan.app.favorite.di.impl.ImageRepositoryImpl
import jeonghwan.app.favorite.di.impl.MovieRepositoryImpl
import jeonghwan.app.favorite.entitymodel.QueryEntity
import jeonghwan.app.favorite.entitymodel.QuerySort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoriesTest {

    // 목으로 KakaoDatasource 생성
    private lateinit var kakaoDatasource: KakaoDatasource

    // Repository 객체
    private lateinit var imageRepository: ImageRepositoryImpl
    private lateinit var movieRepository: MovieRepositoryImpl

    // 더미 데이터

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

    val dummyMovieDocument = MovieDocumentModel(
        title = "영화 제목",
        thumbnail = "https://search3.kakaocdn.net/argon/0x200_85_hr/8x1bJ9J9J9J",
        url = "http://movie.naver.com/abc",
        dateTime = "2021-08-01T12:34:56",
        author = "작가",
        playTime = 120
    )

    private val dummyQuery = QueryEntity(
        query = "kotlin",
        sort = QuerySort.ACCURACY,
        page = 1,
        size = 10
    )

    @Before
    fun setup() {
        kakaoDatasource = mockk()

        imageRepository = ImageRepositoryImpl(kakaoDatasource)
        movieRepository = MovieRepositoryImpl(kakaoDatasource)
    }

    @Test
    fun `getImage returns successful result and maps documents correctly`() = runTest {
        // 목킹: requestImage() 가 성공적인 응답을 반환하도록 설정
        coEvery {
            kakaoDatasource.requestImage(
                query = dummyQuery.query,
                sort = dummyQuery.sort.name,
                page = dummyQuery.page,
                size = dummyQuery.size
            )
        } returns KakaoModel(
            meta = dummyMeta,
            documents = listOf(dummyImageDocument)
        )

        val result = imageRepository.getImage(dummyQuery)

        // 결과 검증: 성공 결과여야 하며, 변환된 문서 값이 올바른지 확인
        assertTrue(result.isSuccess)
        val imageEntities = result.getOrNull()
        assertNotNull(imageEntities)
        assertEquals(1, imageEntities!!.size)
        assertEquals(dummyImageDocument.imageUrl, imageEntities.first().imageUrl)
    }

    @Test
    fun `getMovie returns successful result and maps documents correctly`() = runTest {
        // 목킹: requestMovie() 가 성공 응답을 반환하도록 설정
        coEvery {
            kakaoDatasource.requestMovie(
                query = dummyQuery.query,
                sort = dummyQuery.sort.name,
                page = dummyQuery.page,
                size = dummyQuery.size
            )
        } returns KakaoModel(
            meta = dummyMeta,
            documents = listOf(dummyMovieDocument)
        )

        val result = movieRepository.getMovie(dummyQuery)

        // 결과 검증: 성공 결과여야 하며, 변환된 문서 값이 올바른지 확인
        assertTrue(result.isSuccess)
        val movieEntities = result.getOrNull()
        assertNotNull(movieEntities)
        assertEquals(1, movieEntities!!.size)
        assertEquals(dummyMovieDocument.title, movieEntities.first().title)
    }

    @Test
    fun `getImage returns failure when datasource throws exception image`() = runTest {

        val errorMessage = "Network error"
        coEvery {
            kakaoDatasource.requestImage(
                query = dummyQuery.query,
                sort = dummyQuery.sort.name,
                page = dummyQuery.page,
                size = dummyQuery.size
            )
        } throws Exception(errorMessage)

        val result = imageRepository.getImage(dummyQuery)

        // 결과 검증: 실패 결과, 에러 메시지가 "Network error"이어야 함
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals(errorMessage, exception?.message)
    }
    @Test
    fun `getImage returns failure when datasource throws exception movie`() = runTest {

        val errorMessage = "Network error"
        coEvery {
            kakaoDatasource.requestMovie(
                query = dummyQuery.query,
                sort = dummyQuery.sort.name,
                page = dummyQuery.page,
                size = dummyQuery.size
            )
        } throws Exception(errorMessage)

        val result = movieRepository.getMovie(dummyQuery)

        // 결과 검증: 실패 결과, 에러 메시지가 "Network error"이어야 함
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals(errorMessage, exception?.message)
    }
}