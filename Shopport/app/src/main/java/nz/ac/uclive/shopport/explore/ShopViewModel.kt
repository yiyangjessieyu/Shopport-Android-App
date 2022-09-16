package nz.ac.uclive.shopport.explore

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.uclive.shopport.common.location.LocationDetails

class ShopViewModel : ViewModel() {
    private val _shopList = mutableStateListOf<Shop>()
    var errorMessage: String by mutableStateOf("")
    val shopList: List<Shop>
        get() = _shopList

    fun getShopList(location: LocationDetails) {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _shopList.clear()
                val response = apiService.getShops(location = location.latitude + "," + location.longitude)
                _shopList.addAll(response.results)

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}