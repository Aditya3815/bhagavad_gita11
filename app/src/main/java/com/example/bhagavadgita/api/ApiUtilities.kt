package com.example.bhagavadgita.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

object ApiUtilities {

    val headers = mapOf<String, String>(

        "Accept" to "application/json",
        "x-rapidapi-key" to "767896b08emsh10ea2be19cad56cp1e4292jsn99f04fb2bb59",
        "x-rapidapi-host" to "bhagavad-gita3.p.rapidapi.com"
    )

    val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder().apply {
                headers.forEach { key, value -> addHeader(key, value) }
            }.build()
            chain.proceed(request)
        }
    }.build()

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://bhagavad-gita3.p.rapidapi.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}