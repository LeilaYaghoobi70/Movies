package ly.bale.movies.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ly.bale.movies.model.Movie
import ly.bale.movies.network.TmdbApi
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val tmdbApi: TmdbApi
) {
    suspend fun getMovie() : List<Movie> = withContext(Dispatchers.IO) {
        val movie = tmdbApi.getMovie()
        return@withContext movie
    }

    suspend fun getMovieById(movieId: Int) = withContext(Dispatchers.IO) {
        tmdbApi.getMovieById(id = movieId)
    }
}