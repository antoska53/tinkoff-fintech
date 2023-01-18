package ru.myacademyhomework.tinkoffmessenger.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
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
import ru.myacademyhomework.tinkoffmessenger.network.ChatApi
import javax.inject.Inject
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .authenticator(Authenticator { route, response ->
                val credentials: String = Credentials.basic(BuildConfig.USER_EMAIL, BuildConfig.API_KEY)
                response.request.newBuilder().header("Authorization", credentials).build()
            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}

class ApiClient @Inject constructor(retrofit: Retrofit) {
    val chatApi: ChatApi = retrofit.create()
}