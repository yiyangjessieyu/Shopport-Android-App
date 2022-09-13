package nz.ac.uclive.shopport.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WishListItem::class], version = 1)
abstract class ShopportDatabase : RoomDatabase() {
    abstract fun wishListItemDao(): WishListItemDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        private var INSTANCE: ShopportDatabase? = null

        fun getInstance(context: Context): ShopportDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ShopportDatabase::class.java,
                        "shopport_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}


class ShopportRepository(private val wishListItemDao: WishListItemDao) {
    val wishListItems: LiveData<List<WishListItem>> = wishListItemDao.getAll()
    val numWishListItems: LiveData<Int> = wishListItemDao.getCount()

    suspend fun insert(wishListItem: WishListItem) {
            wishListItemDao.insert(wishListItem)
    }

    suspend fun update(wishListItem: WishListItem) {
            wishListItemDao.update(wishListItem)
    }

    suspend fun delete(wishListItem: WishListItem) {
            wishListItemDao.delete(wishListItem)
    }
}

