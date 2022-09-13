package nz.ac.uclive.shopport

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    fun insert(wishListItem: WishListItem) {
        coroutineScope.launch(Dispatchers.IO) {
            wishListItemDao.insert(wishListItem)
        }
    }

    fun update(wishListItem: WishListItem) {
        coroutineScope.launch(Dispatchers.IO) {
            wishListItemDao.update(wishListItem)
        }
    }

    fun delete(wishListItem: WishListItem) {
        coroutineScope.launch(Dispatchers.IO) {
            wishListItemDao.delete(wishListItem)
        }
    }
}

