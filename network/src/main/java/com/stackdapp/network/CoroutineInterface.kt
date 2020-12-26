package com.stackdapp.network

import retrofit2.Call
import retrofit2.http.*

interface CoroutineInterface {

    @GET("{hostEndPoint}")
    suspend fun retrieveData(@Path("hostEndPoint") endPoint: String, @QueryMap optionsMap: Map<String, Any>): Any

    @POST("{hostEndPoint}")
    suspend fun submitData(@Path("hostEndPoint") endPoint: String, @Body requestBody: Any): Any

    @PUT("{hostEndPoint}")
    suspend fun updateData(@Path("hostEndPoint") endPoint: String, @Body requestBody: Any): Any
}