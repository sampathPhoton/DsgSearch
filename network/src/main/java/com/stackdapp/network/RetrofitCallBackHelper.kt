package com.stackdapp.network

import androidx.appcompat.app.AlertDialog

class RetrofitCallBackHelper {
    var alertDialog: AlertDialog? = null
    companion object{
        val instance: RetrofitCallBackHelper by lazy {
            RetrofitCallBackHelper()
        }
    }
}