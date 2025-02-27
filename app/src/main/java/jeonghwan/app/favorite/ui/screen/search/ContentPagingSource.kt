package jeonghwan.app.favorite.ui.screen.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.ContentQueryEntity
import jeonghwan.app.favorite.domain.model.QuerySort
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

data class ContentPagingKey(
    val query: String,
    val imagePage: Int,
    val moviePage: Int,
    val sort: QuerySort = QuerySort.ACCURACY,
    val size: Int,
    val isImageEnd: Boolean = false,
    val isMovieEnd: Boolean = false
)

class ContentPagingSource(
    private val contentUseCase: ContentUseCaseInterface,
    private val query: String
) : PagingSource<ContentPagingKey, ContentEntity>() {

    override suspend fun load(
        params: LoadParams<ContentPagingKey>
    ): LoadResult<ContentPagingKey, ContentEntity> = coroutineScope {
        val loadSize = params.loadSize
        val currentPage = params.key ?: ContentPagingKey(
            query = query,
            size = loadSize,
            imagePage = 1,
            moviePage = 1,
        )
        val query = ContentQueryEntity(
            query = currentPage.query,
            sort = currentPage.sort,
            imagePage = currentPage.imagePage,
            moviePage = currentPage.moviePage,
            size = currentPage.size,
            isImageEnd = currentPage.isImageEnd,
            isMovieEnd = currentPage.isMovieEnd
        )

        return@coroutineScope try {
            val result = contentUseCase.getContent(query)
            result.fold(
                onSuccess = { pagingResult ->
                    Timber.d("호출 호출 !! : query = ${currentPage.query}, imagePage = ${currentPage.imagePage}, moviePage = ${currentPage.moviePage}")

                    val nextKey = if (pagingResult.data.isEmpty()) null else {
                        currentPage.copy(
                            imagePage = if(pagingResult.isImageLastPage) currentPage.imagePage else currentPage.imagePage + 1,
                            moviePage = if(pagingResult.isMovieLastPage) currentPage.moviePage else currentPage.moviePage + 1,
                            isImageEnd = pagingResult.isImageLastPage,
                            isMovieEnd = pagingResult.isMovieLastPage)
                    }
                    val prevKey = if (currentPage.imagePage == 1 && currentPage.moviePage == 1) null
                        else currentPage.copy(imagePage = currentPage.imagePage - 1, moviePage = currentPage.moviePage - 1)
                    LoadResult.Page(
                        data = pagingResult.data,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                },
                onFailure = { e ->
                    LoadResult.Error(e)
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    // getRefreshKey(): 새로고침할 때 사용될 키를 반환합니다.
    // 여기서는 현재 보이는 페이지의 앵커(anchor) position을 기준으로 계산합니다.
    override fun getRefreshKey(state: PagingState<ContentPagingKey, ContentEntity>): ContentPagingKey? {
        Timber.d("getRefreshKey 호출")
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.let { key ->
                key.copy(imagePage = key.imagePage + 1, moviePage = key.moviePage + 1)
            } ?: state.closestPageToPosition(anchorPos)?.nextKey?.let { key ->
                key.copy(imagePage = key.imagePage - 1, moviePage = key.moviePage - 1)
            }
        }
    }
}