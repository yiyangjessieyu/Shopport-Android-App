package nz.ac.uclive.shopport.database

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishlistViewModel (application: Application): AndroidViewModel(application) {

    private val shopportRepository: ShopportRepository

    val wishListItems: LiveData<List<WishListItem>>
//    val numWishListItems: LiveData<Int>

    init {
        val wishListDao = ShopportDatabase.getInstance(application).wishListItemDao()
        shopportRepository = ShopportRepository(wishListDao)

        wishListItems = shopportRepository.wishListItems
//        numWishListItems = shopportRepository.numWishListItems
    }

    fun addWishListItem(wishListItem: WishListItem)  {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.insert(wishListItem)
        }
    }

    fun updateWishListItem(wishListItem: WishListItem)  {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.update(wishListItem)
        }
    }

    fun deleteWishListItem(wishListItem: WishListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.delete(wishListItem)
        }
    }


}


class ShopportViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return WishlistViewModel(application) as T
    }
}
