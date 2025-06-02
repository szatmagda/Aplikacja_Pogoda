package com.example.pogoda

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import android.Manifest
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cityText = findViewById<EditText>(R.id.cityText)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val intent = Intent(this, Prognoza_miasta::class.java)

        val cities = listOf(
            "Warszawa", "Kraków", "Wrocław", "Gdańsk", "Poznań",
            "Łódź", "Szczecin", "Lublin", "Katowice", "Białystok"
        )

        val cityListView = findViewById<ListView>(R.id.cityListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities)
        cityListView.adapter = adapter

        cityListView.setOnItemClickListener { _, _, position, _ ->
            val selectedCity = cities[position]
            intent.putExtra("CITY_NAME", selectedCity)
            startActivity(intent)
        }

        nextButton.setOnClickListener{
            val city = cityText.text.toString()
            intent.putExtra("CITY_NAME", city)
            startActivity(intent)
        }

        cityText.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            } else {
                val client = LocationServices.getFusedLocationProviderClient(this)
                client.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val city = address?.firstOrNull()?.locality ?: "Nieznane"
                        cityText.setText(city)
                    } else {
                        cityText.setText("Brak lokalizacji")
                    }
                }
            }
        }




        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cityText.performClick()
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }


    }





//    suspend fun getWeatherFromApi(url: String): String {
//        return withContext(Dispatchers.IO) {
//            try {
//                val connection = URL(url).openConnection() as HttpURLConnection
//                connection.requestMethod = "GET"
//                connection.connect()
//
//                val responseJSON = connection.inputStream.bufferedReader().readText()
//                connection.disconnect()
//                responseJSON
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                "Błąd połączenia"
//            }
//        }
//    }
}