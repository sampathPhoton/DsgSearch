package com.stackdapp.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * {
 * "status": "NOT_FOUND",
 * "type": null,
 * "messages": ["Not found: Resource not found: devtestphe2odep (User)"]
 * }
 */
class SecondaryStatusResponseModel : Serializable {

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("messages")
    @Expose
    var messages: List<String>? = null
}