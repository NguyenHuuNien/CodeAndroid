package com.hyunie.easychat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hbb20.CountryCodePicker
class LoginPhoneNumberActivity : AppCompatActivity() {
    lateinit var countryCodePicker : CountryCodePicker
    lateinit var phoneInput : EditText
    lateinit var sendOtpBtn : Button
    lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_phone_number)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        countryCodePicker = findViewById(R.id.login_countrycode)
        phoneInput = findViewById(R.id.login_mobile_number)
        sendOtpBtn = findViewById(R.id.send_otp_btn)
        progressBar = findViewById(R.id.login_progress_bar)

        countryCodePicker.registerCarrierNumberEditText(phoneInput)
        sendOtpBtn.setOnClickListener{
            if(!countryCodePicker.isValidFullNumber){
                phoneInput.setError("Phone number not valid")
                return@setOnClickListener
            }
            val intent = Intent(this, LoginOtpActivity::class.java)
            intent.putExtra("phone",countryCodePicker.fullNumberWithPlus)
            startActivity(intent)
        }
    }
}