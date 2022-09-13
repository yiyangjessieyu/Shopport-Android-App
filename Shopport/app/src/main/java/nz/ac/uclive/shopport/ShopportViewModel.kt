package nz.ac.uclive.shopport

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ShopportViewModel (private val shopportRepository: ShopportRepository): ViewModel() {

    val wishListItems: LiveData<List<WishListItem>> = shopportRepository.wishListItems.asLiveData()
    val numWishListItems: LiveData<Int> = shopportRepository.numWishListItems.asLiveData()

    fun addWishListItem(wishListItem: WishListItem) = viewModelScope.launch {
        shopportRepository.insert(wishListItem)
    }

    fun updateWishListItem(wishListItem: WishListItem) = viewModelScope.launch {
        shopportRepository.update(wishListItem)
    }

    fun deleteWishListItem(wishListItem: WishListItem) = viewModelScope.launch {
        shopportRepository.delete(wishListItem)
    }
}


class FriendsViewModelFactory(private val repository: ShopportRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShopportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
