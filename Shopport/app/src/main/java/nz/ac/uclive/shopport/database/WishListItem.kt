package nz.ac.uclive.shopport.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "wish_list_item")
data class WishListItem (
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo var title: String,
    @ColumnInfo var description: String,
    @ColumnInfo var price: Double,
    @ColumnInfo var location: String,
    @ColumnInfo var imageURI: String,
    @ColumnInfo var bought: Boolean = false)

@Dao
interface WishListItemDao {
    @Insert
    suspend fun insert(wishListItem: WishListItem): Long

    @Update
    suspend fun update(wishListItem: WishListItem)

    @Delete
    suspend fun delete(wishListItem: WishListItem)

    @Query("DELETE FROM wish_list_item")
    suspend fun deleteAll()

    @Query("SELECT * FROM wish_list_item")
    fun getAll(): LiveData<List<WishListItem>>

    @Query("SELECT COUNT(*) FROM wish_list_item")
    fun getCount(): LiveData<Int>

}