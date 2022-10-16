package nz.ac.uclive.shopport.date

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Data representation for a reminder we schedule and issue notifications for.
 */
data class DateItem(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("month")
    var month: Int = 0,
    @SerializedName("day_of_month")
    var day_of_month: Int = 0,
    @SerializedName("hour")
    var hour: Int = 0,
    @SerializedName("minute")
    var minute: Int = 0
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(id)
            writeString(name)
            writeInt(hour)
            writeInt(minute)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateItem

        if (id != other.id) return false
        if (name != other.name) return false
        if (hour != other.hour) return false
        if (minute != other.minute) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + hour
        result = 31 * result + minute
        return result
    }

    companion object CREATOR : Parcelable.Creator<DateItem> {
        override fun createFromParcel(source: Parcel): DateItem {
            return DateItem().apply {
                id = source.readInt()
                name = source.readString()
                hour = source.readInt()
                minute = source.readInt()
            }
        }

        override fun newArray(size: Int): Array<DateItem?> {
            return arrayOfNulls(size)
        }
    }


}
