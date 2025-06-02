package com.example.pogoda

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class Prognoza_miasta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prognoza_miasta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val returnButton = findViewById<Button>(R.id.returnButton)
        returnButton.setOnClickListener {
            finish()
        }

        val yourCity = intent.getStringExtra("CITY_NAME")
        findViewById<TextView>(R.id.cityTextView).text = "Twoje miasto - $yourCity"
    }
}


//        fun getWeather(city:String) {
//            val apiKey = "91cf9b8d62d15304b4eada76ceced413"
//            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric&lang=pl"
//
//            CoroutineScope(Dispatchers.Main).launch {
//
//            }
//
//        }
//
//    }
//}
//
//suspend fun getWeatherFromApi(url: String): String {
//    return withContext(Dispatchers.IO) {
//        try {
//            val connection = URL(url).openConnection() as HttpURLConnection
//            connection.requestMethod = "GET"
//            connection.connect()
//
//            val responseJSON = connection.inputStream.bufferedReader().readText()
//            connection.disconnect()
//            responseJSON
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            "Błąd połączenia"
//        }
//    }
//}
//
//
//    val city = "Warszawa"
//    val pogodaJSON = getWeatherFromApi("https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric&lang=pl")
//    println("Dane: $pogodaJSON")
//}