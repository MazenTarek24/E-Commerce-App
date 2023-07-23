package com.mohamednader.shoponthego.Categories.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.gms.common.api.Api
import com.google.android.material.tabs.TabLayout
import com.mohamednader.shoponthego.BranProduct.view.BrandProductAdapter
import com.mohamednader.shoponthego.Categories.ViewModel.CategoriesViewModel
import com.mohamednader.shoponthego.Categories.ViewModel.CategoryViewModelFactory
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Database.LocalSource
import com.mohamednader.shoponthego.Home.View.BrandAdapter
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Network.RemoteSource
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.SharedPrefs.SharedPrefsSource
import com.mohamednader.shoponthego.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment() , TabLayout.OnTabSelectedListener {

    lateinit var binding : FragmentCategoriesBinding

    lateinit var categoryViewModel : CategoriesViewModel
    lateinit var factory : CategoryViewModelFactory

    lateinit var brandAdapter: BrandProductAdapter
    lateinit var brandLayoutManager: LayoutManager

    val TAG = "CategoryResponseSuccessfully"

    lateinit var categoryId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initRvCategory()


        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("All"))
        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Women"))
        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Men"))
        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Kids"))

        binding.catTabLayout.addOnTabSelectedListener(this)

    }

    private fun initViews()
    {

        factory = CategoryViewModelFactory(Repository.getInstance(
            ApiClient.getInstance(),
            ConcreteLocalSource(requireContext()),
            ConcreteSharedPrefsSource(requireContext())
        ))
        categoryViewModel = ViewModelProvider(this,factory)
            .get(CategoriesViewModel::class.java)

      getAllProduct()
    }

    private fun getAllProduct()
    {
        lifecycleScope.launch(Dispatchers.Main) {
            categoryViewModel.productList.collect{result->
                when(result)
                {
                    is ApiState.Success -> {
                        if (result.data.isNotEmpty()) {
                            Log.i(TAG,
                                "onCreateProduct: SuccessFetchProduct...{${result.data[0].title}}")
                            brandAdapter.submitList(result.data)
                        }else{
                            Log.i(TAG,
                                "onCreateProduct: ListProductIsEmpty")
                        }
                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: LoadingWhenFetchingProducts...")
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(),
                            "There Was An Error when fetching Products", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        categoryViewModel.getAllProductInCategory()

    }

    private fun getAllCategory(collectionId : String , product_type : String , vendor : String)
    {
        lifecycleScope.launch(Dispatchers.Main) {
            categoryViewModel.productCategory.collect{result->
                when(result)
                {
                    is ApiState.Success -> {
                        if (result.data.isNotEmpty()) {
                            Log.i(TAG,
                                "onCreateCategory: SuccessFetchBrands...{${result.data[0].title}}")
                            brandAdapter.submitList(result.data)
                        }else{
                            Log.i(TAG,
                                "onCreateCategory: ListCategoryIsEmpty")
                        }
                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: LoadingWhenFetchingCategoryProducts...")
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(),
                            "There Was An Error when fetching Category Products", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
//        categoryViewModel.getAllProductCategory(collectionId,product_type,vendor)
    }

    private fun setCategoryWomen()
    {
        categoryId = "274500059275"
        brandAdapter.deleteProductBrand()
         getAllCategory(categoryId,"","")

    }
    private fun setCategoryMen()
    {
        categoryId = "274500026507"
        brandAdapter.deleteProductBrand()
        getAllCategory(categoryId,"","")

    }

    private fun setCategoryKids()
    {
        categoryId = "274500092043"
        brandAdapter.deleteProductBrand()
        getAllCategory(categoryId,"","")

    }



    private fun initRvCategory()
    {
        brandLayoutManager = GridLayoutManager(context,2)
        brandAdapter = BrandProductAdapter()
        binding.rvCategory.apply {
            adapter = brandAdapter
            layoutManager = brandLayoutManager
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab?.position)
        {
            0 -> {
                categoryViewModel.getAllProductInCategory()
            }
            1 -> {
                setCategoryWomen()
            }
            2 -> {
               setCategoryMen()

            }
            3 ->{
                setCategoryKids()
            }

        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
       Log.i(TAG,"selected")
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        Log.i(TAG,"selected")

    }


}