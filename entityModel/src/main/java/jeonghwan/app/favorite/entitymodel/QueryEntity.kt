package jeonghwan.app.favorite.entitymodel

data class QueryEntity(
    val query: String, // 검색어
    val sort: QuerySort = QuerySort.ACCURACY, // 정렬 방식
    val page: Int, // 페이지 번호
    val size: Int // 한 페이지에 보여줄 개수
)
