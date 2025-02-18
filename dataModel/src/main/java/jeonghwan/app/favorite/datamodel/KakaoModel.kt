package jeonghwan.app.favorite.datamodel

data class KakaoModel<T>(
    val meta: MetaModel,
    val documents: List<T>
)