package nz.ac.uclive.shopport.database

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "gift_list_item")
data class GiftListItem (
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo var title: String,
    @ColumnInfo var description: String,
    @ColumnInfo var price: Int,
    @ColumnInfo var location: String,
    @ColumnInfo var imageURI: String,
    @ColumnInfo var bought: Boolean = false,
    @ColumnInfo var forPerson: String,
    @ColumnInfo var forPersonColor: Int
): Parcelable

@Dao
interface GiftListItemDao {
    @Insert
    suspend fun insert(giftListItem: GiftListItem): Long

    @Update
    suspend fun update(giftListItem: GiftListItem)

    @Delete
    suspend fun delete(giftListItem: GiftListItem)

    @Query("DELETE FROM gift_list_item")
    suspend fun deleteAll()

    @Query("SELECT * FROM gift_list_item")
    fun getAll(): LiveData<List<GiftListItem>>

    @Query("SELECT COUNT(*) FROM gift_list_item")
    fun getCount(): LiveData<Int>

    @Query("SELECT DISTINCT forPerson FROM gift_list_item")
    fun getAllForPersons() : LiveData<List<String>>

    @Query("SELECT DISTINCT forPersonColor FROM gift_list_item WHERE forPerson = :forPerson")
    fun getColorForPerson(forPerson: String) : Int

}