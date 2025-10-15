package com.example.proyektmii.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyektmii.R
import com.example.proyektmii.data.CartItem
import com.example.proyektmii.data.MenuItem
import com.example.proyektmii.data.local.AppPreferences
import java.text.NumberFormat
import java.util.Locale

class KoperasiSelectionActivity : AppCompatActivity() {

    private lateinit var appPreferences: AppPreferences
    private lateinit var grid: GridView
    private lateinit var continueButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var titleTextView: TextView

    private var cartItems = mutableListOf<CartItem>()
    private val TAG = "KoperasiSelection"

    // dua kategori (kamu bisa sesuaikan drawable names sesuai file di res/drawable)
    private val alatTulisItems = listOf(
        MenuItem("Pulpen Hitam", 5000, R.drawable.pen),
        MenuItem("Pensil 2B", 3000, R.drawable.pencil),
        MenuItem("Buku Tulis 40 Lbr", 8000, R.drawable.notebook),
        MenuItem("Penghapus", 2000, R.drawable.eraser),
        MenuItem("Penggaris", 4000, R.drawable.ruler)
    )

    private val kebutuhanKantorItems = listOf(
        MenuItem("Kertas A4 80gsm (1 rim)", 55000, R.drawable.paper),
        MenuItem("Map Snelhecter", 2500, R.drawable.folder),
        MenuItem("Stapler (Kecil)", 15000, R.drawable.stapler),
        MenuItem("Isi Staples (1000)", 5000, R.drawable.staples)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_selection)
        Log.d(TAG, "onCreate: KoperasiSelectionActivity started.")

        appPreferences = AppPreferences(this)
        grid = findViewById(R.id.destination_grid)
        continueButton = findViewById(R.id.lanjut_ke_keranjang_button)
        backButton = findViewById(R.id.back_button_destinasi_selection)
        titleTextView = findViewById(R.id.destination_selection_title)

        // kategori dikirim dari KoperasiMenuActivity, default alat_tulis
        val kategori = intent.getStringExtra("kategori") ?: "alat_tulis"
        val items = if (kategori.equals("alat_tulis", ignoreCase = true)) alatTulisItems else kebutuhanKantorItems

        // set judul layar (pakai string resource)
        titleTextView.text = when {
            kategori.equals("alat_tulis", ignoreCase = true) -> getString(R.string.koperasi_selection_title)
            else -> getString(R.string.koperasi_selection_title)
        }

        // load existing cart
        cartItems = appPreferences.getCartItems().toMutableList()

        grid.adapter = KoperasiAdapter(items)

        continueButton.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                appPreferences.saveCartItems(cartItems)
                startActivity(Intent(this, com.example.proyektmii.ui.screens.CartActivity::class.java))
            } else {
                Toast.makeText(this, "Keranjang kosong. Tambahkan barang terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener { onBackPressed() }
        updateContinueButtonState()
    }

    override fun onResume() {
        super.onResume()
        // reload cart (jika diubah dari tempat lain)
        cartItems = appPreferences.getCartItems().toMutableList()
        (grid.adapter as? KoperasiAdapter)?.notifyDataSetChanged()
        updateContinueButtonState()
    }

    private fun updateContinueButtonState() {
        continueButton.isEnabled = cartItems.isNotEmpty()
    }

    inner class KoperasiAdapter(private val items: List<MenuItem>) : BaseAdapter() {
        override fun getCount(): Int = items.size
        override fun getItem(position: Int): Any = items[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.grid_item_destination, parent, false)

            val item = items[position]

            // Optional image (layout mungkin tidak punya ImageView; ini aman)

            val nameTv = view.findViewById<TextView>(R.id.destination_name)
            val priceTv = view.findViewById<TextView>(R.id.destination_price)
            val addBtn = view.findViewById<Button>(R.id.add_button)
            val quantityLayout = view.findViewById<LinearLayout>(R.id.quantity_control)
            val minusBtn = view.findViewById<Button>(R.id.minus_button)
            val plusBtn = view.findViewById<Button>(R.id.plus_button)
            val qtyTv = view.findViewById<TextView>(R.id.quantity_text)

            nameTv.text = item.name
            priceTv.text = "Rp ${NumberFormat.getNumberInstance(Locale("in", "ID")).format(item.price)}"

            val existing = cartItems.find { it.menuItem.name == item.name }
            val currentQty = existing?.quantity ?: 0

            if (currentQty > 0) {
                addBtn.visibility = View.GONE
                quantityLayout.visibility = View.VISIBLE
                qtyTv.text = currentQty.toString()
            } else {
                addBtn.visibility = View.VISIBLE
                quantityLayout.visibility = View.GONE
            }

            addBtn.setOnClickListener {
                val updated = cartItems.toMutableList()
                val found = updated.find { it.menuItem.name == item.name }
                if (found != null) {
                    val idx = updated.indexOf(found)
                    updated[idx] = found.copy(quantity = found.quantity + 1)
                } else {
                    updated.add(CartItem(item, 1))
                }
                cartItems = updated
                appPreferences.saveCartItems(updated)
                notifyDataSetChanged()
                updateContinueButtonState()
            }

            minusBtn.setOnClickListener {
                val updated = cartItems.toMutableList()
                val found = updated.find { it.menuItem.name == item.name }
                if (found != null) {
                    if (found.quantity > 1) {
                        val idx = updated.indexOf(found)
                        updated[idx] = found.copy(quantity = found.quantity - 1)
                    } else {
                        updated.remove(found)
                    }
                    cartItems = updated
                    appPreferences.saveCartItems(updated)
                    notifyDataSetChanged()
                    updateContinueButtonState()
                }
            }

            plusBtn.setOnClickListener {
                val updated = cartItems.toMutableList()
                val found = updated.find { it.menuItem.name == item.name }
                if (found != null) {
                    val idx = updated.indexOf(found)
                    updated[idx] = found.copy(quantity = found.quantity + 1)
                } else {
                    updated.add(CartItem(item, 1))
                }
                cartItems = updated
                appPreferences.saveCartItems(updated)
                notifyDataSetChanged()
                updateContinueButtonState()
            }

            return view
        }
    }
}
