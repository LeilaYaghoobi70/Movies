package ly.bale.movies.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ly.bale.movies.dataBase.AppDataBase
import ly.bale.movies.dataBase.MovieDao

import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DateBaseModule {

    @Provides
    @Singleton
    fun provideMovieDao(@ApplicationContext  context: Context): MovieDao {
        val dataBase = Room.databaseBuilder(context, AppDataBase::class.java, "movie.db").build()
        return  dataBase.movieDao()
    }
}