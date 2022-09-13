package nz.ac.uclive.shopport

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ShopportViewModel (application: Application): ViewModel() {

    private val shopportRepository: ShopportRepository

    val wishListItems: LiveData<List<WishListItem>>
    val numWishListItems: LiveData<Int>

    init {
        val shopportDb = ShopportDatabase.getInstance(application)
        val wishListDao = shopportDb.wishListItemDao()
        shopportRepository = ShopportRepository(wishListDao)

        wishListItems = shopportRepository.wishListItems
        numWishListItems = shopportRepository.numWishListItems
    }

    fun addWishListItem(wishListItem: WishListItem)  {
        shopportRepository.insert(wishListItem)
    }

    fun updateWishListItem(wishListItem: WishListItem)  {
        shopportRepository.update(wishListItem)
    }

    fun deleteWishListItem(wishListItem: WishListItem) {
        shopportRepository.delete(wishListItem)
    }


}


class ShopportViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ShopportViewModel(application) as T
    }
}
