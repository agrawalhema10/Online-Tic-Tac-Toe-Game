package com.airubiya.onlinetictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var database=FirebaseDatabase.getInstance()
    private var myref=database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun buLoginEvent(view:View){
        LoginToFirebase(etEmail.text.toString(),etPassword.text.toString())
    }
    fun LoginToFirebase(Email:String,Password:String){
        mAuth!!.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(this){
            task->
            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Successful Login", Toast.LENGTH_LONG).show()
                var currentUser = mAuth!!.currentUser
                if (currentUser!=null) {
                    myref.child("Users").child(SplitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                }
                LoadMain()
            }
            else{
                Toast.makeText(applicationContext,"Failed Login",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LoadMain()
    }

    fun  LoadMain(){

        var currentUser=mAuth!!.currentUser
        if (currentUser!=null){
            // save in database

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)

            startActivity(intent)
            finish()
        }


    }
    fun SplitString(str:String): String {
        var split=str.split("@")
        return split[0]
    }
}


