package com.san22.clonewhatsapp

import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit

class OtpActivity() : AppCompatActivity() {
   lateinit var mycallback:PhoneAuthProvider.OnVerificationStateChangedCallbacks
   var mVerificationID: String? =null
    var phno:String?=null
    var mResendToken:PhoneAuthProvider.ForceResendingToken?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        setspannablestring()
         phno=intent.getStringExtra(PhoneNumber)
        phnotv.text="Verifying $phno"
        showTimer(30000)


       initview()
        if (phno != null) {
            startverify(phno!!)
        }

    }
    fun onClick( v:View)
    {
        when(v)
        {
            sendb->{
                Log.i("Button pressed","ok")
                signInWithPhoneAuthCredential(credential=PhoneAuthProvider.getCredential(mVerificationID!!,otpbox.text.toString()))

            }
            resendb->{
                Log.i("Button pressed","ok")
                if (mResendToken!=null)
                {    resendOTP(phno.toString()!!,mResendToken!!)
                    val waitingdialog=ProgressDialog(this)
                    waitingdialog.setMessage("Waitng to detect OTP automatically")
                    waitingdialog.setCancelable(true)
                    waitingdialog.setCanceledOnTouchOutside(true)
                    waitingdialog.show()
                }
            }
        }


    }

    private fun resendOTP(phno: String,mResendingToken: PhoneAuthProvider.ForceResendingToken) {
     PhoneAuthProvider.getInstance().verifyPhoneNumber(phno,30,TimeUnit.SECONDS,this,mycallback,mResendingToken)
    }

    private fun startverify(phno:String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phno, // Phone number to verify
            30, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            mycallback)
    }

    private fun initview() {

        val waitingdialog=ProgressDialog(this)
        waitingdialog.setMessage("Waitng to detect OTP automatically")
        waitingdialog.setCancelable(true)
        waitingdialog.setCanceledOnTouchOutside(true)
        waitingdialog.show()
        mycallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                val smscode:String?=credential.smsCode
                if (!smscode.isNullOrBlank())
                {
                    otpbox.setText(smscode)
                    waitingdialog.dismiss()
                }


                        signInWithPhoneAuthCredential(credential)




            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.


                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
                Toast.makeText(this@OtpActivity,"$e.localizedMessage",Toast.LENGTH_LONG).show()
                Log.e("Error",e.localizedMessage)

                // Show a message and update the UI
                // ...
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later
                    mVerificationID=verificationId
                mResendToken=token

                // ...
            }
        }


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
      val mauth=FirebaseAuth.getInstance()
        mauth.signInWithCredential(credential).addOnCompleteListener(this)
        { task->
            if (task.isSuccessful)
                startActivity(Intent(this,SignUp::class.java).addFlags(FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK))





        }
    }


    private fun setspannablestring() {
        val span=SpannableString(getString(R.string.myspannablestring))
        val clickableSpan=object :ClickableSpan()
        {
            override fun onClick(p0: View) {

             startActivity(Intent(this@OtpActivity,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }

        }
        span.setSpan(clickableSpan,span.length-("Wrong Number?").length,span.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        messagetv.movementMethod=LinkMovementMethod.getInstance()
        messagetv.setText(span)
    }


    private fun showTimer(time: Long)
    {
//        resendb.isEnabled=false

        val mycounter=object :CountDownTimer(time,1000)
        {
            override fun onTick(p0: Long) {
               val timeleft=p0
                countertv.text="Seconds left:"+timeleft/1000
            }

            override fun onFinish() {
                resendb.isEnabled=true
                countertv.text=""

            }

        }.start()

    }
}