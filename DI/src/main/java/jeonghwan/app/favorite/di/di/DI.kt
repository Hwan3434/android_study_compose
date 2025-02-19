package jeonghwan.app.favorite.di.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.di.impl.ContentUseCaseImpl
import jeonghwan.app.favorite.di.impl.ImageRepositoryImpl
import jeonghwan.app.favorite.di.impl.MovieRepositoryImpl
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

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