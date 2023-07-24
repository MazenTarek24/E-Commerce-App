package com.mohamednader.shoponthego.Categories.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.tabs.TabLayout
import com.mohamednader.shoponthego.BranProduct.view.BrandProductAdapter
import com.mohamednader.shoponthego.Categories.ViewModel.CategoriesViewModel
import com.mohamednader.shoponthego.Categories.ViewModel.CategoryViewModelFactory
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment() , TabLayout.OnTabSelectedListener  {

    lateinit var binding: FragmentCategoriesBinding

    lateinit var categoryViewModel: CategoriesViewModel
    lateinit var factory: CategoryViewModelFactory

    lateinit var brandAdapter: BrandProductAdapter
    lateinit var brandLayoutManager: LayoutManager


    val TAG = "CategoryResponseSuccessfully"

     var categoryId: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
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
        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Sale"))

        binding.catTabLayout.addOnTabSelectedListener(this)

        binding.accessoriesTxt.setOnClickListener {
            displayProductType("ACCESSORIES")
        }

        binding.shoesTxt.setOnClickListener {
            displayProductType("SHOES")
        }

        binding.tshirtTxt.setOnClickListener {
            displayProductType("T-SHIRTS")
        }

        displayProductType("ACCESSORIES")

    }

    private fun displayProductType(productType: String) {
        brandAdapter.deleteProductBrand()
        getAllCategory(categoryId,productType)
        getAllProduct(productType)
    }


    private fun initViews() {

        factory = CategoryViewModelFactory(Repository.getInstance(
            ApiClient.getInstance(),
            ConcreteLocalSource(requireContext()),
            ConcreteSharedPrefsSource(requireContext())
        ))
        categoryViewModel = ViewModelProvider(this, factory)
            .get(CategoriesViewModel::class.java)

    }


    private fun getAllProduct(productType: String? = null)
    {
        lifecycleScope.launch(Dispatchers.Main) {
            categoryViewModel.productList.collect{result->
                when(result)
                {
                    is ApiState.Success -> {
                        if (result.data.isNotEmpty()) {
                            Log.i(TAG,
                                "onCreateProduct: SuccessFetchProduct...{${result.data[0].title}}")

                            // If a productType is specified, filter the data accordingly
                            val filteredProduct = if (!productType.isNullOrBlank()) {
                                result.data.filter { it.productType == productType }
                            } else {
                                result.data
                            }

                            brandAdapter.submitList(filteredProduct)

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
        categoryViewModel.getAllProductInCategory(productType!!)

    }

    private fun getAllCategory(collectionId : Long , product_type : String)
    {
        lifecycleScope.launch(Dispatchers.Main) {
            categoryViewModel.productCategory.collect{result->
                when(result)
                {
                    is ApiState.Success -> {
                        if (result.data.isNotEmpty()) {
                            Log.i(TAG,
                                "onCreateCategory: SuccessFetchCategory...{${result.data[0].title}}")
                            val filterdProduct = if (!product_type.isNullOrBlank())
                            {
                                result.data.filter { it.productType == product_type }
                            }else{
                                result.data
                            }
                            brandAdapter.submitList(filterdProduct)

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
       categoryViewModel.getAllProductCategory(collectionId,product_type)
    }


    private fun setSaleCategory()
    {
        categoryId = 456248230205
        brandAdapter.deleteProductBrand()

        getAllCategory(categoryId,"")
    }
    private fun setCategoryWomen()
    {
         categoryId = 456248164669
         brandAdapter.deleteProductBrand()

         getAllCategory(categoryId,"",)

    }
    private fun setCategoryMen()
    {
       categoryId = 456248131901
       brandAdapter.deleteProductBrand()

       getAllCategory(categoryId,"")

    }

    private fun setCategoryKids()
    {
        categoryId = 456248197437
        brandAdapter.deleteProductBrand()

        getAllCategory(categoryId,"")

    }

   private fun setAllProduct()
    {
        brandAdapter.deleteProductBrand()
        getAllProduct("")
    }


    private fun initRvCategory()
    {
        brandLayoutManager = GridLayoutManager(context,2)
        brandAdapter = BrandProductAdapter(){

        }
        binding.rvCategory.apply {
            adapter = brandAdapter
            layoutManager = brandLayoutManager
        }


    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab?.position)
        {
            0 -> {
                setAllProduct()
            }
            1 -> {
                setCategoryWomen()
            }
            2 -> {
               setCategoryMen()

            }
            3->{
                setCategoryKids()
            }

            4->{
                setSaleCategory()
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