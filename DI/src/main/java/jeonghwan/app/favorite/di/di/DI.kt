package jeonghwan.app.favorite.di.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jeonghwan.app.favorite.data.datasource.KakaoDatasource
import jeonghwan.app.favorite.core.database.datasource.CacheDatasource
import jeonghwan.app.favorite.core.database.datasource.FavoriteDatasource
import jeonghwan.app.favorite.di.impl.ContentUseCaseImpl
import jeonghwan.app.favorite.di.impl.FavoriteRepositoryImpl
import jeonghwan.app.favorite.di.impl.FavoriteUseCaseImpl
import jeonghwan.app.favorite.di.impl.ImageRepositoryImpl
import jeonghwan.app.favorite.di.impl.MovieRepositoryImpl
import jeonghwan.app.favorite.domain.repository.FavoriteRepositoryInterface
import jeonghwan.app.favorite.domain.repository.ImageRepositoryInterface
import jeonghwan.app.favorite.domain.repository.MovieRepositoryInterface
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import jeonghwan.app.favorite.domain.usecase.FavoriteUsecaseInterface
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

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