package ly.bale.movies.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class GsonModule {

    @Provides
    @Singleton
    fun gsonProvider() =  GsonBuilder().create()
}