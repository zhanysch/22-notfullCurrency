package com.example.currencyh.data.remote


import com.example.currencyh.data.DataClass.CurrencyModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("latest")
    fun getCurrencies(@Query("access_key") key : String) : Call<CurrencyModel>
}
