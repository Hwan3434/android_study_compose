package jeonghwan.app.favorite.ui.screen.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import jeonghwan.app.favorite.domain.model.ContentEntity
import jeonghwan.app.favorite.domain.model.QueryEntity
import jeonghwan.app.favorite.domain.model.QuerySort
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class ContentPagingSource(
    private val contentUseCase: ContentUseCaseInterface,
    private val query: String
) : PagingSource<Int, ContentEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentEntity> = coroutineScope {
        // 현재 페이지: 초기에는 key가 null이므로 1페이지부터 시작합니다.

        val currentPage = params.key ?: 1
        Timber.d("load current page: $currentPage")
        // QueryEntity 생성 (필요한 추가 필드가 있다면 추가)
        val queryEntity = QueryEntity(
            query = query,
            sort = QuerySort.ACCURACY,
            page = currentPage,
            size = params.loadSize
        )

        return@coroutineScope try {
            val result = contentUseCase.getContent(queryEntity)
            when {
                result.isSuccess -> {
                    val data = result.getOrDefault(emptyList())
                    val nextKey = if (data.isEmpty()) null else currentPage + 1
                    val prevKey = if (currentPage == 1) null else currentPage - 1
                    LoadResult.Page(
                        data = data,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                result.isFailure -> {
                    LoadResult.Error(result.exceptionOrNull() ?: Exception("Unknown error"))
                }
                else -> {
                    LoadResult.Error(Exception("Unknown error"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // getRefreshKey(): 새로고침할 때 사용될 키를 반환합니다.
    // 여기서는 현재 보이는 페이지의 앵커(anchor) position을 기준으로 계산합니다.
    override fun getRefreshKey(state: PagingState<Int, ContentEntity>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }
}