package jeonghwan.app.favorite.data.common

import okhttp3.Interceptor
import okhttp3.Response


class KakaoAuthorizationInterceptor(private val kakaoApiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "KakaoAK $kakaoApiKey")
            .build()
        return chain.proceed(newRequest)
    }
}