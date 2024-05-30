package com.hyunie.easychat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hyunie.easychat.utils.AndroidUtil
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.scheduleAtFixedRate

class LoginOtpActivity : AppCompatActivity() {
    var timeoutSecends = 60L
    lateinit var verificartionCode : String
    lateinit var resendingToken : PhoneAuthProvider.ForceResendingToken
    lateinit var phoneNumber : String;
    lateinit var otpInput : EditText
    lateinit var nextBtn : Button
    lateinit var progressBar: ProgressBar
    lateinit var resendOtpTextView : TextView
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_otp)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        otpInput = findViewById(R.id.login_otp)
        progressBar = findViewById(R.id.login_progress_bar)
        nextBtn = findViewById(R.id.login_next_btn)
        resendOtpTextView = findViewById(R.id.resend_otp_textview)

        phoneNumber = intent.extras?.getString("phone")!!
        sendOtp(phoneNumber,false)
        nextBtn.setOnClickListener {
            var otp = otpInput.text.toString()
            var credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verificartionCode,otp)
            signIn(credential)
            setInProgress(true)
        }

        resendOtpTextView.setOnClickListener {
            sendOtp(phoneNumber,true)
        }
    }

    fun sendOtp(phoneNumber:String, isResend:Boolean){
        startResendTimer()
        setInProgress(true)
        var builder : PhoneAuthOptions.Builder = PhoneAuthOptions
            .newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSecends,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signIn(p0)
                    setInProgress(false)
                }
                override fun onVerificationFailed(p0: FirebaseException) {
                    AndroidUtil.showToast(this@LoginOtpActivity,"OTP verification failed")
                    setInProgress(false)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificartionCode = p0
                    resendingToken = p1
                    AndroidUtil.showToast(this@LoginOtpActivity,"OTP sent successfully")
                    setInProgress(false)
                }
            })
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build())
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    private fun startResendTimer() {
        resendOtpTextView.isEnabled = false
        val timer = Timer()

        timer.scheduleAtFixedRate(0, 1000) {
            timeoutSecends--
            resendOtpTextView.text = "Resend OTP in $timeoutSecends seconds"
            if (timeoutSecends <= 0L) {
                timeoutSecends = 60L
                timer.cancel()
                runOnUiThread {
                    resendOtpTextView.isEnabled = true
                }
            }
        }

    }

    private fun signIn(p0: PhoneAuthCredential) {
        setInProgress(true)
        mAuth.signInWithCredential(p0).addOnCompleteListener(object:OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                setInProgress(false)
                if(p0.isSuccessful){
                    var intent = Intent(this@LoginOtpActivity,LoginUsernameActivity::class.java)
                    intent.putExtra("phone",phoneNumber)
                    startActivity(intent)
                }
                else{
                    AndroidUtil.showToast(this@LoginOtpActivity,"OTP verification failed")
                }
            }
        })

    }

    fun setInProgress(inProgress:Boolean){
        if(inProgress){
            progressBar.visibility = View.VISIBLE
            nextBtn.visibility = View.GONE
        }else{
            progressBar.visibility = View.GONE
            nextBtn.visibility = View.VISIBLE
        }
    }
}