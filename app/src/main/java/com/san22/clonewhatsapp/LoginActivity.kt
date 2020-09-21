package com.san22.clonewhatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*

val PhoneNumber="phoneno"
class LoginActivity : AppCompatActivity() {
    lateinit var dialogbox:MaterialAlertDialogBuilder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val nextb=findViewById<Button>(R.id.nextb)


       var phno:String

        nextb.setOnClickListener()
        {
            phno=et.text.toString()
            var ccpCountry=myccp.selectedCountryCodeWithPlus
            checkno(phno,ccpCountry)



        }


    }

    private fun checkno(phno: String, ccpCountry: String?) {

        if (phno.length==10)
        {

            val newphno=ccpCountry+phno
            notifyuser("We will be verifying this number do you want to continue or would you like to edit the number?",newphno)
        }
       else
        {
            Toast.makeText(this,"Incorrect phone number added",Toast.LENGTH_SHORT).show()
        }

    }

    fun notifyuser(string: String,phno: String)
    {
        dialogbox=MaterialAlertDialogBuilder(this).apply {
            setMessage(string)
            setPositiveButton("Ok")
            { _,_->
                startActivity( Intent(this@LoginActivity,OtpActivity::class.java).putExtra(
                    PhoneNumber,phno) )
                finish()
            }

            setNegativeButton("Edit")
            { dialog,_->
                dialog.dismiss()

            }
            setCancelable(false)
            create()
            show()
        }
    }
}