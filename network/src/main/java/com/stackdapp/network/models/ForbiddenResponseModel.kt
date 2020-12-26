package om.stackdapp.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class ForbiddenResponseModel : Serializable {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("messages")
    @Expose
    var messages: ArrayList<String>? = null

    companion object {
        private const val serialVersionUID = 2908913849342618175L
    }
}