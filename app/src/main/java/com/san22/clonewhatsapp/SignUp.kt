package com.san22.clonewhatsapp

import android.app.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.iceteck.silicompressorr.SiliCompressor
import id.zelory.compressor.Compressor
import id.zelory.compressor.compressFormat
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.io.File

//var downloadurl:String=""
class SignUp : AppCompatActivity() {
    val storage by lazy {
        FirebaseStorage.getInstance()
    }
    val auth by lazy {
        FirebaseAuth.getInstance()
    }
    val database by lazy {
        FirebaseFirestore.getInstance()
    }
    private lateinit var downloadurl:String
    private lateinit var downloadurlthumb:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        buttnext.isEnabled=false
        myuserimage.setOnClickListener()
        { buttnext.isEnabled=false
            checkimagepermission()

        }
        buttnext.setOnClickListener()
        {

            val name=etname.text.toString()
            if (name.isNullOrBlank())
            {
                Toast.makeText(this,"Please enter your name",Toast.LENGTH_SHORT).show()
            }
            else{
               val user=user(name,downloadurl,downloadurl,auth.uid!!)
                database.collection("users").document(auth.uid!!).set(user).addOnSuccessListener {
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun checkimagepermission() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(
                    permission,
                    1001
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(
                    permissionWrite,
                    1002
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                pickImageFromGallery()
            }

        }
    }

    private fun pickImageFromGallery() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"

        startActivityForResult(intent,1000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && requestCode==1000)
        {
            data?.data?.let {
                 myuserimage.setImageURI(it)
                val actualimg=File(it.path)
                var thumbimg:Uri



                 uploadimage(it)
                
            }
        }
    }

    private fun uploadimage(it: Uri) {

       val ref=storage.reference.child("Uploads/"+auth.uid.toString())
        val uploadTask=ref.putFile(it)
        uploadTask.continueWith(Continuation<UploadTask.TaskSnapshot,Task<Uri>>
        {task->
            if (!task.isSuccessful)
            {
                task.exception?.let {
                    throw it
                }
            }

            return@Continuation ref.downloadUrl
        }).addOnCompleteListener{ task->

            if (task.isSuccessful)
            {    buttnext.isEnabled=true
                 task.result!!.addOnSuccessListener {
                     task2->
                     downloadurl=task2!!.toString()
                     val filepath=File(downloadurl)


                     Log.i("URL","Download URL: $downloadurl ")
                 }


            }
        }
    }





}