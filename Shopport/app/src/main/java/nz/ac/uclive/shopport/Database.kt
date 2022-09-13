package nz.ac.uclive.shopport

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [WishListItem::class], version = 1)
abstract class ShopportDatabase : RoomDatabase() {
    abstract fun wishListItemDao(): WishListItemDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ShopportDatabase? = null

        fun getDatabase(context: Context): ShopportDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopportDatabase::class.java,
                    "shopport_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}


class ShopportRepository(private val wishListItemDao: WishListItemDao) {
    val wishListItems: Flow<List<WishListItem>> = wishListItemDao.getAll()
    val numWishListItems: Flow<Int> = wishListItemDao.getCount()

    @WorkerThread
    suspend fun insert(wishListItem: WishListItem) {
        wishListItemDao.insert(wishListItem)
    }

    @WorkerThread
    suspend fun update(wishListItem: WishListItem) {
        wishListItemDao.update(wishListItem)
    }

    @WorkerThread
    suspend fun delete(wishListItem: WishListItem) {
        wishListItemDao.delete(wishListItem)
    }
}

