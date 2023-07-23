package com.mohamednader.shoponthego.Home.View

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.gms.common.api.Api

import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.CouponAdapter
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.OnGetNowClickListener
import com.mohamednader.shoponthego.Home.ViewModel.HomeViewModel
import com.mohamednader.shoponthego.Model.Pojo.Coupon.Coupon
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Settings.View.SettingsActivity
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


class HomeFragment : Fragment(), OnGetNowClickListener {

    private val TAG = "HomeFragment_INFO_TAG"
    private lateinit var binding: FragmentHomeBinding

    //View Model Members
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var factory: GenericViewModelFactory

    //Needed Variables
    private var couponList: MutableList<Coupon> = mutableListOf()
    var coupon: Coupon = Coupon("", "", "")

    //RecyclerViews
    lateinit var couponAdapter: CouponAdapter

    lateinit var brandAdapter: BrandAdapter
    lateinit var brandLayoutManager: LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRvBrands()
        initViews()

    }

    private fun initRvBrands() {
        brandAdapter = BrandAdapter()
        brandLayoutManager = GridLayoutManager(context, 2)
        binding.rvBrand.apply {
            adapter = brandAdapter
            layoutManager = brandLayoutManager
        }
    }

    private fun initViews() {

        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteSharedPrefsSource(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        //ViewPager Components
        couponAdapter = CouponAdapter(requireContext(), this)


        binding.couponViewPager.apply {
            adapter = couponAdapter
        }



        binding.cart.setOnClickListener{
            val intent : Intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        apiRequests()
        requestBrands()

    }


    private fun apiRequests() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            launch {

                homeViewModel.productList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<Product>> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.get(0).id}}")
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(
                                requireContext(), "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }


            val job = launch {
                homeViewModel.discountCodesList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<DiscountCodes>> -> {
                            Log.i(TAG, "discountCodesList: Success...{${result.data.get(0).code}}")
                            Log.i(TAG, "discountCodesList: ${couponList.toString()}")
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(
                                requireContext(), "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }


            launch {
                homeViewModel.priceRulesList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<PriceRules>> -> {
                            for (i in 0 until result.data.size) {
                                val priceRule: PriceRules = PriceRules(
                                    result.data.get(i).id,
                                    result.data.get(i).valueType,
                                    result.data.get(i).value,
                                    result.data.get(i).targetType,
                                    result.data.get(i).title
                                )
                                coupon = Coupon("", "", "")
                                coupon.value =
                                    "${priceRule.value.toDouble().toInt().absoluteValue}% Off"
                                if (priceRule.targetType == "shipping_line") coupon.description =
                                    "On shipping today"
                                else coupon.description = "On everything today"
                                coupon.code = priceRule.title
                                couponList.add(coupon)
                                homeViewModel.getDiscountCodesByPriceRuleIDFromNetwork(priceRule.id)
                            }
                            Log.i(TAG, "apiRequests: $couponList")
                            couponAdapter.submitList(couponList)
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(
                                requireContext(), "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }

        }

        homeViewModel.getAllProductsFromNetwork()
        homeViewModel.getAllPriceRulesFromNetwork()
    }

    override fun onGetNowClickListener(codeToCopy: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", codeToCopy)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "You Copied $codeToCopy", Toast.LENGTH_SHORT).show()
    }

    private fun requestBrands() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.brandList.collect { result ->
                when (result) {
                    is ApiState.Success<List<SmartCollection>> -> {
                        Log.i(
                            TAG, "onCreateBrands: SuccessFetchBrands...{${result.data.get(0).id}}"
                        )
                        brandAdapter.submitList(result.data)
                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: LoadingWhenFetchingBrands...")
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "There Was An Error when fetching brands",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        homeViewModel.getAllBrands()
    }

}