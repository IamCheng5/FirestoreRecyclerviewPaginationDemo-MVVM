package com.iamcheng5.firestorerecyclerviewpaginationdemo_mvvm

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Post(@DocumentId val id:String,val title:String ,val description: String, val content: String,@ServerTimestamp val timeStamp:Date?=null):
    Parcelable {
    @Suppress("unused")
    constructor() : this("","", "", "")
}