package nz.ac.uclive.shopport.database

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishlistViewModel (application: Application): AndroidViewModel(application) {

    private val shopportRepository: ShopportRepository

    val wishListItems: LiveData<List<WishListItem>>

    init {
        val wishListDao = ShopportDatabase.getInstance(application).wishListItemDao()
        shopportRepository = ShopportRepository(wishListDao)

        wishListItems = shopportRepository.wishListItems
    }

    fun addWishListItem(wishListItem: WishListItem)  {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.insertWishListItem(wishListItem)
        }
    }

    fun updateWishListItem(wishListItem: WishListItem)  {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.updateWishListItem(wishListItem)
        }
    }

    fun deleteWishListItem(wishListItem: WishListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.deleteWishListItem(wishListItem)
        }
    }

    fun deleteAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.deleteAllWishListItems()
        }
    }


}


class ShopportViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return WishlistViewModel(application) as T
    }
}
