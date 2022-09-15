package nz.ac.uclive.shopport.explore

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ShopViewModel : ViewModel() {
    private val _shopList = mutableStateListOf<Shop>()
    var errorMessage: String by mutableStateOf("")
    val shopList: List<Shop>
        get() = _shopList

    fun getShopList() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _shopList.clear()
                val response = apiService.getShops(location = "-43.526883,172.574191")
                _shopList.addAll(response.results)

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}