package com.mohamednader.shoponthego.Profile.View

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.mohamednader.shoponthego.AddressConfig.View.AddressConfig
import com.mohamednader.shoponthego.Auth.SignUp.View.SignUpActivity
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.CurrencyAdapter
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.OnCurrencyClickListener
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Order.view.OrderActivity
import com.mohamednader.shoponthego.Profile.View.Addresses.AddressAdapter
import com.mohamednader.shoponthego.Profile.View.Addresses.OnAddressClickListener
import com.mohamednader.shoponthego.Profile.ViewModel.ProfileViewModel
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.CustomProgress
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.Utils.convertCurrencyFromEGPTo
import com.mohamednader.shoponthego.databinding.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), OnCurrencyClickListener, OnAddressClickListener {

    val TAG = "ProfileFragment_INFO_TAG"

    private lateinit var binding: FragmentProfileBinding

    //View Model Members
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var factory: GenericViewModelFactory

    //Currencies Bottom Sheet
    lateinit var currencyBottomSheetBinding: BottomSheetDialogCurrenciesBinding
    lateinit var currencyBottomSheetDialog: BottomSheetDialog
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var currencyLinearLayoutManager: LinearLayoutManager

    //Language Bottom Sheet
    lateinit var languageBottomSheetBinding: BottomSheetDialogLanguageBinding
    lateinit var languageBottomSheetDialog: BottomSheetDialog

    //Addresses Bottom Sheet
    lateinit var addressBottomSheetBinding: BottomSheetDialogAddressesBinding
    lateinit var addressBottomSheetDialog: BottomSheetDialog
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressLinearLayoutManager: LinearLayoutManager

    //Needed Variables
    lateinit var currenciesList: List<CurrencyInfo>
    lateinit var addressesList: MutableList<Address>
    lateinit var customer: Customer
    lateinit var customerId: String
    private lateinit var customProgress: CustomProgress
    private lateinit var firebaseAuth: FirebaseAuth
    var totalPriceOrder = 0.0

    var currencyISO = "EGP"
    var currencyRate = 1.0

    val ADDRESS_CONFIG_ACTIVITY_REQUEST_CODE: Int = 1234

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()

    }

    private fun initViews() {

        //View Model
        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteDataStoreSource(requireContext())))
        profileViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        //Progress Bar
        customProgress = CustomProgress.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        //Currency Bottom Sheet
        currencyAdapter = CurrencyAdapter(requireContext(), this)
        currencyLinearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        currencyBottomSheetBinding = BottomSheetDialogCurrenciesBinding.inflate(layoutInflater)
        currencyBottomSheetDialog = BottomSheetDialog(requireContext())
        currencyBottomSheetDialog.setContentView(currencyBottomSheetBinding.root)
        currencyBottomSheetBinding.currenciesRecyclerView.apply {
            adapter = currencyAdapter
            layoutManager = currencyLinearLayoutManager
        }

        //Address Bottom Sheet
        addressAdapter = AddressAdapter(requireContext(), this, "Profile")
        addressLinearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        addressBottomSheetBinding = BottomSheetDialogAddressesBinding.inflate(layoutInflater)
        addressBottomSheetDialog = BottomSheetDialog(requireContext())
        addressBottomSheetDialog.setContentView(addressBottomSheetBinding.root)
        addressBottomSheetBinding.addressesRecyclerView.apply {
            adapter = addressAdapter
            layoutManager = addressLinearLayoutManager
        }



        languageBottomSheetBinding = BottomSheetDialogLanguageBinding.inflate(layoutInflater)
        languageBottomSheetDialog = BottomSheetDialog(requireContext())
        languageBottomSheetDialog.setContentView(languageBottomSheetBinding.root)


        binding.language.setOnClickListener {
            languageBottomSheetDialog.show()
        }


        languageBottomSheetBinding.englishSelect.setOnClickListener {

            //Change Language
            binding.language.text = "Language: English"
            languageBottomSheetDialog.dismiss()
        }
        languageBottomSheetBinding.arabicSelect.setOnClickListener {

            //Change Language
            binding.language.text = "Language: Arabic"
            languageBottomSheetDialog.dismiss()
        }


        binding.logoutBtn.setOnClickListener {

            AlertDialog.Builder(requireContext()).setMessage("Do you want logout?")
                .setCancelable(false).setPositiveButton("Yes, Log me Out!") { dialog, _ ->

                    firebaseAuth.signOut()
                    val intent = Intent(requireContext(), SignUpActivity::class.java)
                    requireActivity().finish()

                    Toast.makeText(
                            requireContext(), "Nice to Meet You!", Toast.LENGTH_SHORT
                    ).show()
                }.setNegativeButton("No, Stay here", null).show()

        }

        binding.currency.setOnClickListener {
//            currencyAdapter.submitList(currenciesList)
            currencyBottomSheetDialog.show()
        }

        binding.moreText.setOnClickListener {
            val intent = Intent(requireContext(), OrderActivity::class.java)
            startActivity(intent)
        }

        binding.helpCenter.setOnClickListener {
            val url =
                "https://www.shopify.com/tools/policy-generator/show/JhwuMn7qlBI=--jO2CKCES9S5LzlG+--ph6FD+hOf1wTiILu5k1NpA==?utm_campaign=growth_tools&utm_content=policy&utm_medium=email&utm_source=sendgrid"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


        binding.address.setOnClickListener {
//            addressAdapter.submitList(addressesList)
            addressBottomSheetDialog.show()
        }

        addressBottomSheetBinding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddressConfig::class.java)
            startActivityForResult(intent, ADDRESS_CONFIG_ACTIVITY_REQUEST_CODE)
            addressBottomSheetDialog.dismiss()
        }


        apiRequests()

    }

    @SuppressLint("SetTextI18n")
    private fun apiRequests() {
        lifecycleScope.launchWhenStarted {

            launch {
                profileViewModel.getStringDS(Constants.customerIdKey).asLiveData()
                    .observe(requireActivity()) { customerID ->
                        // Update UI with the retrieved name
                        Log.i(TAG, "apiRequests: VIP: $customerID")
                        customerId = customerID!!
                        profileViewModel.getCustomerByIdFromNetwork(customerId.toLong())
                        profileViewModel.getAllCurrenciesFromNetwork()
                    }
            }


            launch {

                profileViewModel.currencyRes.collect { result ->
                    when (result) {
                        is ApiState.Success<List<CurrencyInfo>> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.get(0).iso}}")
                            currenciesList = result.data
                            currencyAdapter.submitList(currenciesList)
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(requireContext(),
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }



            launch {
                profileViewModel.customer.collect { result ->
                    when (result) {
                        is ApiState.Success<Customer> -> {
                            customer = result.data

                            binding.nameText.text = customer.firstName
                            binding.emailText.text = customer.email

                            if (customer.lastOrderId != null) {
                                profileViewModel.getOrderByIdFromNetwork(customer.lastOrderId!!)
                                binding.emptyOrder.visibility = View.GONE
                            } else {
                                binding.orderCardView.visibility = View.INVISIBLE
                                binding.emptyOrder.visibility = View.VISIBLE
                            }



                            addressesList = result.data.addresses!!
                            if (addressesList.isNotEmpty()) {
                                addressAdapter = AddressAdapter(requireContext(),
                                        this@ProfileFragment,
                                        "Profile")
                                addressBottomSheetBinding.addressesRecyclerView.apply {
                                    adapter = addressAdapter
                                    layoutManager = addressLinearLayoutManager
                                }
                                addressAdapter.submitList(addressesList)
                                addressBottomSheetBinding.emptyMsg.visibility = View.GONE
                            } else {
                                addressBottomSheetBinding.emptyMsg.visibility = View.VISIBLE
                                Log.i(TAG, "onCreate: Success...The list is empty}")
                                Toast.makeText(requireContext(),
                                        "There is no Address, please add one",
                                        Toast.LENGTH_SHORT).show()
                            }

                        }
                        is ApiState.Loading -> {

                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(requireContext(),
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            launch {
                profileViewModel.order.collect { result ->
                    when (result) {
                        is ApiState.Success<Order> -> {
                            val order = result.data


                            binding.itemDate.text = "Created At = ${order.created_at}"
                            binding.itemAddress.text = "Order Number = ${order.number.toString()}"
                            binding.itemPhone.text = "Name = ${order.billing_address?.firstName} "
                            binding.itemId.text = "Order Id = ${order.id.toString()}"
                            totalPriceOrder = order.current_total_price!!.toDouble()
                            binding.itemTotalPriceUsd.text = "Total price = ${
                                convertCurrencyFromEGPTo((order.current_total_price)!!.toDouble(),
                                        currencyRate)
                            } $currencyISO"

                            customProgress.hideProgress()
                            binding.orderCardView.visibility = View.VISIBLE
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                            customProgress.showDialog(requireContext(), false)
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(requireContext(),
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            launch {
                profileViewModel.getStringDS(Constants.currencyKey).collect() { result ->
                    currencyISO = result ?: ""
                }
            }

            launch {
                profileViewModel.getStringDS(Constants.rateKey).collect() { result ->
                    currencyRate = result?.toDouble() ?: 1.0
                }
            }

            launch {
                profileViewModel.currencyRate.collect { result ->
                    when (result) {
                        is ApiState.Success<List<ToCurrency>> -> {
                            Log.i(TAG, "apiRequests: ${result.data}")
                            profileViewModel.saveStringDS(Constants.currencyKey,
                                    result.data.get(0).quotecurrency)
                            profileViewModel.saveStringDS(Constants.rateKey,
                                    result.data.get(0).mid.toString())

                            if (totalPriceOrder != 0.0) {
                                binding.itemTotalPriceUsd.text = "Total price = ${
                                    convertCurrencyFromEGPTo(totalPriceOrder,
                                            result.data.get(0).mid)
                                } ${result.data.get(0).quotecurrency}"
                            }

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(requireContext(),
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            launch {
                profileViewModel.getStringDS(Constants.currencyKey).collect() { result ->
                    binding.currency.text = "Currency: $result"
                }
            }






            launch {
                profileViewModel.updateCustomer.collect { result ->
                    when (result) {
                        is ApiState.Success<Customer> -> {
                            customer = result.data

                            addressesList = result.data.addresses!!
                            if (addressesList.isNotEmpty()) {
                                addressAdapter = AddressAdapter(requireContext(),
                                        this@ProfileFragment,
                                        "Profile")
                                addressBottomSheetBinding.addressesRecyclerView.apply {
                                    adapter = addressAdapter
                                    layoutManager = addressLinearLayoutManager
                                }
                                addressAdapter.submitList(addressesList)
                                addressBottomSheetBinding.emptyMsg.visibility = View.GONE
                            } else {
                                addressBottomSheetBinding.emptyMsg.visibility = View.VISIBLE
                                Log.i(TAG, "onCreate: Success...The list is empty}")
                                Toast.makeText(requireContext(),
                                        "There is no Address, please add one",
                                        Toast.LENGTH_SHORT).show()
                            }
                            delay(100)
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(requireContext(),
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADDRESS_CONFIG_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val resultAddress: Address = data?.getSerializableExtra("Address") as Address
            Log.i(TAG, "onActivityResult: $resultAddress")

            val updatedAddresses = customer.addresses
            updatedAddresses?.add(resultAddress)

            customer = customer.copy(addresses = updatedAddresses)
            profileViewModel.updateCustomerOnNetwork(customer.id!!,
                    SingleCustomerResponse(customer))
        }
    }

    override fun onCurrencyClickListener(currencyISO: String) {
        Toast.makeText(requireContext(), "you Clicked $currencyISO", Toast.LENGTH_SHORT).show()
        profileViewModel.getCurrencyConvertorFromNetwork("EGP", currencyISO)
        currencyBottomSheetDialog.dismiss()
    }

    override fun onAddressClickListener(addressId: Long) {
        TODO("Not yet implemented")
    }

    override fun onMakeDefaultClickListener(addressId: Long) {
        val updatedCustomer = customer
        updatedCustomer.addresses?.filter { data ->
            data.default == true
        }?.map { data ->
            data.default = false
        }

        updatedCustomer.addresses?.filter { data ->
            data.id == addressId
        }?.map { data ->
            data.default = true
        }
        Log.i(TAG, "onMakeDefaultClickListener: $updatedCustomer")
        profileViewModel.updateCustomerOnNetwork(customer.id!!, SingleCustomerResponse(customer))
        addressBottomSheetDialog.dismiss()
    }

    override fun onDeleteClickListener(addressId: Long) {

        AlertDialog.Builder(requireContext()).setMessage("Do you want to delete this Address")
            .setCancelable(false).setPositiveButton("Yes, Delete it") { dialog, _ ->

                profileViewModel.deleteCustomerAddressOnNetwork(customer.id!!, addressId)
                profileViewModel.getCustomerByIdFromNetwork(customerID = customer.id!!)
                addressBottomSheetDialog.dismiss()

                Toast.makeText(
                        requireContext(), "Address Deleted!", Toast.LENGTH_SHORT
                ).show()

            }.setNegativeButton("No, Keep it", null).show()

    }

}