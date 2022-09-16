package nz.ac.uclive.shopport.common.location

data class LocationDetails (
    val longitude:String,
    val latitude:String,
    val string: String = "$latitude,$longitude"
)
