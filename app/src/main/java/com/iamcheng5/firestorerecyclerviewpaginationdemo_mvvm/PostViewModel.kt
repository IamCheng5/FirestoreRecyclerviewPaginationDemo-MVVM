package com.iamcheng5.firestorerecyclerviewpaginationdemo_mvvm

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PostViewModel : ViewModel() {
    private val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + Dispatchers.IO
    private val repository: PostRepository = PostRepository()
    //get data once
    fun getPosts(reload:Boolean): LiveData<Result<List<Post>>> = liveData(coroutineContext) {
        emit(Result.Loading)
        val result = repository.getPosts(reload)
        if (result is Result.Success && result.data.isEmpty())
            emit(Result.Error(Exception("Data is empty.")))
        else
            emit(result)
    }

    fun addPost(post:Post): LiveData<Result<Boolean>> = liveData(coroutineContext) {
        emit(Result.Loading)
        emit(repository.addPost(post))
    }

    fun removePost(post:Post): LiveData<Result<Boolean>> = liveData(coroutineContext) {
        emit(Result.Loading)
        emit(repository.removePost(post))
    }
    //listen for realtime updates
    //fun getPosts(): LiveData<List<Post>> =repository.getPosts()
}
