package com.stackdapp.network.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * This is the POJO class to set Connection status
 */
class ConnectionModel (
    @field:SerializedName("isConnected") val isConnected: Boolean
) : Serializable {

    companion object {
        private const val serialVersionUID = 6306766549071013107L
    }
}