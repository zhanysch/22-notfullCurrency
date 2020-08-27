package com.example.currencyh.data.remote

import com.example.currencyh.BuildConfig.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private var service: CurrencyService? = null

   fun getService (): CurrencyService? {  //  <- singlton глоб точка доступа к этому классу(2)
       if (service == null)
           service = buildRetrofit()
       return service
   }
    private fun buildRetrofit(): CurrencyService {  // (1)
       return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyService :: class.java)
    }
}