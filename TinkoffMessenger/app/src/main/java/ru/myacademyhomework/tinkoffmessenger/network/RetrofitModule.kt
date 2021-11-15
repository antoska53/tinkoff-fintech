package ru.myacademyhomework.tinkoffmessenger.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import ru.myacademyhomework.tinkoffmessenger.BuildConfig

object RetrofitModule {
    private val json: Json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()
    private val client = OkHttpClient().newBuilder()
        .authenticator(Authenticator { route, response ->
            val credentials: String = Credentials.basic(BuildConfig.USER_EMAIL, BuildConfig.API_KEY)
            response.request.newBuilder().header("Authorization", credentials).build()
        })
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    val chatApi: ChatApi = retrofit.create()
}

