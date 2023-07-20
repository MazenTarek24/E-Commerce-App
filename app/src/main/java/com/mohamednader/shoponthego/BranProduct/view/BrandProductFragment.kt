package com.mohamednader.shoponthego.BranProduct.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.BranProduct.viewmodel.BrandProductViewModel
import com.mohamednader.shoponthego.BranProduct.viewmodel.BrandProductViewModelFactory
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.databinding.FragmentBrandProductBinding
import kotlinx.coroutines.launch

class BrandProductFragment : Fragment() {

    var TAG = "BrandProductFragment"
    lateinit var brandProductAdapter: BrandProductAdapter
    lateinit var binding: FragmentBrandProductBinding
    lateinit var brandLayoutManager: RecyclerView.LayoutManager

    //View Model Members
    private lateinit var brandProductViewModel : BrandProductViewModel
    private lateinit var factory: BrandProductViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBrandProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initRvProductBrands()


    }

    private fun initViews() {
        val brandId = arguments?.getString("brandId")

            factory = BrandProductViewModelFactory(
                Repository.getInstance(
                    ApiClient.getInstance(),
                    ConcreteLocalSource(requireContext()),
                    ConcreteSharedPrefsSource(requireContext()) )
            )
            brandProductViewModel = ViewModelProvider(this, factory)
                .get(BrandProductViewModel::class.java)

        getBrandsProduct(brandId!!)

    }



    private fun getBrandsProduct(id : String)
    {
       viewLifecycleOwner.lifecycleScope.launch {
            brandProductViewModel.productList.collect{ result->
                when(result){
                    is ApiState.Success -> {
                        Log.i(TAG, "onCreateBrands: SuccessFetchBrands...{${result.data.get(0).id}}")
                        brandProductAdapter.submitList(result.data)
                    }
                    is ApiState.Loading ->{
                        Log.i(TAG, "onCreate: LoadingWhenFetchingBrandsProducts...")
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(),
                            "There Was An Error when fetching brands Products", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        brandProductViewModel.getAllBrandProduct(id)
    }

    private fun initRvProductBrands() {
        brandProductAdapter = BrandProductAdapter()
        brandLayoutManager = LinearLayoutManager(context , RecyclerView.VERTICAL , false)

        binding.rvProduct.apply {
            adapter = brandProductAdapter
            layoutManager = brandLayoutManager
        }
    }
}