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
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

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

        val sharedPreferences = getSharedPreferences("MY_CITY", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString("CITY_NAME", "Wybierz miasto")

        val cityText = findViewById<EditText>(R.id.cityText)
        cityText.setText(value)

        val nextButton = findViewById<Button>(R.id.nextButton)
        val intent = Intent(this, Prognoza_miasta::class.java)

        val cities = listOf(
            "Warszawa", "Kraków", "Wrocław", "Gdańsk", "Londyn",
            "Paryż", "Rzym", "Pekin", "Madryt", "Berlin"
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

            if (city.isEmpty()){
                Toast.makeText(this, "Proszę podać nazwę miasta", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferences.edit().putString("CITY_NAME", city).apply()

                intent.putExtra("CITY_NAME", city)
                startActivity(intent)
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
            )
        } else {
            setCity()
        }
    }

        private fun setCity() {
            val cityText = findViewById<EditText>(R.id.cityText)
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


        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if (requestCode == 100 && grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                setCity()
            } else {
                Toast.makeText(this, "Brak zgody na lokalizację", Toast.LENGTH_SHORT).show()
            }
        }
    }
