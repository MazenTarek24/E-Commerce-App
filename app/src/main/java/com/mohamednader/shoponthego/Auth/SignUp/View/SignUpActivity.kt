package com.mohamednader.shoponthego.Auth.SignUp.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mohamednader.shoponthego.Auth.Login.View.LoginActivity
import com.mohamednader.shoponthego.Auth.Login.ViewModel.LoginViewModel
import com.mohamednader.shoponthego.Auth.SignUp.ViewModel.SignUpViewModel
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItem
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivitySignUpBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private val TAG = "SignUP_INFO_TAG"
    private var CustomerID: Long? = 0
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivitySignUpBinding
    lateinit var progressDialog: ProgressDialog
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var factory: GenericViewModelFactory
    var Addresses: List<Address>? = null
    lateinit var customer: Customer
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        firebaseAuth = FirebaseAuth.getInstance()

        customer = Customer()

        progressDialog = ProgressDialog(this)
        binding.signup.setOnClickListener {
            registerUser()
        }
        binding.signInTv.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
        apicall()
        apicallforcreateDraftOrder()

    }

    private fun registerUser() {
        val name: String = binding.usernameedtxt.text.toString().trim { it <= ' ' }
        val email: String = binding.emaileditText2.text.toString().trim { it <= ' ' }
        val password: String = binding.passwordeditText3.text.toString().trim { it <= ' ' }
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
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser
                    sendVerificationEmail()
                    customer = Customer(firstName = binding.usernameedtxt.text.toString(),
                            note = currentUser?.uid,
                            verifiedEmail = currentUser?.isEmailVerified,
                            email = currentUser?.email)


                    println(firebaseAuth.currentUser!!.uid)
                    signUpViewModel.createCustomer(SingleCustomerResponse(customer))
                    Toast.makeText(this@SignUpActivity,
                            "Account successfully created",
                            Toast.LENGTH_LONG).show()


                } else if (password.length < 6) {
                    Toast.makeText(this, "Password must be greater than 6", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                } else {
//                Toast.makeText(SignUpActivity.this, "This account is already registered or there is an error with the internet connection",Toast.LENGTH_LONG).show();
                    Toast.makeText(this@SignUpActivity,
                            Objects.requireNonNull(task.exception)?.localizedMessage,
                            Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            })

    }

    private fun initViews() {

        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this),
                ConcreteDataStoreSource(this)))
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        signUpViewModel = ViewModelProvider(this, factory).get(SignUpViewModel::class.java)
    }

    private fun apicall() {
        lifecycleScope.launch {

            signUpViewModel.customer.collect { result ->
                when (result) {
                    is ApiState.Success<Customer> -> {
                        Log.i(TAG, "onCreate: Success...{${result.data.id}")
                        CustomerID = result.data.id


                        val draftOrder = DraftOrder(
                                    lineItems = mutableListOf(LineItem(title = "favourite",
                                            price = "20.00",
                                            quantity = 2)),
                                    customer = Customer(result.data.id!!),
                                    note = "favDraft")
                        loginViewModel.saveStringDS(Constants.customerIdKey , CustomerID.toString())

                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java).apply {
                            putExtra("CustomerID", CustomerID)
                        }
                        startActivity(intent)
                        delay(100)
//                        finish()

                    }
                    is ApiState.Loading -> {
//                                Log.i(TAG, "onCreate: Loading..."

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@SignUpActivity,
                                "There Was An Error",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun sendVerificationEmail() {
        val user = firebaseAuth.currentUser

        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Verification email sent successfully
            } else {
                // Handle errors
            }
        }
    }

    private fun apicallforcreateDraftOrder() {
        lifecycleScope.launch {

            loginViewModel.customerList.collect { result ->
                when (result) {
                    is ApiState.Success<DraftOrder> -> {
                        Log.i(TAG,
                                "onCreate Login: Success..zzzzzzzzzzzzzzzzzzz.{${result.data.id}")

                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreatezzzzzzzzzzzzzzzz: Loading...")

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@SignUpActivity,
                                "There Was An Errorzzzzzzzzzzzzzzz",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}