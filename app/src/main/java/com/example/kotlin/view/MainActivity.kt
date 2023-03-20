package com.example.kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.kotlin.R
import com.example.kotlin.view.weatherlist.ThreadsFragment
import com.example.kotlin.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_threads->{
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ThreadsFragment.newInstance())
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}