package nz.ac.uclive.shopport

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "wish_list_item")
class WishListItem (
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo var title: String,
    @ColumnInfo var description: String,
    @ColumnInfo var price: Int,
    @ColumnInfo var location: String,
    @ColumnInfo var imageId: String,
    @ColumnInfo var bought: Boolean) {

    override fun toString() = title;
}

@Dao
interface WishListItemDao {
    @Insert
    suspend fun insert(wishListItem: WishListItem): Long

    @Update
    suspend fun update(wishListItem: WishListItem)

    @Delete
    suspend fun delete(wishListItem: WishListItem)

    @Query("SELECT * FROM wish_list_item")
    fun getAll(): LiveData<List<WishListItem>>

    @Query("SELECT COUNT(*) FROM wish_list_item")
    fun getCount(): LiveData<Int>

}