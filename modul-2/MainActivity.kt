package com.example.tiptime

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    // inisialisasi variabel sebelum menggunakannya.
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inisialisasi objek yang dgunakan untuk mengakses view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //memanggil calculate Tip
        binding.calculateButton.setOnClickListener{ calculateTip() }

    }
    fun calculateTip(){
        // mengakses biaya layanan
        val stringInTextField = binding.costOfService.text.toString()
        // mengkonversi nilai dari teks menjadi desimal, karena int hanya bisa untuk menyimpan bilangan bulat.
        val cost = stringInTextField.toDouble()

        // metode mendaptkan persentsi tip
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        //membulatkan dan menghitung tip
        var tip = tipPercentage * cost
        val roundUp = binding.roundUpSwitch.isChecked
        if (roundUp) {
            tip = ceil(tip)
            }
        // memformat tip
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }
}
