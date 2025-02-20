package jeonghwan.app.favorite.di.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.di.impl.ContentUseCaseImpl
import jeonghwan.app.favorite.di.impl.ImageRepositoryImpl
import jeonghwan.app.favorite.di.impl.MovieRepositoryImpl
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import javax.inject.Singleton
import jeonghwan.app.favorite.domain.repository.AppDatabase
import jeonghwan.app.favorite.domain.repository.FavoriteDao

@Module
@InstallIn(SingletonComponent::class)
object DI {

    // New function to provide the database
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    // New function to provide the DAO
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        kakaoDatasource: KakaoDatasource
    ): ImageRepositoryInterface = ImageRepositoryImpl(kakaoDatasource)

    @Provides
    @Singleton
    fun provideMovieRepository(
        kakaoDatasource: KakaoDatasource
    ): MovieRepositoryInterface = MovieRepositoryImpl(kakaoDatasource)

    @Provides
    @Singleton
    fun provideContentUseCase(
        imageRepository: ImageRepositoryInterface,
        movieRepository: MovieRepositoryInterface
    ): ContentUseCaseInterface = ContentUseCaseImpl(imageRepository, movieRepository)
}