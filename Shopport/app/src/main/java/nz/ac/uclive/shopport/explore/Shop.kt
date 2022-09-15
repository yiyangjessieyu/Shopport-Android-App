package nz.ac.uclive.shopport.explore

import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("results")
    val results: List<Shop>,
)

data class Shop (
    @SerializedName("name")
    val name: String,

    @SerializedName("place_id")
    val placeId: String,

    @SerializedName("opening_hours")
    val openingHours: OpeningHours?,

    @SerializedName("rating")
    val rating: Double
)

data class OpeningHours (
    @SerializedName("open_now")
    val openNow: Boolean
)