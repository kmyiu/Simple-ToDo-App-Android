package com.kmyiu.simpletodoapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmyiu.simpletodoapp.zenquotes.ZenQuotesResponse
import com.kmyiu.simpletodoapp.zenquotes.ZenQuotesService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteViewModel : ViewModel() {
    val quoteSentence: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            getQuote()
        }
    }

    fun refreshQuote() {
        quoteSentence.value = "Your next quote is..."
        getQuote()
    }

    private fun getQuote() {
        val retrofit = Retrofit.Builder().baseUrl("https://zenquotes.io/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: ZenQuotesService = retrofit.create(ZenQuotesService::class.java)
        val call: Call<List<ZenQuotesResponse?>> = service.getRandomQuote()
        call.enqueue(object : Callback<List<ZenQuotesResponse?>> {
            override fun onResponse(
                call: Call<List<ZenQuotesResponse?>>, response: Response<List<ZenQuotesResponse?>>
            ) {
                Log.d("ZenQuotes", "Response OK")
                val todayQuote: String? = response.body()?.get(0)?.q
                val author = response.body()?.get(0)?.a
                if (todayQuote != null) {
                    if (author != null && author.isNotEmpty())
                        quoteSentence.value = "$todayQuote by $author"
                    else
                        quoteSentence.value = "$todayQuote"
                }
            }

            override fun onFailure(call: Call<List<ZenQuotesResponse?>>, t: Throwable) {
                Log.d("ZenQuotes", "Response Fail")
                quoteSentence.value = "Tap me to get a next quote"
                Log.d(
                    "todayQuote", t.toString()
                )
            }
        })
    }

}
