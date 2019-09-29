package com.lunayoung.caramelo.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (val uid: String, val username: String, val profileImageUrl: String) : Parcelable {


    //유저 클래스 생성자
    constructor() : this("", "", "")


}