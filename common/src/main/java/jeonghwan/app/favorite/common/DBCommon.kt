package jeonghwan.app.favorite.common

class DBCommon {
    companion object {
        const val DATABASE_NAME = "favorite.db"
        const val DATABASE_FAVORITE_TABLE = "favorite"
        const val DATABASE_CACHE_TABLE = "cache"
        const val CACHE_EXPIRATION_MINUTES = 5L
    }
}