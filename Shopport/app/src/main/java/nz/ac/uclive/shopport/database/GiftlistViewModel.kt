package nz.ac.uclive.shopport.database


import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GiftlistViewModel (application: Application): AndroidViewModel(application) {

    private val shopportRepository: GiftlistItemRepository

    val giftListItems: LiveData<List<GiftListItem>>

    init {
        val giftListDao = ShopportDatabase.getInstance(application).giftListItemDao()
        shopportRepository = GiftlistItemRepository(giftListDao)

        giftListItems = shopportRepository.giftListItems
    }

    fun addGiftListItem(giftListItem: GiftListItem)  {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.insertGiftListItem(giftListItem)
        }
    }

    fun updateGiftListItem(giftListItem: GiftListItem)  {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.updateGiftListItem(giftListItem)
        }
    }

    fun deleteGiftListItem(giftListItem: GiftListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.deleteGiftListItem(giftListItem)
        }
    }

    fun deleteAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            shopportRepository.deleteAllGiftListItems()
        }
    }

    fun getColorForPerson(person: String): Int {
        var color = Color.White.toArgb()
        viewModelScope.launch(Dispatchers.IO) {
            color =  shopportRepository.getColorForPerson(person)
        }
        return color
    }

    fun getAllForPersons(): LiveData<List<String>> {
        return shopportRepository.persons
    }
}


class GiftlistViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GiftlistViewModel(application) as T
    }
}
