package com.example.kotlin.view

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotlin.MyApp
import com.example.kotlin.R
import com.example.kotlin.lesson10.MapsFragment
import com.example.kotlin.lesson6.MainService
import com.example.kotlin.lesson6.MyBroadcastReceiver
import com.example.kotlin.lesson6.ThreadsFragment
import com.example.kotlin.lesson9.WorkWithContentProviderFragment
import com.example.kotlin.utils.KEY_BUNDLE_ACTIVITY_MESSAGE
import com.example.kotlin.utils.KEY_SHARED_PREFERENCES_FILE_NAME_1
import com.example.kotlin.utils.KEY_SHARED_PREFERENCES_FILE_NAME_1_KEY_IS_RUSSIAN
import com.example.kotlin.utils.KEY_VIBE
import com.example.kotlin.view.weatherlist.HistoryWeatherListFragment
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

        startService(Intent(this, MainService::class.java).apply {//создали сервис
            putExtra(KEY_BUNDLE_ACTIVITY_MESSAGE, "привет сервис")
        })

        val receiver = MyBroadcastReceiver() //создали приемник
        registerReceiver(receiver, IntentFilter(KEY_VIBE)) //волна

        val sp = getSharedPreferences(KEY_SHARED_PREFERENCES_FILE_NAME_1, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(
            KEY_SHARED_PREFERENCES_FILE_NAME_1_KEY_IS_RUSSIAN,
            true
        ) // сохраняем в SP состояние кнопки локации Россия/мир
        editor.apply()

        val defaultValueIsRussian = true
        sp.getBoolean(KEY_SHARED_PREFERENCES_FILE_NAME_1_KEY_IS_RUSSIAN, defaultValueIsRussian)

        Thread {
            MyApp.getHistoryDao().getAll()
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_threads -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ThreadsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            R.id.action_history -> {
                navigate(HistoryWeatherListFragment.newInstance())
            }
            R.id.action_work_with_content_provider -> {
                navigate(WorkWithContentProviderFragment.newInstance())
            }
            R.id.action_google_maps -> {
                navigate(MapsFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigate(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment)
            .addToBackStack("")
            .commit()
    }
}