package ly.bale.movies.dataBase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ly.bale.movies.model.Genres
import java.lang.reflect.Type



class GenreTypeConverter  {
    private val gson: Gson = Gson()

    @TypeConverter
    fun genresToJson(genres: List<Genres>?):String?{
        genres?.let {
            val type: Type = object : TypeToken<List<Genres?>?>() {}.type
            return gson.toJson(genres, type)
        }
        return  null
    }

    @TypeConverter
    fun jsonToGenres(genres: String?): List<Genres>? {
        genres?.let {
            val type = object : TypeToken<List<Genres?>?>() {}.type
            return gson.fromJson(genres, type)
        }
        return null
    }
}