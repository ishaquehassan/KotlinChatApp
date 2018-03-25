package com.sample.app2

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var auth:FirebaseAuth? = null
    var progressMsg:ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if(auth?.currentUser != null){
            gotoChat()
            return
        }

        progressMsg = ProgressDialog(this)
        progressMsg?.setCancelable(false)
        progressMsg?.setMessage("Authenticating... Please wait")

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        
        btnSignIn.setOnClickListener {
            if(runValidation(etEmail,etPassword)){
                signIn(etEmail.text.toString(),etPassword.text.toString())
            }
        }

        btnSignUp.setOnClickListener {
            if(runValidation(etEmail,etPassword)){
                signUp(etEmail.text.toString(),etPassword.text.toString())
            }
        }
    }

    private fun runValidation(e:EditText, p:EditText) : Boolean{
        if (etEmail.text.isEmpty()){
            etEmail.error = "Please enter Email"
            return false
        }
        if (etPassword.text.isEmpty()){
            etPassword.error = "Please enter Password"
            return false
        }

        return true
    }

    private fun signIn(email:String, password: String){
        progressMsg?.show()
        auth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener {task ->
            progressMsg?.dismiss()
            if(task.isSuccessful){
                Toast.makeText(this,"Signed In Successfully",Toast.LENGTH_LONG).show()
                gotoChat()
            }else{
                Toast.makeText(this,"Error ${ task.exception?.message}",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signUp(email:String, password:String){
        progressMsg?.show()
        auth?.createUserWithEmailAndPassword(email,password)?.addOnCompleteListener {task ->
            progressMsg?.dismiss()
            if(task.isSuccessful){
                Toast.makeText(this,"Signed Up Successfully",Toast.LENGTH_LONG).show()
                gotoChat()
            }else{
                Toast.makeText(this,"Error ${ task.exception?.message}",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun gotoChat(){
        startActivity(Intent(this,ChatActivity::class.java))
        finish()
    }

}
