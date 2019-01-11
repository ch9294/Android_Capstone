package ch9294.kr.ac.kmu.activityex1

import android.os.Parcel
import android.os.Parcelable

class MemberInfo(
    var _id: String?,
    var _name: String?,
    var _age: String?,
    var _sex: String?,
    var _tel: String?
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MemberInfo> = object : Parcelable.Creator<MemberInfo> {
            override fun createFromParcel(source: Parcel?): MemberInfo {
                return MemberInfo(source?.readString(),source?.readString(),source?.readString(),source?.readString(),source?.readString())
            }

            override fun newArray(size: Int): Array<MemberInfo?> {
                return arrayOfNulls<MemberInfo>(size)
            }
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(_id)
        dest?.writeString(_name)
        dest?.writeString(_age)
        dest?.writeString(_sex)
        dest?.writeString(_tel)
    }

    override fun describeContents(): Int {
        return 0
    }
}