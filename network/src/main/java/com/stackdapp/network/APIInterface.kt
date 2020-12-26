package com.stackdapp.network

import android.net.Uri
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.util.*

@JvmSuppressWildcards
interface APIInterface {

    @GET("/{hostEndPoint}")
    fun retrieveData(@Path(value ="hostEndPoint", encoded = true) endPoint: String, @QueryMap optionsMap: Map<String, Any>): Call<JsonObject>

    @POST("/{hostEndPoint}")
    fun submitData(@Path("hostEndPoint") endPoint: String, @Body requestBody: Any): Call<JsonObject>

    @PUT("/{hostEndPoint}")
    fun updateData(@Path("hostEndPoint") endPoint: String, @Body requestBody: Any): Call<JsonObject>

    @GET("/{hostEndPoint}")
    suspend fun getDataUsingCoroutine(@Path(value ="hostEndPoint", encoded = true) endPoint: String, @QueryMap optionsMap: Map<String, Any>): JsonObject

    @POST("/{hostEndPoint}")
    suspend fun submitDataUsingCoroutine(@Path("hostEndPoint") endPoint: String, @Body requestBody: Any): Any

    @PUT("/{hostEndPoint}")
    suspend fun updateDataUsingCoroutine(@Path("hostEndPoint") endPoint: String, @Body requestBody: Any): Any

}