package com.mohamednader.shoponthego.Settings.View

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.CurrencyAdapter
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.OnCurrencyClickListener
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Settings.ViewModel.SettingsViewModel
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivitySettingsBinding
import com.mohamednader.shoponthego.databinding.BottomSheetDialogCurrenciesBinding
import kotlinx.coroutines.launch


class SettingsActivity : AppCompatActivity(),OnCurrencyClickListener{

    val TAG = "SettingsActivity_INFO_TAG"

    private lateinit var binding: ActivitySettingsBinding

    //View Model Members
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var factory: GenericViewModelFactory

    //Currencies Bottom Sheet
    lateinit var currencyBottomSheetBinding: BottomSheetDialogCurrenciesBinding
    lateinit var currencyBottomSheetDialog: BottomSheetDialog
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var currencyLinearLayoutManager: LinearLayoutManager


    //Needed Variables
    lateinit var currenciesList: List<CurrencyInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        //View Model
        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(this@SettingsActivity),
                ConcreteSharedPrefsSource(this@SettingsActivity)
            )
        )
        settingsViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        //Currency Bottom Sheet
        currencyAdapter = CurrencyAdapter(this@SettingsActivity, this)
        currencyLinearLayoutManager =
            LinearLayoutManager(this@SettingsActivity, RecyclerView.VERTICAL, false)
       currencyBottomSheetBinding =  BottomSheetDialogCurrenciesBinding.inflate(layoutInflater)
        currencyBottomSheetDialog = BottomSheetDialog(this@SettingsActivity)
        currencyBottomSheetDialog.setContentView(currencyBottomSheetBinding.root)
        currencyBottomSheetBinding.currenciesRecyclerView.apply {
            adapter = currencyAdapter
            layoutManager = currencyLinearLayoutManager
        }



        binding




        binding.currency.setOnClickListener{
            currencyAdapter.submitList(currenciesList)
            currencyBottomSheetDialog.show()
        }

        getAllCurrencies()
    }


    private fun getAllCurrencies() {
        lifecycleScope.launchWhenStarted {

            launch {

                settingsViewModel.currencyRes.collect { result ->
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
                            Toast.makeText(
                                this@SettingsActivity, "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        settingsViewModel.getAllCurrenciesFromNetwork()
    }


    override fun onCurrencyClickListener(currencyISO: String) {
        Log.i(TAG, "onCurrencyClickListener: you Clicked $currencyISO")
        Toast.makeText(this@SettingsActivity, "you Clicked $currencyISO", Toast.LENGTH_SHORT).show()
        currencyBottomSheetDialog.dismiss();
    }
}