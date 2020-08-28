package com.example.currencyh.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import com.example.currencyh.BuildConfig.API_KEY
import com.example.currencyh.R
import com.example.currencyh.data.DataClass.CurrencyModel
import com.example.currencyh.data.remote.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var currentPosition:Int = 0
    private var values = arrayListOf<String>()
    private var list = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListeners()
        setupNetwork()

    }

    private fun setupNetwork() {
        fetchCurrencys()
    }

    private fun setupListeners() {
        EditOne.doAfterTextChanged {// в edittext прописыв n число (!)
           // spTwo.selectedItemPosition   //!!
            if (it.toString().isNotEmpty())
            calculate(it.toString())}
            //EditTwo.setText(it.toString()) // это n число будет автоматом вписываться во 2 edittext

    }
    fun calculate(value: String){
        val result = (values[spTwo.selectedItemPosition].toDouble() * value.toDouble()/list[spOne.selectedItemPosition].toDouble())
        EditTwo.setText(result.toString())
    }


    private fun fetchCurrencys(){
        RetrofitBuilder.getService()?.getCurrencies(API_KEY)
            ?.enqueue(object: Callback<CurrencyModel>{
                override fun onResponse(
                    call: Call<CurrencyModel>,
                    response: Response<CurrencyModel>
                ) {
                  if (response.isSuccessful && response.body() !=null) {
                      workwithData(response.body())
                  } else{
                      Log.e("Network","Kein Data")
                  } }

                override fun onFailure(call: Call<CurrencyModel>, t: Throwable) {
                    Log.e("Network",t.localizedMessage)
                }
            } )
    }
   
    private fun workwithData(data: CurrencyModel?) {
        val keys = data?.rates?.keySet()?.toList()  // итог arraylist из ключей

        if (keys!= null){ // проверка на null для ошибки
            for (item in keys){
                values.add(data.rates.get(item).toString()) // итог список из значений
                list.add(data.rates.get(item).toString())

            }
        }
        val adapter = CurrencySpinnerAdapter(applicationContext, R.layout.item_spinner,keys!!)
        spOne.adapter = adapter
        spTwo.adapter = adapter

        spOne.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
               currentPosition = position
            } }



        spTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (EditOne.text.toString().isNotEmpty())
                    calculate(EditOne.text.toString())  // чтоб не выходила ошибака
            }
        }

        Log.e("Network","t.localizedMessage")
    }
}