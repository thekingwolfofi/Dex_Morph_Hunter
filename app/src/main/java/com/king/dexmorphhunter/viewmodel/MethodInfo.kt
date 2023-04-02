package com.king.dexmorphhunter.model

import android.os.Parcel
import android.os.Parcelable

data class MethodInfo(
    val className: String?,
    val methodName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(className)
        parcel.writeString(methodName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MethodInfo> {
        override fun createFromParcel(parcel: Parcel): MethodInfo {
            return MethodInfo(parcel)
        }

        override fun newArray(size: Int): Array<MethodInfo?> {
            return arrayOfNulls(size)
        }
    }
}
