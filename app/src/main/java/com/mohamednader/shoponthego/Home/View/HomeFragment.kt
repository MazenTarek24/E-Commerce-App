package com.mohamednader.shoponthego.Home.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.ViewModel.HomeViewModel
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private val TAG = "HomeFragment_INFO_TAG"
    private lateinit var binding: FragmentHomeBinding

    //View Model Members
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var factory: GenericViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

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



        requestProducts()


    }

    private fun requestProducts() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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
                        Toast.makeText(requireContext(), "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        homeViewModel.getAllProductsFromNetwork()
    }

}