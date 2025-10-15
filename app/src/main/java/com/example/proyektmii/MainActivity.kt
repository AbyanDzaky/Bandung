package com.example.proyektmii

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.proyektmii.ui.screens.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize card views
        try {
            findViewById<CardView>(R.id.card_pintu_masuk).setOnClickListener {
                startActivity(Intent(this, PintuMasukActivity::class.java))
            }

            findViewById<CardView>(R.id.card_parkir).setOnClickListener {
                startActivity(Intent(this, ParkingActivity::class.java))
            }

            findViewById<CardView>(R.id.card_destinasi).setOnClickListener {
                startActivity(Intent(this, KoperasiMenuActivity::class.java))
            }

            findViewById<CardView>(R.id.card_kantin).setOnClickListener {
                startActivity(Intent(this, CanteenMenuActivity::class.java))
            }

            findViewById<CardView>(R.id.card_ceksaldo).setOnClickListener {
                startActivity(Intent(this, CheckBalanceActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}