package ly.bale.movies.network

import ly.bale.movies.model.Movie
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TmdbApi {

    @Headers("Content-Type: application/json")
    @GET("movie/popular")
    suspend fun  getMovie(): List<Movie>

    @Headers("Content-Type: application/json")
    @GET("movie/{id}")
    suspend fun  getMovieById(@Path("id") id:Int) : Movie
}