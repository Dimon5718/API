package com.example.api

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    // Указываем адрес для обращения к API
    val URL = "https://api.incdb.com/jokes/random"
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextBtn.setOnClickListener{
            // Пока идёт загрузка "шутки", показывается progressBar
            runOnUiThread{
                progressBar.visibility = View.VISIBLE
            }
            // Отправляем запрос по URL
            val request: Request = Request.Builder().url(URL).build()
            okHttpClient.newCall(request).enqueue(object : Callback{
                // Функция сработает при ошибке  вмомент запроса
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("Error", e.toString())
                }

                // Функция сработает если ошибок нет
                override fun onResponse(call: okhttp3.Call, response: Response) {
                    // Получение шутки из JSON по ключам value -> joke
                    val joke = (JSONObject(response!!.body()!!
                        .string()).getJSONObject("value").get("joke")).toString()
                    //После загрузки "шутки", убираем progressBar и выводим "шутку" на экране
                    runOnUiThread{
                        progressBar.visibility = View.GONE
                        jock.text = joke
                    }
                }
            })
        }
    }
}
