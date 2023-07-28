package com.mohamednader.shoponthego.Profile.View

import android.app.Activity
import android.content.Intent
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
import com.mohamednader.shoponthego.AddressConfig.View.AddressConfig
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.CurrencyAdapter
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.OnCurrencyClickListener
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Order.view.OrderActivity
import com.mohamednader.shoponthego.Profile.View.Addresses.AddressAdapter
import com.mohamednader.shoponthego.Profile.View.Addresses.OnAddressClickListener
import com.mohamednader.shoponthego.Profile.ViewModel.ProfileViewModel
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.BottomSheetDialogAddressesBinding
import com.mohamednader.shoponthego.databinding.BottomSheetDialogCurrenciesBinding
import com.mohamednader.shoponthego.databinding.FragmentProfileBinding
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

    //Addresses Bottom Sheet
    lateinit var addressBottomSheetBinding: BottomSheetDialogAddressesBinding
    lateinit var addressBottomSheetDialog: BottomSheetDialog
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressLinearLayoutManager: LinearLayoutManager

    //Needed Variables
    lateinit var currenciesList: List<CurrencyInfo>
    lateinit var addressesList: List<Address>
    lateinit var customer: Customer
    lateinit var customerId: String


    val ADDRESS_CONFIG_ACTIVITY_REQUEST_CODE: Int = 1234

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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



        binding.currency.setOnClickListener {
//            currencyAdapter.submitList(currenciesList)
            currencyBottomSheetDialog.show()
        }

        binding.moreText.setOnClickListener {
            val intent = Intent(requireContext(), OrderActivity::class.java)
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


                            addressesList = result.data.addresses!!
                            if (addressesList.isNotEmpty()) {
                                addressAdapter.submitList(addressesList)
                            } else {
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
                profileViewModel.updateCustomer.collect { result ->
                    when (result) {
                        is ApiState.Success<Customer> -> {
                            customer = result.data

                            addressesList = result.data.addresses!!
                            if (addressesList.isNotEmpty()) {
                                addressAdapter.submitList(addressesList)
                            } else {
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

/*
val address = Address(firstName = customer.firstName,
                                        lastName = customer.lastName,
                                        country = "Egypt",
                                        city = "Giza",
                                        address1 = "Haday2 El Aharm",
                                        phone = customer.phone)
 */

        }
    }

    override fun onCurrencyClickListener(currencyISO: String) {
        Toast.makeText(requireContext(), "you Clicked $currencyISO", Toast.LENGTH_SHORT).show()
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

        profileViewModel.deleteCustomerAddressOnNetwork(customer.id!!, addressId)
        profileViewModel.getCustomerByIdFromNetwork(customerID = customer.id!!)
        addressBottomSheetDialog.dismiss()
    }

}