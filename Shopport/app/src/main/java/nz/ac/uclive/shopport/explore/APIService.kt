package nz.ac.uclive.shopport.explore

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val MAP_KEY = "AIzaSyB_7fydc7xSLuj7SWmpO4bvGWilso30YcU"
const val BASE_URL = "https://maps.googleapis.com/maps/api/"

interface APIService {
    @GET("place/nearbysearch/json")
    suspend fun getShops(
        @Query("location") location: String,
        @Query("rankby") rankby: String = "distance",
        @Query("keyword") keyword: String = "clothes",
        @Query("key") key: String = MAP_KEY,
    ): Response

    companion object {
        var apiService: APIService? = null
        fun getInstance(): APIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}