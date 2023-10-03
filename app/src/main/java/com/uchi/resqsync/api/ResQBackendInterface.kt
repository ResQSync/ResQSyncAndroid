package com.uchi.resqsync.api

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


interface ResQBackendInterface {
    @POST(API.SIGNUP_API_URL)
    fun registerUser(@Body request: RegistrationRequest): Call<RegistrationResponse>

    @POST(API.SIGNIN_API_URL)
    fun singInUser(@Body request: LoginRequest) : Call<LoginResponse>
}

object BackendService{

    val backendInstance: ResQBackendInterface
    init {
        val trustAllCertificates = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, SecureRandom())

        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        backendInstance = retrofit.create(ResQBackendInterface::class.java)
    }
}