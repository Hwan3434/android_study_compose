package jeonghwan.app.favorite.data.common

import okhttp3.Interceptor
import okhttp3.Response



class CacheInterceptor(
    private val shouldCache: (String) -> Boolean,
    private val maxAgeSeconds: Int = 300
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url.toString()

        val requestWithCache = if (shouldCache(originalUrl)) {
            originalRequest.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAgeSeconds")
                .build()
        } else {
            originalRequest // 캐싱 조건에 해당하지 않으면 원래 요청 그대로 진행
        }
        return chain.proceed(requestWithCache)
    }
}
