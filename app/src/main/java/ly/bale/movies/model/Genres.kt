package ly.bale.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Genres(

    @SerializedName("id")
    @ColumnInfo(name = "serverId")
    var serverId: Int?,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String?
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var localId: Int = 0
}
