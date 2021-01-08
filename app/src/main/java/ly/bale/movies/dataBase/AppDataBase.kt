package ly.bale.movies.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ly.bale.movies.model.Movie

@Database(entities = [Movie::class], version = 2, exportSchema = false)
@TypeConverters(
    GenreTypeConverter::class
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}