package com.stackdapp.network

import android.accounts.NetworkErrorException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.icollectcards.collect.util.network.NetworkCallHandler
import com.stackdapp.network.models.SecondaryStatusResponseModel
import om.stackdapp.network.models.PrimaryStatusResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitCallBack<Any>(tag: String, callback: RetrofitCallBackInterface<out Any?>?) : Callback<Any> {
    private var mCallback: RetrofitCallBackInterface<out Any?>? = callback
    override fun onResponse(call: Call<Any>, response: Response<Any?>) {

        val tag = getTag(call)
//        DialogUtils.instance.hideProgressBar(tag)
        removeMetaData(tag)
        if (call.isCanceled) {
            return
        }

        if (response.isSuccessful) {
            mCallback?.onSuccess(response)
        } else {
            val gson:Gson by lazy {
                GsonBuilder().serializeNulls().create()
            }
            response.errorBody()?.let { responseBody ->
                try {
                    val errorModel = gson.fromJson(responseBody.string(), PrimaryStatusResponseModel::class.java)
                    error(response.code(), errorModel?.status, tag)
                } catch (e: Exception) {
                    e.printStackTrace()
                    checkSecondaryStatusModel(gson, response, tag)
                }
            } ?: error(response.code(), null, tag)

        }
    }

    private fun checkSecondaryStatusModel(jsonBuilder: Gson, response: Response<Any?>, tag: String?) {
        try {
            val errorModel = jsonBuilder.fromJson(response.errorBody()!!.string(), SecondaryStatusResponseModel::class.java)
            errorModel?.messages?.isNotEmpty()?.let {notEmpty->
                if (notEmpty) error(response.code(), errorModel.messages!![0], tag) else error(response.code(), null, tag)
            } ?: error(response.code(), null, tag)
        } catch (e: Exception) {
            e.printStackTrace()
            error(response.code(), null, tag)
        }
    }

    override fun onFailure(call: Call<Any>, error: Throwable) {
        val tag = getTag(call)
//        DialogUtils.instance.hideProgressBar(tag)
        removeMetaData(tag)
        if (call.isCanceled) {
            mCallback?.onError(GENERAL_EXCEPTION_ERROR_CODE, NETWORK_ERROR)
            return
        }

        when (error) {
            is UnknownHostException ->{
                error(UNKNOWN_HOST_ERROR_CODE, NETWORK_ERROR, tag)
            }
            is NetworkErrorException -> {
                error(NETWORK_ERROR_CODE, NETWORK_ERROR, tag)
            }
            is SocketTimeoutException -> {
                error(TIME_OUT_ERROR_CODE, NETWORK_ERROR, tag)
            }
            is HttpException -> {
                error(HTTP_ERROR_CODE, NETWORK_ERROR, tag)
            }
            is IOException -> {
                error(IO_ERROR_CODE, NETWORK_ERROR, tag)
            }
            else -> {
                error(GENERAL_EXCEPTION_ERROR_CODE, NETWORK_ERROR, tag)
            }
        }
    }
    private fun error(errorCode: Int, errorMessage: String?, tag: String?) {
        val errMsg = errorMessage ?: NETWORK_ERROR
        mCallback?.onError(errorCode, errMsg)

    }

    private fun removeMetaData(tag: String?) {
        NetworkCallHandler.serviceTag.remove(tag)
        NetworkCallHandler.services.remove(tag)
        NetworkCallHandler.servicesCallBack.remove(tag)
    }

    private fun getTag(call: Call<Any>): String? {
        return (call.request().tag() as? Array<*>)!![0] as String
    }

    fun updateCallBackInterface(
        tag: String,
        callBackInterface: RetrofitCallBackInterface<out Any?>?
    ) {
        mCallback = callBackInterface
    }

    companion object {
        const val NETWORK_ERROR_CODE = 800
        const val TIME_OUT_ERROR_CODE = 801
        const val HTTP_ERROR_CODE = 802
        const val IO_ERROR_CODE = 803
        const val GENERAL_EXCEPTION_ERROR_CODE = 804
        const val UNKNOWN_HOST_ERROR_CODE = 805
        const val NETWORK_ERROR = "Service failed, kindly try again."
    }

    init {
        // Use it if required to pre initialize something
    }
}