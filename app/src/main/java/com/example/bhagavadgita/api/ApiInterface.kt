package com.example.bhagavadgita.api

import com.example.bhagavadgita.model.ChaptersItem
import com.example.bhagavadgita.model.VersesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("/v2/chapters/")
    fun getAllChapters(): Call<List<ChaptersItem>>

    @GET("/v2/chapters/{chapterNumber}/verses/")
    fun getVerses(@Path("chapterNumber") chapterNumber: Int): Call<List<VersesItem>>

    @GET("v2/chapters/{chapterNum}/verses/{verseNum}/")
    fun getParticularVerse(
        @Path("chapterNum") chapterNumber: Int,
        @Path("verseNum") verseNumber: Int
    ): Call<VersesItem>
}