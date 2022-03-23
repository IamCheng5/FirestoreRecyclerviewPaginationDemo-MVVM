package com.iamcheng5.firestorerecyclerviewpaginationdemo_mvvm

sealed class Result<out R> {
    data class Success<out T>(val data:T): Result<T>()
    data class Error(val exception:Exception): Result<Nothing>()
    object Loading : Result<Nothing>()
}