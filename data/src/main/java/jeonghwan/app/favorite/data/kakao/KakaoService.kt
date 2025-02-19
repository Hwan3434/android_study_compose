package jeonghwan.app.favorite.data.kakao

import jeonghwan.app.favorite.data.common.Constants
import jeonghwan.app.favorite.datamodel.ImageDocumentModel
import jeonghwan.app.favorite.datamodel.KakaoModel
import jeonghwan.app.favorite.datamodel.MovieDocumentModel
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoService {
    @GET(Constants.SEARCH+Constants.IMAGE)
    suspend fun requestImage(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): KakaoModel<ImageDocumentModel>

    @GET(Constants.SEARCH+Constants.MOVIE)
    suspend fun requestMovie(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): KakaoModel<MovieDocumentModel>
}