package com.example.avendanoguevaragustavoadolfo_parcial2ej2

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var acelerometro: Sensor? = null

    private lateinit var lienzo: CLienzo
    private var x = 0f
    private var y = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lienzo = findViewById(R.id.ctrlLienzo)

        try {
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

            //Sensor Acelerómetro
            acelerometro = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager!!.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_GAME)
        }
        catch(e: Exception) {
            Toast.makeText(applicationContext, "Error: $e", Toast.LENGTH_SHORT)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        try {
            synchronized(this) {
                if(event.sensor == sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) {

                    if(resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
                        x = event.values[1]
                        y = event.values[0]
                    }
                    else if(resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                        x = event.values[0]
                        y = event.values[1]
                    }

                    if(Math.abs(x) > 0.25 || Math.abs(y) > 0.25) {
                        lienzo.setPosicionPelota(x, y)
                    }
                }
            }
        }
        catch(e: Exception) {
            Toast.makeText(applicationContext, "Error: $e", Toast.LENGTH_SHORT)
        }
    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) { }
}