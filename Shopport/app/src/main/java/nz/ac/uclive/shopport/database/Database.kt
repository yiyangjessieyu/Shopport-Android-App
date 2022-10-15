package nz.ac.uclive.shopport.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WishListItem::class, GiftListItem::class], version = 1)
abstract class ShopportDatabase : RoomDatabase() {
    abstract fun wishListItemDao(): WishListItemDao
    abstract fun giftListItemDao(): GiftListItemDao

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


class WishlistItemRepository(
    private val wishListItemDao: WishListItemDao,
    ) {

    val wishListItems: LiveData<List<WishListItem>> = wishListItemDao.getAll()

    suspend fun insertWishListItem(wishListItem: WishListItem) {
            wishListItemDao.insert(wishListItem)
    }

    suspend fun updateWishListItem(wishListItem: WishListItem) {
            wishListItemDao.update(wishListItem)
    }

    suspend fun deleteWishListItem(wishListItem: WishListItem) {
            wishListItemDao.delete(wishListItem)
    }

    suspend fun deleteAllWishListItems() {
            wishListItemDao.deleteAll()
    }
}

class GiftlistItemRepository(
    private val giftListItemDao: GiftListItemDao,
) {

    val giftListItems: LiveData<List<GiftListItem>> = giftListItemDao.getAll()

    suspend fun insertGiftListItem(giftListItem: GiftListItem) {
        giftListItemDao.insert(giftListItem)
    }

    suspend fun updateGiftListItem(giftListItem: GiftListItem) {
        giftListItemDao.update(giftListItem)
    }

    suspend fun deleteGiftListItem(giftListItem: GiftListItem) {
        giftListItemDao.delete(giftListItem)
    }

    suspend fun deleteAllGiftListItems() {
        giftListItemDao.deleteAll()
    }
}


