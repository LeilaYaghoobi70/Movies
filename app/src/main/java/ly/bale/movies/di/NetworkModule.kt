package ly.bale.movies.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ly.bale.movies.network.RetrofitFactory
import ly.bale.movies.network.TmdbApi
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getServices() :TmdbApi  = RetrofitFactory.services

}