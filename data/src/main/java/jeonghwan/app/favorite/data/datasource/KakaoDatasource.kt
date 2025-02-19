package jeonghwan.app.favorite.data.datasource

import jeonghwan.app.favorite.data.kakao.KakaoService
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.KakaoModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel

class KakaoDatasource(private val kakaoService: KakaoService) {
    suspend fun requestImage(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?
    ): KakaoModel<ImageDocumentModel> {
        return kakaoService.requestImage(
            query,
            sort,
            page,
            size
        )
    }

    suspend fun requestMovie(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?
    ): KakaoModel<MovieDocumentModel> {
        return kakaoService.requestMovie(
            query,
            sort,
            page,
            size
        )
    }
}