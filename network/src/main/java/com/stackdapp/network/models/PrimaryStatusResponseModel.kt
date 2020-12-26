package om.stackdapp.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class PrimaryStatusResponseModel : Serializable {

    @SerializedName("status")
    @Expose
    var status: String? = null

}