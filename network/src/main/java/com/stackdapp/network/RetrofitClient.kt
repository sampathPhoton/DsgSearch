package com.stackdapp.network

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager


/**
 * Retrofit Client Class
 */
class RetrofitClient private constructor() {

    private val errorTag = RetrofitClient::class.java.simpleName
    private var authHttpClient: OkHttpClient? = null
    private var retrofitBuilder: Retrofit.Builder? = null
    private var nonAuthHttpClient: OkHttpClient? = null
    private var gsonConverterFactory: GsonConverterFactory? = null
    private val timeOut = 60 // in seconds

    @JvmField
    var isTimeOutDialogShown = false

    private fun getTaggedCall(request: Request, okHttpClient: OkHttpClient): Call {
        var req = request
        req = req.newBuilder().tag(arrayOf<Any?>(null)).build()
        val call: Call = okHttpClient.newCall(req)
        (req.tag() as Array<Any?>?)!![0] = call
        return call
    }

    @Synchronized
    fun getAuthService(baseUrl: String?): APIInterface {
        if (authHttpClient == null) {
            authHttpClient = getHttpClient()
        }
        if (retrofitBuilder == null) {
            retrofitBuilder = Retrofit.Builder()
        }
        if (gsonConverterFactory == null) {
            gsonConverterFactory = GsonConverterFactory.create()
        }
        return retrofitBuilder!!.baseUrl(baseUrl!!)
            .client(authHttpClient!!)
            .callFactory(object : Call.Factory {
                override fun newCall(request: Request): Call {
                    return getTaggedCall(request, authHttpClient!!)
                }
            })
            .addConverterFactory(gsonConverterFactory!!)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(APIInterface::class.java)
    }

    @Synchronized
    fun getCustomAuthService(baseUrl: String?, canSerializeNulls: Boolean): APIInterface {
        if (authHttpClient == null) {
            authHttpClient = getHttpClient()
        }
        if (retrofitBuilder == null) {
            retrofitBuilder = Retrofit.Builder()
        }
        if (gsonConverterFactory == null) {
            gsonConverterFactory = if (canSerializeNulls) {
                GsonConverterFactory.create(GsonBuilder().serializeNulls().create())
            } else {
                GsonConverterFactory.create()
            }
        }
        return retrofitBuilder!!.baseUrl(baseUrl!!)
            .client(authHttpClient!!)
            .callFactory(object : Call.Factory {
                override fun newCall(request: Request): Call {
                    return getTaggedCall(request, authHttpClient!!)
                }
            })
            .addConverterFactory(gsonConverterFactory!!)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(APIInterface::class.java)
    }

    private fun getHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient()
        val builder = okHttpClient.newBuilder()
        builder.connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeOut.toLong(), TimeUnit.SECONDS)
        //Temp Code to bypass SSL
        SSLTrustAll(builder)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY) //Log level
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    private fun isRequestUnauthorized(response: Response): Boolean {
        return response.code == 401
    }

    /**
     * This Method will trust all certificates
     *
     * @param builder
     */
    private fun SSLTrustAll(builder: OkHttpClient.Builder) {
        val trustAllCerts = arrayOf<X509TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            builder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0])
            builder.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
        } catch (e: NoSuchAlgorithmException) {
            Log.e(errorTag, e.toString())
        } catch (e: KeyManagementException) {
            Log.e(errorTag, e.toString())
        }
    }

    companion object {
        @JvmStatic
        var instance: RetrofitClient = RetrofitClient()
            private set
    }
}
