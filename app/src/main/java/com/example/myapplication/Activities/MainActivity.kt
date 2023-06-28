package com.example.myapplication.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.example.myapplication.InputValidation
import com.example.myapplication.LoginSQL
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.title = "CDPC Manager"

        var db = LoginSQL(this)
        db.addName("", "SuperUser", "super@super.com",
            "password", "", "", "", "", "")
        db.addName("Example", "SPOC", "spoc@spoc.com",
            "password", "", "", "", "", "")

        val editEmailName: EditText = findViewById(R.id.emailID_edit)
        val editPassword: EditText = findViewById(R.id.password_edit)

        val textEmailName: TextInputLayout = findViewById(R.id.emailID_text)
        val textPassword: TextInputLayout = findViewById(R.id.password_text)

        val loginBtn : Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            if(!InputValidation.isInputEditEmailFilled(editEmailName, textEmailName, "Enter Valid Email")){
                return@setOnClickListener
            }
            if(!InputValidation.isInputEditTextFilled(editPassword, textPassword, "Enter Correct Password")){
                return@setOnClickListener
            }

            val email = editEmailName.text.toString().trim()
            val pass = editPassword.text.toString().trim()
            if(db.checkEmail(email)){
                Toast.makeText(this, "Email not Registered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(db.checkLogin(email,pass)){

                if(db.getUserType(email)=="SPOC"){
                    val intent = Intent(this, SPOCActivity::class.java)
                    intent.putExtra("key", email)
                    startActivity(intent)
                    finish()
                }
                else{
                    val intent = Intent(this, SuperUserActivity::class.java)
                    intent.putExtra("key", email)
                    startActivity(intent)
                    finish()
                }

            }else{
                Toast.makeText(this, "Invalid Email/Incorrect Password", Toast.LENGTH_SHORT).show()
            }

        }

    }

}