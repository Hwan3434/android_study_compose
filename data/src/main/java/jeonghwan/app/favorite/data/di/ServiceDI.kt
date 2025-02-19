package jeonghwan.app.favorite.data.di

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jeonghwan.app.favorite.data.BuildConfig
import jeonghwan.app.favorite.data.common.CacheInterceptor
import jeonghwan.app.favorite.data.common.Constants
import jeonghwan.app.favorite.data.common.KakaoAuthorizationInterceptor
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.data.kakao.KakaoService
import java.io.File
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object KakaoNetworkModule {

    private const val CACHE_SIZE: Long = 10 * 1024 * 1024  // 10 MB

    @Provides
    @Singleton
    @Named("KakaoCache")
    fun provideKakaoCache(application: Application): Cache {
        val cacheDir = File(application.cacheDir, "http-cache")
        return Cache(cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    @Named("KakaoCacheInterceptor")
    fun provideKakaoCacheInterceptor(): Interceptor {
        return CacheInterceptor(
            shouldCache = { url ->
                url.contains(Constants.SEARCH+Constants.IMAGE) || url.contains(Constants.SEARCH+Constants.MOVIE)
            },
            maxAgeSeconds = 300
        )
    }

    @Provides
    @Singleton
    @Named("KakaoAuthInterceptor")
    fun provideKakaoAuthorizationInterceptor(): KakaoAuthorizationInterceptor {
        return KakaoAuthorizationInterceptor(kakaoApiKey = BuildConfig.KAKAO_API_KEY)
    }

    @Provides
    @Singleton
    @Named("KakaoLoggingInterceptor")
    fun provideKakaoLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named("KakaoOkHttp")
    fun provideKakaoOkHttpClient(
        @Named("KakaoCache") cache: Cache,
        @Named("KakaoCacheInterceptor") cacheInterceptor: Interceptor,
        @Named("KakaoLoggingInterceptor") loggingInterceptor: HttpLoggingInterceptor,
        @Named("KakaoAuthInterceptor") authInterceptor: KakaoAuthorizationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(cacheInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("KakaoRetrofit")
    fun provideKakaoRetrofit(
        @Named("KakaoOkHttp") client: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // BASE_URL for Kakao API, 반드시 "/"로 끝나야 함
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideKakaoApiService(
        @Named("KakaoRetrofit") retrofit: Retrofit
    ) = retrofit.create(KakaoService::class.java)

    @Provides
    @Singleton
    fun provideKakaoDatasource(
        kakaoApiService: KakaoService
    ) = KakaoDatasource(kakaoApiService)
}