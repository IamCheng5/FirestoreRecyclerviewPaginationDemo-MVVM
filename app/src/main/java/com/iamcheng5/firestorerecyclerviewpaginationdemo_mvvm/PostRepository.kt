package com.iamcheng5.firestorerecyclerviewpaginationdemo_mvvm

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PostRepository {
    companion object {
        private const val LIMIT = 10L
    }

    private val db: FirebaseFirestore = Firebase.firestore
    private var lastDocument: DocumentSnapshot? = null

    //get data once

    suspend fun getPosts(reload: Boolean): Result<List<Post>> = suspendCoroutine { cont ->
        if(reload) lastDocument = null
        var query: Query = db.collection("posts").orderBy("timeStamp",Query.Direction.ASCENDING).limit(LIMIT)
        lastDocument?.let {
            query = query.startAfter(it)
        }
        query.get()
            .addOnSuccessListener {
                try {
                    cont.resume(Result.Success(it.toObjects()))
                    if(!it.isEmpty)
                        lastDocument = it.documents[it.documents.size-1]
                } catch (ex: Exception) {
                    cont.resume(Result.Error(ex))
                }
            }.addOnFailureListener { ex ->
                cont.resume(Result.Error(ex))
            }
    }

    suspend fun addPost(post: Post): Result<Boolean> = suspendCoroutine { cont ->
        db.collection("posts").add(post)
            .addOnCompleteListener { task ->
                cont.resume(Result.Success(task.isSuccessful))
            }
    }

    suspend fun removePost(post: Post): Result<Boolean> = suspendCoroutine { cont ->
        db.collection("posts").document(post.id).delete()
            .addOnCompleteListener { task ->
                cont.resume(Result.Success(task.isSuccessful))
            }
    }
    //listen for realtime updates
    /*
    private val list = MutableLiveData<List<Post>>()
    private val posts = mutableListOf<Post>()
    init{
        db.collection("posts").orderBy("timeStamp",Query.Direction.ASCENDING).addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(ContentValues.TAG, "Listen error", error)
                    return
                }
                posts.clear()
                value?.let {
                    posts.addAll(it.toObjects())
                }
                list.postValue(posts)
                /*
                for (change in value!!.documentChanges) {
                    if (change.type == DocumentChange.Type.ADDED) {
                        Log.d(ContentValues.TAG, "data:" + change.document.data)
                    }
                    val source = if (value.metadata.isFromCache)
                        "local cache"
                    else
                        "server"
                    Log.d(ContentValues.TAG, "Data fetched from $source")
                }*/

            }

        })
    }
    fun getPosts(): LiveData<List<Post>>{
        return list
    }
    */

}
