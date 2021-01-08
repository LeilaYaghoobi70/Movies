package ly.bale.movies.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.bale.movies.model.Movie
import retrofit2.http.DELETE

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movieEntity LIMIT :pageSize OFFSET :offset")
    suspend fun getMovies(pageSize: Int, offset: Int): List<Movie>?

    @Query("SELECT  COUNT(id) from (SELECT  id  FROM movieEntity LIMIT :pageSize OFFSET :offset)")
    suspend fun getMoviesCount(pageSize: Int, offset: Int): Int

    @Query("DELETE  FROM movieEntity")
    suspend fun deleteMovies()
}