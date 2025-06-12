package com.example.pogoda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Prognoza_miasta : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
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

        val yourCity = intent.getStringExtra("CITY_NAME") ?: ""
        findViewById<TextView>(R.id.cityTextView).text = yourCity

        val weatherTextView = findViewById<TextView>(R.id.weatherTextView)

        CoroutineScope(Dispatchers.Main).launch {
            val result = getWeatherFromApi(yourCity)
            if (result != null) {
                val json = JSONObject(result)

                val main = json.getJSONObject("main")
                val desc = json.getJSONArray("weather").getJSONObject(0).getString("description")
                val temp = main.getDouble("temp")
                val feelsLike = main.getDouble("feels_like")
                val humidity = main.getInt("humidity")
                val pressure = main.getInt("pressure")
                val wind = json.getJSONObject("wind")
                val windSpeed = wind.getDouble("speed")

                weatherTextView.text = """
                        Pogoda: $desc
                        Temperatura: $temp°C
                        Odczuwalna temp.: $feelsLike°C
                        Wilgotność: $humidity%
                        Ciśnienie: $pressure hPa
                        Wiatr: $windSpeed m/s
                    """.trimIndent()

            } else {
                weatherTextView.text = "Błąd pobierania danych."
            }
        }
    }

    private suspend fun getWeatherFromApi(yourCity: String):String {
        val apiKey = "91cf9b8d62d15304b4eada76ceced413"
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${yourCity}&appid=${apiKey}&units=metric&lang=pl"

        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseJSON = connection.inputStream.bufferedReader().readText()
                connection.disconnect()
                responseJSON

            } catch (e: Exception) {
                e.printStackTrace()
                "Błąd połączenia"
            }
        }
    }
}