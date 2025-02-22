package jeonghwan.app.favorite.di.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jeonghwan.app.favorite.common.DBCommon
import jeonghwan.app.favorite.common.URLCommon
import jeonghwan.app.favorite.data.database.AppDatabase
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.data.datasource.CacheDatasource
import jeonghwan.app.favorite.data.datasource.FavoriteDatasource
import jeonghwan.app.favorite.data.BuildConfig
import jeonghwan.app.favorite.data.common.KakaoAuthorizationInterceptor
import jeonghwan.app.favorite.data.impl.usecase.ContentUseCaseImpl
import jeonghwan.app.favorite.data.impl.repository.FavoriteRepositoryImpl
import jeonghwan.app.favorite.data.impl.usecase.FavoriteUseCaseImpl
import jeonghwan.app.favorite.data.impl.repository.ImageRepositoryImpl
import jeonghwan.app.favorite.data.impl.repository.MovieRepositoryImpl
import jeonghwan.app.favorite.data.kakao.KakaoService
import jeonghwan.app.favorite.domain.repository.FavoriteRepositoryInterface
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import jeonghwan.app.favorite.domain.usecase.FavoriteUsecaseInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DBCommon.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDatasource {
        return appDatabase.favoriteDatasource()
    }

    @Provides
    fun provideCacheDaoDao(appDatabase: AppDatabase): CacheDatasource {
        return appDatabase.cacheDatasource()
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
        @Named("KakaoLoggingInterceptor") loggingInterceptor: HttpLoggingInterceptor,
        @Named("KakaoAuthInterceptor") authInterceptor: KakaoAuthorizationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("KakaoRetrofit")
    fun provideKakaoRetrofit(
        @Named("KakaoOkHttp") client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URLCommon.KAKAO_URL) // BASE_URL for Kakao API, 반드시 "/"로 끝나야 함
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoApiService(
        @Named("KakaoRetrofit") retrofit: Retrofit
    ): KakaoService = retrofit.create(KakaoService::class.java)

    @Provides
    @Singleton
    fun provideKakaoDatasource(
        kakaoApiService: KakaoService,
    ) = KakaoDatasource(
        kakaoService = kakaoApiService
    )

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        favoriteDatasource: FavoriteDatasource,
    ): FavoriteRepositoryInterface = FavoriteRepositoryImpl(favoriteDatasource)


    @Provides
    @Singleton
    fun provideFavoriteUseCase(
        favoriteRepositoryInterface: FavoriteRepositoryInterface,
    ): FavoriteUsecaseInterface = FavoriteUseCaseImpl(
        favoriteRepositoryInterface = favoriteRepositoryInterface
    )

    @Provides
    @Singleton
    fun provideImageRepository(
        kakaoDatasource: KakaoDatasource,
        cacheDatasource: CacheDatasource
    ): ImageRepositoryInterface = ImageRepositoryImpl(kakaoDatasource, cacheDatasource)

    @Provides
    @Singleton
    fun provideMovieRepository(
        kakaoDatasource: KakaoDatasource,
        cacheDatasource: CacheDatasource
    ): MovieRepositoryInterface = MovieRepositoryImpl(kakaoDatasource, cacheDatasource)

    @Provides
    @Singleton
    fun provideContentUseCase(
        imageRepository: ImageRepositoryInterface,
        movieRepository: MovieRepositoryInterface
    ): ContentUseCaseInterface = ContentUseCaseImpl(imageRepository, movieRepository)
}