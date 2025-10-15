package com.example.proyektmii.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.proyektmii.R

class KoperasiMenuActivity : AppCompatActivity() {
    private val TAG = "KoperasiMenuActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_menu)
        Log.d(TAG, "onCreate: Menu Koperasi dimulai.")

        try {
            val backButton = findViewById<ImageButton>(R.id.back_button_destinasi_menu)
            val cardAlatTulis = findViewById<CardView>(R.id.card_alat_tulis)
            val cardKebutuhanKantor = findViewById<CardView>(R.id.card_kebutuhan_kantor)

            backButton.setOnClickListener { onBackPressed() }

            // Kategori: Alat Tulis
            cardAlatTulis.setOnClickListener {
                Log.d(TAG, "onClick: Tombol 'Alat Tulis' ditekan.")
                val intent = Intent(this, KoperasiSelectionActivity::class.java)
                intent.putExtra("kategori", "alat_tulis")
                startActivity(intent)
            }

            // Kategori: Kebutuhan Kantor
            cardKebutuhanKantor.setOnClickListener {
                Log.d(TAG, "onClick: Tombol 'Kebutuhan Kantor' ditekan.")
                val intent = Intent(this, KoperasiSelectionActivity::class.java)
                intent.putExtra("kategori", "kebutuhan_kantor")
                startActivity(intent)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error saat inisialisasi view di onCreate", e)
            Toast.makeText(this, "Terjadi kesalahan pada aplikasi.", Toast.LENGTH_LONG).show()
        }
    }
}
