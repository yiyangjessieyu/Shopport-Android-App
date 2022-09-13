package nz.ac.uclive.shopport

import android.app.Application

class ShopportApplication : Application() {
    val database by lazy { ShopportDatabase.getDatabase(this) }
    val repository by lazy { ShopportRepository(database.wishListItemDao()) }

}