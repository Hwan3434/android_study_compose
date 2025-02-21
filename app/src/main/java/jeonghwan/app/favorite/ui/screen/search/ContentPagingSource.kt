package jeonghwan.app.favorite.ui.screen.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.ContentQueryEntity
import jeonghwan.app.favorite.domain.model.QuerySort
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import kotlinx.coroutines.coroutineScope

data class ContentPagingKey(
    val query: String,
    val imagePage: Int,
    val moviePage: Int,
    val sort: QuerySort,
    val size: Int,
    val isImageEnd: Boolean = false,
    val isMovieEnd: Boolean = false
)

class ContentPagingSource(
    private val contentUseCase: ContentUseCaseInterface,
    private val initialKey: ContentPagingKey,
) : PagingSource<ContentPagingKey, ContentEntity>() {

    override suspend fun load(
        params: LoadParams<ContentPagingKey>
    ): LoadResult<ContentPagingKey, ContentEntity> = coroutineScope {
        val key = params.key ?: initialKey
        val query = ContentQueryEntity(
            query = key.query,
            sort = key.sort,
            imagePage = key.imagePage,
            moviePage = key.moviePage,
            size = key.size,
            isImageEnd = key.isImageEnd,
            isMovieEnd = key.isMovieEnd
        )

        return@coroutineScope try {
            val result = contentUseCase.getContent(query)
            result.fold(
                onSuccess = { pagingResult ->
                    val nextKey = if (pagingResult.data.isEmpty()) null else {
                        key.copy(
                            imagePage = if(pagingResult.isImageLastPage) key.imagePage else key.imagePage + 1,
                            moviePage = if(pagingResult.isMovieLastPage) key.moviePage else key.moviePage + 1,
                            isImageEnd = pagingResult.isImageLastPage,
                            isMovieEnd = pagingResult.isMovieLastPage)
                    }

                    val prevKey = if (key.imagePage == 1 && key.moviePage == 1) null
                        else key.copy(imagePage = key.imagePage - 1, moviePage = key.moviePage - 1)

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
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.let { key ->
                key.copy(imagePage = key.imagePage + 1, moviePage = key.moviePage + 1)
            } ?: state.closestPageToPosition(anchorPos)?.nextKey?.let { key ->
                key.copy(imagePage = key.imagePage - 1, moviePage = key.moviePage - 1)
            }
        }
    }
}