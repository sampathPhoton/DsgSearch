package com.stackdapp.network

import retrofit2.Response

interface RetrofitCallBackInterface<T> {
    fun onSuccess(response: Response<out Any?>)
    fun onError(code: Int, error: String)
}