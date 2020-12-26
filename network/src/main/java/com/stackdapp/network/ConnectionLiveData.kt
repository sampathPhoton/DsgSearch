package com.stackdapp.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData
import com.stackdapp.network.models.ConnectionModel

class ConnectionLiveData(private val context: Context) : LiveData<ConnectionModel>() {

    /**
     * This Class will broadcast the NetworkInfo and sets the value in the LiveData.
     */
    private val networkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected = activeNetwork?.isConnectedOrConnecting
            if (isConnected == true) {
                postValue(ConnectionModel(true))
            } else {
                postValue(ConnectionModel(false))
            }

        }
    }

    /**
     * Registering the Receiver when LiveData is Active
     */
    override fun onActive() {
        super.onActive()
        val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(networkReceiver, filter)
    }

    /**
     * Un-Registering the Receiver when LiveData is InActive
     */
    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }
}