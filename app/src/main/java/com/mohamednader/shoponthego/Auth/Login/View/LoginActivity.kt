package com.mohamednader.shoponthego.Auth.Login.View

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.example.Addresses
import com.example.example.Customerre
import com.example.example.PostCustomer
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohamednader.shoponthego.Auth.SignUp.View.SignUpActivity
import com.mohamednader.shoponthego.Auth.SignUp.ViewModel.SignUpViewModel
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.View.HomeFragment
import com.mohamednader.shoponthego.MainHome.View.MainHomeActivity
import com.mohamednader.shoponthego.MainHome.View.MainHomeActivity
import com.mohamednader.shoponthego.Model.Pojo.customer.Customer
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "SignUP_INFO_TAG"

    private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var loadingBar :ProgressDialog
    private lateinit var progressDialog :ProgressDialog
    private lateinit var binding: ActivityLoginBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var factory: GenericViewModelFactory
    lateinit var customer: Customer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        firebaseAuth = Firebase.auth
        initViews()
        val currentUser = firebaseAuth.currentUser

        val customerId = intent.getLongExtra("CustomerID",0)
        println("sssssssssssssssssssssssss"+customerId)


        progressDialog = ProgressDialog(this)
        val preferences = getSharedPreferences("user", MODE_PRIVATE)
        binding.login.setOnClickListener {

            userLogin(

                binding.emailEditText.getText().toString().trim { it <= ' ' },
                binding.passwordEditText.getText().toString().trim { it <= ' ' })
        }
        binding.forgetpassword.setOnClickListener {
            showRecoverPasswordDialog()

        }
        binding.createnewAccout.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))

        }

        apicall()

    }

    private fun showRecoverPasswordDialog() {
        val linearLayout = LinearLayout(this)
        val emailEditText = EditText(this)
        emailEditText.hint = "E-mail"
        emailEditText.minEms = 10
        emailEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        linearLayout.addView(emailEditText)
        linearLayout.setPadding(10, 10, 10, 10)
        MaterialAlertDialogBuilder(this, androidx.transition.R.style.AlertDialog_AppCompat)
            .setTitle("change Password")
            .setMessage("After confirmation, you will receive an email with a link to change your password")
            .setView(linearLayout)
            .setPositiveButton("Confrimation") { dialogInterface: DialogInterface?, i: Int ->
                val email = emailEditText.text.toString().trim { it <= ' ' }
                val emailPattern :Regex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+")
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (!email.matches(emailPattern)) {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                beginRecovery(email)
            }
            .setNeutralButton(
                "cancel"
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            .show()
    }
    private fun userLogin(email: String, password: String) {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
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
        progressDialog.setMessage("Signing in...")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                val preferences =
                    getSharedPreferences("user", MODE_PRIVATE)
                preferences.edit()
                    .putBoolean("flag", true).apply()
                val user = firebaseAuth.currentUser

                if (user?.isEmailVerified == true) {
                    // User's email address is verified
                    startActivity(Intent(this, MainHomeActivity::class.java)
                    )
                    Toast.makeText(this@LoginActivity, "Logged in successfully", Toast.LENGTH_SHORT).show()

                }
              else{
                    Toast.makeText(this@LoginActivity, "please verfiy your email", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()

                }
            } else {
//                Toast.makeText(LoginActivity.this, "Wrong email or password, or an error with the Internet connection", Toast.LENGTH_SHORT).show();
                Toast.makeText(
                    this@LoginActivity,
                    Objects.requireNonNull(task.exception)
                        !!.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
                progressDialog.dismiss()
            }
        }
    }

    private fun beginRecovery(email: String) {
        loadingBar = ProgressDialog(this)
        loadingBar.setMessage("Confirmation message is being sent")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            loadingBar.dismiss()
            if (task.isSuccessful()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Confirmation message sent successfully, check your email",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Failed to send confirmation message, this email is not registered",
                    Toast.LENGTH_LONG
                ).show()
            }
        }.addOnFailureListener { e -> loadingBar.dismiss() }
    }
    private fun apicall() {
        lifecycleScope.launch {

            signUpViewModel.product
                .collect { result ->
                    when (result) {
                        is ApiState.Success<Customerre> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.id}")


                        }
                        is ApiState.Loading -> {
//                                Log.i(TAG, "onCreate: Loading..."

                        }
                        is ApiState.Failure -> {
                            //hideViews()

                            Toast.makeText(
                                this@LoginActivity, "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }
    private fun initViews() {

        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(this),
                ConcreteSharedPrefsSource(this)
            )
        )

        signUpViewModel =
            ViewModelProvider(this, factory).get(SignUpViewModel::class.java)
    }

}
