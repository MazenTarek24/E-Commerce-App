package com.mohamednader.shoponthego.Payment.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.MainHome.View.MainHomeActivity
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.AppliedDiscount
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.ShippingLine
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Payment.ViewModel.PaymentViewModel
import com.mohamednader.shoponthego.Profile.View.Addresses.AddressAdapter
import com.mohamednader.shoponthego.Profile.View.Addresses.OnAddressClickListener
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.CustomProgress
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.Utils.convertCurrencyFromEGPTo
import com.mohamednader.shoponthego.databinding.ActivityPaymentBinding
import com.mohamednader.shoponthego.databinding.BottomSheetDialogAddressesBinding
import com.mohamednader.shoponthego.databinding.BottomSheetDialogPaymentMethodBinding
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class PaymentActivity : AppCompatActivity(), OnAddressClickListener {

    val TAG = "PaymentActivity_INFO_TAG"

    private lateinit var binding: ActivityPaymentBinding

    //View Model Members
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var factory: GenericViewModelFactory

    //Addresses Bottom Sheet
    lateinit var addressBottomSheetBinding: BottomSheetDialogAddressesBinding
    lateinit var paymentMethodBottomSheetBinding: BottomSheetDialogPaymentMethodBinding
    lateinit var addressBottomSheetDialog: BottomSheetDialog
    lateinit var paymentMethodBottomSheetDialog: BottomSheetDialog
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressLinearLayoutManager: LinearLayoutManager

    //Needed Variables
    lateinit var draftOrder: DraftOrder
    lateinit var customer: Customer
    lateinit var addressesList: List<Address>
    lateinit var customerId: String
    lateinit var priceRuleList: List<PriceRules>
    private lateinit var customProgress: CustomProgress
    private var totalPrice = "0.0"
    var currencyISO = "EGP"
    var currencyRate = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initViews()

    }

    private fun initViews() {

        //View Model
        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this@PaymentActivity),
                ConcreteDataStoreSource(this@PaymentActivity)))
        paymentViewModel = ViewModelProvider(this, factory).get(PaymentViewModel::class.java)

        //Progress Bar
        customProgress = CustomProgress.getInstance()

        getCurrency()

        //Address Bottom Sheet
        addressAdapter = AddressAdapter(this@PaymentActivity, this, "Payment")
        addressLinearLayoutManager =
            LinearLayoutManager(this@PaymentActivity, RecyclerView.VERTICAL, false)
        addressBottomSheetBinding = BottomSheetDialogAddressesBinding.inflate(layoutInflater)
        addressBottomSheetDialog = BottomSheetDialog(this@PaymentActivity)
        addressBottomSheetDialog.setContentView(addressBottomSheetBinding.root)
        addressBottomSheetBinding.addressesRecyclerView.apply {
            adapter = addressAdapter
            layoutManager = addressLinearLayoutManager
        }
        addressBottomSheetBinding.fab.visibility = View.GONE



        paymentMethodBottomSheetBinding =
            BottomSheetDialogPaymentMethodBinding.inflate(layoutInflater)
        paymentMethodBottomSheetDialog = BottomSheetDialog(this@PaymentActivity)
        paymentMethodBottomSheetDialog.setContentView(paymentMethodBottomSheetBinding.root)






        binding.backArrowImg.setOnClickListener {
            onBackPressed()
        }

        binding.changePaymentMethod.setOnClickListener {
            paymentMethodBottomSheetDialog.show()
        }


        binding.changeAddressBtn.setOnClickListener {
            addressBottomSheetDialog.show()
        }

        binding.applyPromoCodeBtn.setOnClickListener {
            val code = binding.promoCodeEditText.text.toString()
            val priceRule: PriceRules? = priceRuleList.find { priceRuleItem ->
                priceRuleItem.title == code
            }
            if (priceRule == null) {
                Toast.makeText(this@PaymentActivity, "Invalid Code", Toast.LENGTH_SHORT).show()
            } else if (priceRule.targetType == "shipping_line") {
                draftOrder =
                    draftOrder.copy(shippingLine = ShippingLine(custome = true, price = "0.0"))
                Log.i(TAG, "initViews: DRAFT ORDER $draftOrder")
                paymentViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                        SingleDraftOrderResponse(draftOrder))
                Toast.makeText(this@PaymentActivity, "Accepted Code", Toast.LENGTH_SHORT).show()
            } else {
                val appliedDiscount: AppliedDiscount = AppliedDiscount(title = priceRule.title,
                        description = "Given Discount",
                        value = abs(priceRule.value.toDouble()).toString(),
                        value_type = priceRule.valueType,
                        amount = "0.0")
                draftOrder = draftOrder.copy(appliedDiscount = appliedDiscount)
                Log.i(TAG, "initViews: DRAFT ORDER $draftOrder")
                paymentViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                        SingleDraftOrderResponse(draftOrder))
                Toast.makeText(this@PaymentActivity, "Accepted Code", Toast.LENGTH_SHORT).show()
            }

        }

        binding.placeOrderBtn.setOnClickListener {
            if (addressesList.isNotEmpty()) {
                if (totalPrice.toDouble() > 2000.00) {
                    Toast.makeText(this@PaymentActivity,
                            "Cannot use COD for orders > 2000, Please use PayPal",
                            Toast.LENGTH_LONG).show()
                } else {
                    getCompleteDraftOrder()
                    paymentViewModel.completeDraftOrderPendingByID(draftOrder.id!!)
                }
            } else {
                Toast.makeText(this@PaymentActivity,
                        "There is no Address, please add one",
                        Toast.LENGTH_SHORT).show()
            }
        }

        paymentMethodBottomSheetBinding.cashSelectBtn.setOnClickListener {
            binding.paymentMethodText.setText("Cash On Delivery")
            binding.paymentButtonContainer.visibility = View.GONE
            binding.placeOrderBtn.visibility = View.VISIBLE
            paymentMethodBottomSheetDialog.dismiss()
        }
        paymentMethodBottomSheetBinding.paypalSelectBtn.setOnClickListener {
            binding.paymentMethodText.setText("Using PayPal")
            binding.paymentButtonContainer.visibility = View.VISIBLE
            binding.placeOrderBtn.visibility = View.GONE
            initPayPalPaymentButton()
            paymentMethodBottomSheetDialog.dismiss()
        }

    }

    private fun getCurrency() {
        paymentViewModel.getStringDS(Constants.currencyKey).asLiveData()
            .observe(this@PaymentActivity) { result ->
                currencyISO = result ?: ""

                paymentViewModel.getStringDS(Constants.rateKey).asLiveData()
                    .observe(this@PaymentActivity) { result ->
                        currencyRate = result?.toDouble() ?: 1.0
                        apiRequests()
                    }
            }
    }

    private fun apiRequests() {

        lifecycleScope.launchWhenStarted {

            launch {
                paymentViewModel.getStringDS(Constants.customerIdKey).asLiveData()
                    .observe(this@PaymentActivity) { customerID ->
                        // Update UI with the retrieved name
                        Log.i(TAG, "apiRequests: VIP: $customerID")
                        customerId = customerID!!
                        paymentViewModel.getAllDraftOrdersFromNetwork(customerId.toLong())
                        paymentViewModel.getCustomerByID(customerId.toLong())
                        paymentViewModel.getAllPriceRulesFromNetwork()
                    }
            }



            launch {
                paymentViewModel.draftOrdersList.collectLatest { result ->
                    when (result) {
                        is ApiState.Success<List<DraftOrder>> -> {
                            if (result.data.isNotEmpty()) {
                                draftOrder = result.data.get(0)
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${draftOrder.id}")
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${draftOrder.email}")
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${
                                    draftOrder.lineItems?.get(0)?.quantity
                                }")

                                val subtotalValue = "${
                                    convertCurrencyFromEGPTo((draftOrder.subtotalPrice)!!.toDouble(),
                                            currencyRate)
                                } $currencyISO"

                                val taxValue = "${
                                    convertCurrencyFromEGPTo((draftOrder.totalTax)!!.toDouble(),
                                            currencyRate)
                                } $currencyISO"

                                val discountValue = "${
                                    convertCurrencyFromEGPTo(((draftOrder.appliedDiscount?.amount) ?: "0.0").toDouble(),
                                            currencyRate)
                                } $currencyISO"

                                val shippingValue = "${
                                    convertCurrencyFromEGPTo(((draftOrder.shippingLine?.price) ?: "0.0").toDouble(),
                                            currencyRate)
                                } $currencyISO"

                                val totalValue = "${
                                    convertCurrencyFromEGPTo(((draftOrder.totalPrice) ?: "0.0").toDouble(),
                                            currencyRate)
                                } $currencyISO"


                                if (draftOrder.shippingLine?.price == "0.00") {
                                    binding.discountValue.text = "Free Shipping"
                                    binding.appliedCode.text =
                                        draftOrder.appliedDiscount?.title ?: "FreeShipping"
                                } else {
                                    binding.discountValue.text = discountValue
                                    binding.appliedCode.text =
                                        draftOrder.appliedDiscount?.title ?: "None"
                                }


                                binding.subtotalValue.text = subtotalValue
                                binding.taxValue.text = taxValue
                                binding.shippingValue.text = shippingValue
                                binding.totalValue.text = totalValue

                                totalPrice = "${
                                    convertCurrencyFromEGPTo(((draftOrder.totalPrice) ?: "0.0").toDouble(),
                                            currencyRate)
                                }"

                            } else {
                                Log.i(TAG, "onCreate: Success...The list is empty}")
                            }
                            customProgress.hideProgress()
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                            customProgress.showDialog(this@PaymentActivity, false)
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(this@PaymentActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }



            launch {
                paymentViewModel.updatedDraftCartOrder.collect { result ->
                    when (result) {
                        is ApiState.Success<DraftOrder> -> {

                            draftOrder = result.data

                            val subtotalValue = "${
                                convertCurrencyFromEGPTo((draftOrder.subtotalPrice)!!.toDouble(),
                                        currencyRate)
                            } $currencyISO"

                            val taxValue = "${
                                convertCurrencyFromEGPTo((draftOrder.totalTax)!!.toDouble(),
                                        currencyRate)
                            } $currencyISO"

                            val discountValue = "${
                                convertCurrencyFromEGPTo(((draftOrder.appliedDiscount?.amount) ?: "0.0").toDouble(),
                                        currencyRate)
                            } $currencyISO"

                            val shippingValue = "${
                                convertCurrencyFromEGPTo(((draftOrder.shippingLine?.price) ?: "0.0").toDouble(),
                                        currencyRate)
                            } $currencyISO"

                            val totalValue = "${
                                convertCurrencyFromEGPTo(((draftOrder.totalPrice) ?: "0.0").toDouble(),
                                        currencyRate)
                            } $currencyISO"

                            if (draftOrder.shippingLine?.price == "0.00") {
                                binding.discountValue.text = "Free Shipping"
                                binding.appliedCode.text =
                                    draftOrder.appliedDiscount?.title ?: "FreeShipping"
                            } else {
                                binding.discountValue.text = discountValue
                                binding.appliedCode.text =
                                    draftOrder.appliedDiscount?.title ?: "None"
                            }

                            binding.subtotalValue.text = subtotalValue
                            binding.taxValue.text = taxValue
                            binding.shippingValue.text = shippingValue
                            binding.totalValue.text = totalValue


                            totalPrice = "${
                                convertCurrencyFromEGPTo(((draftOrder.totalPrice) ?: "0.0").toDouble(),
                                        currencyRate)
                            }"
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@PaymentActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            launch {
                paymentViewModel.customer.collect { result ->
                    when (result) {
                        is ApiState.Success<Customer> -> {
                            customer = result.data
                            binding.streetText.text = customer.defaultAddress?.address1
                            binding.cityText.text = customer.defaultAddress?.city
                            binding.countryText.text = customer.defaultAddress?.country
                            binding.phoneText.text = customer.defaultAddress?.phone
                            binding.nameText.text =
                                "${customer.defaultAddress?.firstName ?: ""}  ${customer.defaultAddress?.lastName ?: ""}"
                            addressesList = customer.addresses!!
                            if (addressesList.isNotEmpty()) {
                                addressAdapter.submitList(addressesList)
                                addressBottomSheetBinding.emptyMsg.visibility = View.GONE
                            } else {
                                addressBottomSheetBinding.emptyMsg.visibility = View.VISIBLE
                                Log.i(TAG, "onCreate: Success...The list is empty}")
                                Toast.makeText(this@PaymentActivity,
                                        "There is no Address, please add one",
                                        Toast.LENGTH_SHORT).show()
                            }

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@PaymentActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            launch {
                paymentViewModel.priceRulesList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<PriceRules>> -> {
                            priceRuleList = result.data
                            Log.i(TAG, "apiRequests: $priceRuleList")
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(this@PaymentActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }

        }
    }

    override fun onAddressClickListener(addressId: Long) {
        addressesList.filter {
            it.id == addressId
        }.map {
            binding.streetText.text = it.address1
            binding.cityText.text = it.city
            binding.countryText.text = it.country
            binding.phoneText.text = it.phone
            binding.nameText.text = "${it.firstName} + ${it.lastName}"

            val shippingAddress: Address = Address(address1 = it.address1,
                    city = it.city,
                    country = it.country,
                    phone = it.phone,
                    firstName = it.firstName,
                    lastName = it.lastName)
            draftOrder = draftOrder.copy(shippingAddress = shippingAddress)
            Log.i(TAG, "initViews: DRAFT ORDER $draftOrder")
            paymentViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                    SingleDraftOrderResponse(draftOrder))
            Toast.makeText(this@PaymentActivity, "Address Updated", Toast.LENGTH_SHORT).show()
        }
        addressBottomSheetDialog.dismiss()
    }

    override fun onMakeDefaultClickListener(addressId: Long) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClickListener(addressId: Long) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            draftOrder =
                draftOrder.copy(appliedDiscount = AppliedDiscount(), shippingLine = ShippingLine())
            Log.i(TAG, "initViews: DRAFT ORDER $draftOrder")
            paymentViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                    SingleDraftOrderResponse(draftOrder))
            delay(500)
            super.onBackPressed()
        }
    }

    fun getCompleteDraftOrder() {

        lifecycleScope.launch {

            launch {
                paymentViewModel.completeDraftOrderPaid.collect { result ->
                    when (result) {
                        is ApiState.Success<DraftOrder> -> {

                            paymentViewModel.deleteDraftOrderByID(draftOrder.id!!)
                            Toast.makeText(this@PaymentActivity,
                                    "Paid Payment using PayPal Done",
                                    Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@PaymentActivity, MainHomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@PaymentActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            launch {
                paymentViewModel.completeDraftOrderPending.collect { result ->
                    when (result) {
                        is ApiState.Success<DraftOrder> -> {

                            paymentViewModel.deleteDraftOrderByID(draftOrder.id!!)
                            Toast.makeText(this@PaymentActivity,
                                    "Pending Payment Done",
                                    Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@PaymentActivity, MainHomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@PaymentActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }

    private fun initPayPalPaymentButton() {
        binding.paymentButtonContainer.setup(createOrder = CreateOrder { createOrderActions ->
            Log.i(TAG, "initPayPalPaymentButton: $totalPrice")
            val order = OrderRequest(intent = OrderIntent.CAPTURE,
                    appContext = AppContext(userAction = UserAction.PAY_NOW),
                    purchaseUnitList = listOf(PurchaseUnit(amount = Amount(currencyCode = CurrencyCode.USD,
                            value = totalPrice))))
            createOrderActions.create(order)
        }, onApprove = OnApprove { approval ->
            approval.orderActions.capture { captureOrderResult ->
                Log.i(TAG, "CaptureOrderResult: $captureOrderResult")
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
                getCompleteDraftOrder()
                paymentViewModel.completeDraftOrderPaidByID(draftOrder.id!!)

            }
        }, onCancel = OnCancel {
            Log.d(TAG, "Buyer Cancelled This Purchase")
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show()
        }, onError = OnError { errorInfo ->
            Log.d(TAG, "Error: $errorInfo")
            Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show()
        })
    }

}