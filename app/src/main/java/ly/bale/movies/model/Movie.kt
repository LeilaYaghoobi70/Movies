package ly.bale.movies.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import ly.bale.movies.dataBase.GenreTypeConverter

@Entity(tableName = "movieEntity")
data  class  Movie (

    @SerializedName("vote_count")
    @ColumnInfo(name = "voteCount")
    var voteCount: Int?,

    @SerializedName("id")
    @ColumnInfo(name = "serverId")
    var serverId: Int?,

    @SerializedName("video")
    @ColumnInfo(name = "isVideo")
    var video: Boolean?,

    @SerializedName("vote_average")
    @ColumnInfo(name = "voteAverage")
    var voteAverage: Double?,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String?,

    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    var popularity: Double?,

    @SerializedName("poster_path")
    @ColumnInfo(name = "posterPath")
    var posterPath: String?,

    @ColumnInfo(name = "originalLanguage")
    @SerializedName("original_language")
    var originalLanguage: String?,

    @SerializedName("original_title")
    @ColumnInfo(name = "originalTitle")
    var originalTitle: String?,

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdropPath")
    var backdropPath: String?,

    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    var adult: Boolean?,

    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    var overview: String?,

    @SerializedName("release_date")
    @ColumnInfo(name = "releaseDate")
    var releaseDate: String?,

    @SerializedName("genres")
    @TypeConverters(GenreTypeConverter::class)
    var genreIds: List<Genres>?,

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var localId: Int = 0

}


