package com.mohamednader.shoponthego.Auth.SignUp.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mohamednader.shoponthego.Auth.Login.View.LoginActivity
import com.mohamednader.shoponthego.MainHome.View.MainHomeActivity
import com.mohamednader.shoponthego.databinding.ActivitySignUpBinding
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var progressDialog: ProgressDialog

    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(getLayoutInflater())
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        println(firebaseAuth.currentUser!!.uid)
        if (currentUser != null) {
            startActivity(Intent(this, MainHomeActivity::class.java))
        }
        progressDialog = ProgressDialog(this)
        binding.signup.setOnClickListener {
            registerUser()
        }
        binding.signInTv.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

        }

    }

    private fun registerUser() {
        val name: String = binding.userEditText.text.toString().trim { it <= ' ' }
        val email: String = binding.emailEditText.text.toString().trim { it <= ' ' }
        val password: String = binding.passwordEditText.text.toString().trim { it <= ' ' }
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }
        progressDialog.setMessage("Account is being created...")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
            OnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Account successfully created",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else if (password.length < 6) {
                    Toast.makeText(this, "Password must be greater than 6", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                } else {
//                Toast.makeText(SignUpActivity.this, "This account is already registered or there is an error with the internet connection",Toast.LENGTH_LONG).show();
                    Toast.makeText(
                        this@SignUpActivity,
                        Objects.requireNonNull(task.exception)
                            !!.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    progressDialog.dismiss()
                }
            })
    }

}