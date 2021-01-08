package ly.bale.movies.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ly.bale.movies.dataBase.MovieDao
import ly.bale.movies.model.Movie
import ly.bale.movies.network.TmdbApi
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val movieDao: MovieDao
) {
    suspend fun getMovie(pageNumber: Int, isRefresh: Boolean): List<Movie> =
        withContext(Dispatchers.IO) {
            val count = movieDao.getMoviesCount(20, (pageNumber - 1) * 20)
            if (isRefresh || count == 0) {
                val movies = tmdbApi.getMovie(page = pageNumber)

                if (isRefresh)
                    movieDao.deleteMovies()

                movieDao.insertMovies(movies)

                return@withContext movies
            } else
                return@withContext movieDao.getMovies(pageSize = 20, offset = (pageNumber - 1) * 20)
                    ?: ArrayList()
        }

    suspend fun getMovieById(movieId: Int) = withContext(Dispatchers.IO) {
        tmdbApi.getMovieById(id = movieId)
    }
}