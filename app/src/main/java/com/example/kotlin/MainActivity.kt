package com.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button: Button = findViewById(R.id.button)
        val title: TextView = findViewById(R.id.title_text_view)
        val description: TextView = findViewById(R.id.description_text_view)
        val helloWorld: TextView = findViewById(R.id.hello_world_text_view)


        button.setOnClickListener {
            Toast.makeText(this, "Hello Toast!", Toast.LENGTH_SHORT).show()
        }

        "Заголовок".also { title.text = it }
        description.text = "Описание"

        val data = Data()
        val data2 = data.copy() //скопировал свойства
        helloWorld.text = data2.toString()
        data2.title = "Заголовок2"
        data2.description = "Описание2"
        Log.d("mylogs", "$data")
        Log.d("mylogs", "$data2")

        for(i in 1..10) {
            Log.d("mylogs", "$i")
        }

        for(i in 10 downTo 1 step 2) {
            Log.d("mylogs", "$i")
        }





    }
}