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
    val rating: Double,

    @SerializedName("vicinity")
    val vicinity: String,

    @SerializedName("geometry")
    val geometry: Geometry
)

data class OpeningHours (
    @SerializedName("open_now")
    val openNow: Boolean
)

data class Geometry (
    @SerializedName("location")
    val location: LatLng,

    @SerializedName("viewport")
    val viewport: Viewport
)

data class Viewport (
    @SerializedName("northeast")
    val northeast: LatLng,

    @SerializedName("southwest")
    val southwest: LatLng,
)

data class LatLng (
    @SerializedName("lat")
    val lat: Number,

    @SerializedName("lng")
    val lng: Number,
)