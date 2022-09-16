package nz.ac.uclive.shopport.common

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.uclive.shopport.MainActivity
import nz.ac.uclive.shopport.common.location.LocationLiveData
import nz.ac.uclive.shopport.explore.APIService
import nz.ac.uclive.shopport.explore.Shop
import kotlin.reflect.KProperty

class LocationViewModel(context: Context): ViewModel() {
    private val locationLiveData = LocationLiveData(context)
    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }
}