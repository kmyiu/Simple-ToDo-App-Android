package com.kmyiu.simpletodoapp.zenquotes

import retrofit2.Call
import retrofit2.http.GET

interface ZenQuotesService {
    @GET("api/random/")
    fun getRandomQuote(): Call<List<ZenQuotesResponse?>>
}