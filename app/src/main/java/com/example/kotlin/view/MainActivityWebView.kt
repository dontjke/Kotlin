package com.example.kotlin.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.databinding.ActivityMainWebViewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {
    lateinit var binding: ActivityMainWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ok.setOnClickListener {
            val urlText = binding.urlEditText.text.toString() // получили адрес
            val uri = URL(urlText) // создал uri
            val urlConnection: HttpsURLConnection =  // создание потока
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000 // время на подключение
                    readTimeout = 1000 // ожидание ответа
                }

            Thread { //открыли вспомогательный поток
                val buffer =
                    BufferedReader(InputStreamReader(urlConnection.inputStream)) //открываем соединение и забуферизировали
                val result = getLinesAsOneBigText(buffer)
                /*runOnUiThread{ //поток главный   1 способ
                    binding.webview.loadData(result,"text/html; utf-8", "utf-8")
                }*/
                Handler(Looper.getMainLooper()).post { //поток главный   2 способ
                    //  binding.webview.loadData(result,"text/html; utf-8", "utf-8")
                    binding.webview.settings.javaScriptEnabled = true
                    binding.webview.loadDataWithBaseURL(
                        null,
                        result,
                        "text/html; utf-8",
                        "utf-8",
                        null
                    )
                }

            }.start()

        }

    }

    private fun getLinesAsOneBigText(bufferedReader: BufferedReader): String {  //конвертер в строку
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
}