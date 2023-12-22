package com.example.uasmobileapp.db

import android.os.Parcel
import android.os.Parcelable

data class Film(
    var judul: String = "",
    var imageLink: String = "",
    var deskripsi: String = "",
    var sutradara: String = "",
    var docId : String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(judul)
        parcel.writeString(imageLink)
        parcel.writeString(deskripsi)
        parcel.writeString(sutradara)
        parcel.writeString(docId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Film> {
        override fun createFromParcel(parcel: Parcel): Film {
            return Film(parcel)
        }

        override fun newArray(size: Int): Array<Film?> {
            return arrayOfNulls(size)
        }
    }
}
