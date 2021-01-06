package ly.bale.movies.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ly.bale.movies.model.Movie
import ly.bale.movies.network.TmdbApi
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val tmdbApi: TmdbApi
) {
    suspend fun getMovie(page:Int) : List<Movie> = withContext(Dispatchers.IO) {
        return@withContext tmdbApi.getMovie(page)
    }

    suspend fun getMovieById(movieId: Int) = withContext(Dispatchers.IO) {
        tmdbApi.getMovieById(id = movieId)
    }
}