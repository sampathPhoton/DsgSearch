package com.icollectcards.collect.util.network

import com.google.gson.JsonObject
import com.stackdapp.network.RetrofitCallBack
import com.stackdapp.network.RetrofitCallBackInterface
import com.stackdapp.network.RetrofitClient
import retrofit2.Call

class NetworkCallHandler {

    data class Service(val call: Call<Any>, val attachedActivityName: String? = null)

    companion object {

        @JvmField
        val serviceTag: MutableSet<String> = mutableSetOf()

        @JvmField
        var services: MutableMap<String, Service> = mutableMapOf()
        var servicesCallBack: MutableMap<String, RetrofitCallBack<out Any>> = mutableMapOf()

        fun cancelAllTask() {
            val iterator = services.iterator()
            for ((taskName, task) in iterator) {
                if (task.call.isExecuted) {
                    task.call.cancel()
                    iterator.remove()
                    serviceTag.remove(taskName)
                    servicesCallBack.remove(taskName)
                }
            }
        }


        fun cancelTask(taskName: String) {
            val iterator = services.iterator()
            for ((name, task) in iterator) {
                if (taskName == name && task.call.isExecuted) {
                    task.call.cancel()
                    iterator.remove()
                    serviceTag.remove(taskName)
                    servicesCallBack.remove(taskName)
                }
            }
        }

        private fun setTag(call: Call<Any>, tagName: String): String {
            (call.request().tag() as? Array<Any?>)!![0] = tagName
            return tagName
        }

        private fun canEnqueue(call: Call<Any>, tagName: String, activityDisplayName: String): Boolean {
            val canEnqueue = serviceTag.add(setTag(call, tagName))
            if (canEnqueue) {
                services[tagName] = Service(call, activityDisplayName)
            }
            return canEnqueue
        }

        private fun qService(call: Call<Any>, tag: String, activityDisplayName: String, callBackInterface: RetrofitCallBackInterface<out Any?>?) {
            val canQ = canEnqueue(call, tag, activityDisplayName)
            if (canQ) {
                val callBack = RetrofitCallBack(tag, callBackInterface)
                servicesCallBack[tag] = callBack
                call.enqueue(callBack)
            } else {
                val callBack = servicesCallBack[tag] as RetrofitCallBack<Any>
                callBack.updateCallBackInterface(tag, callBackInterface)
            }
        }

        fun getDataUsingCallback(
            baseUrl: String, endPoint: String,
            options: Map<String, Any>, tagToCancel: String,
            activityDisplayName: String,
            callBackInterface: RetrofitCallBackInterface<JsonObject?>
        ) {
            val call = RetrofitClient.instance.getAuthService(baseUrl)
                    .retrieveData(endPoint, options)
            qService(call as Call<Any>, tagToCancel, activityDisplayName, callBackInterface)
        }
        suspend fun getData(baseUrl: String, endPoint: String, options: Map<String, Any>) : JsonObject {
            return RetrofitClient.instance.getAuthService(baseUrl)
                .getDataUsingCoroutine(endPoint, options)
        }
        suspend fun submitData(baseUrl: String, endPoint: String, request: Any) : Any {
            return RetrofitClient.instance.getAuthService(baseUrl)
                .submitDataUsingCoroutine(endPoint, request)
        }
        suspend fun updateData(baseUrl: String, endPoint: String, request: Any) : Any {
            return RetrofitClient.instance.getAuthService(baseUrl)
                .updateDataUsingCoroutine(endPoint, request)
        }
    }
}

