package com.stackdapp.network.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.stackdapp.network.ConnectionLiveData
import com.stackdapp.network.models.ConnectionModel

/**
 * ViewModels which connects with the BaseActivity to get Network status
 */
class NetworkConnectivityViewModel(private val app: Application) : ViewModel() {

    /**
     * This method will initialize the LiveData with the BroadCast Receiver
     *
     * @return liveData
     */
    val connectionObservable: LiveData<ConnectionModel>? by lazy {
        ConnectionLiveData(app.applicationContext)
    }

}