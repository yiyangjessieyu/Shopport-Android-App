package nz.ac.uclive.shopport.database
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "date_item")
data class DateItem (
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo var title: String,
    @ColumnInfo var description: String,
    @ColumnInfo var forPerson: String,
    @ColumnInfo var forPersonColor: Int,
    @ColumnInfo var month: Int,
    @ColumnInfo var dayOfMonth: Int
)

@Dao
interface DateItemDao {
    @Insert
    suspend fun insert(dateItem: DateItem): Long

    @Update
    suspend fun update(dateItem: DateItem)

    @Delete
    suspend fun delete(ateItem: DateItem)

    @Query("DELETE FROM date_item")
    suspend fun deleteAll()

    @Query("SELECT * FROM date_item")
    fun getAll(): LiveData<List<DateItem>>

    @Query("SELECT COUNT(*) FROM date_item")
    fun getCount(): LiveData<Int>

    @Query("SELECT DISTINCT forPerson FROM date_item")
    fun getAllForPersons() : LiveData<List<String>>

    @Query("SELECT DISTINCT forPersonColor FROM date_item WHERE forPerson = :forPerson")
    fun getColorForPerson(forPerson: String) : Int

}
