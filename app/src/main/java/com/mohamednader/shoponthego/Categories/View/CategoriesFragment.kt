package com.mohamednader.shoponthego.Categories.View

import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.tabs.TabLayout
import com.mohamednader.shoponthego.BranProduct.view.BrandProductAdapter
import com.mohamednader.shoponthego.Cart.View.CartActivity
import com.mohamednader.shoponthego.Categories.ViewModel.CategoriesViewModel
import com.mohamednader.shoponthego.Categories.ViewModel.CategoryViewModelFactory
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.databinding.FragmentCategoriesBinding
import com.mohamednader.shoponthego.fav.FavActivty
import com.mohamednader.shoponthego.productinfo.ProductInfo
import com.mohamednader.shoponthego.search.SearchActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment() , TabLayout.OnTabSelectedListener , PriceFilterDialogFragment.PriceFilterListener  {

    lateinit var binding: FragmentCategoriesBinding

    lateinit var categoryViewModel: CategoriesViewModel
    lateinit var factory: CategoryViewModelFactory

    lateinit var categoryAdapter: CategoryAdapter
    lateinit var catLayoutManager: LayoutManager


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


        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Women"))
        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Men"))
        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Kids"))
        binding.catTabLayout.addTab(binding.catTabLayout.newTab().setText("Sale"))

        binding.catTabLayout.addOnTabSelectedListener(this)


        binding.accessoriesTxt.setOnClickListener {
            displayProductType("ACCESSORIES")
//            showPriceFilterDialog()
        }

        binding.shoesTxt.setOnClickListener {
            displayProductType("SHOES")
//            showPriceFilterDialog()
        }

        binding.tshirtTxt.setOnClickListener {
            displayProductType("T-SHIRTS")
//            showPriceFilterDialog()
        }

        setCategoryWomen()

        binding.filterPrice.setOnClickListener {
            showPriceFilterDialog()
        }

        Navigation()

    }

    private fun displayProductType(productType: String) {
        categoryAdapter.deleteProductBrand()
        getAllCategory(categoryId,productType)
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
                            binding.rvCategory.visibility = View.VISIBLE
                            binding.imgNoProduct.visibility = View.GONE

                            val filterdProduct = if (!product_type.isNullOrBlank())
                            {
                                result.data.filter { it.productType == product_type }
                            }else{
                                result.data
                            }
                            categoryAdapter.submitList(filterdProduct)

                        }else{
                            binding.rvCategory.visibility = View.GONE

                            binding.imgNoProduct.visibility = View.VISIBLE
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
        categoryAdapter.deleteProductBrand()

        getAllCategory(categoryId,"")
    }
    private fun setCategoryWomen()
    {
         categoryId = 456248164669
        categoryAdapter.deleteProductBrand()

         getAllCategory(categoryId,"",)

    }
    private fun setCategoryMen()
    {
       categoryId = 456248131901
        categoryAdapter.deleteProductBrand()

       getAllCategory(categoryId,"")

    }

    private fun setCategoryKids()
    {
        categoryId = 456248197437
        categoryAdapter.deleteProductBrand()

        getAllCategory(categoryId,"")

    }



    private fun initRvCategory()
    {
        categoryAdapter = CategoryAdapter(){
            val intent = Intent(context, ProductInfo::class.java)
            intent.putExtra("id",it )
            startActivity(intent)

        }

        catLayoutManager = GridLayoutManager(context,2)
        binding.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = catLayoutManager
        }


    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab?.position)
        {
            0 -> {
                setCategoryWomen()
            }
            1 -> {
               setCategoryMen()

            }
            2 ->{
                setCategoryKids()
            }

            3 ->{
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

    override fun onPriceFiltered(priceFrom: Double, priceTo: Double) {
        filterByPrice(priceFrom , priceTo)
    }

    private fun filterByPrice(priceFrom: Double, priceTo: Double) {
        categoryAdapter.deleteProductBrand()
        val filteredProducts = categoryAdapter.currentList.filter { product ->
            product.variants?.get(0)?.price!!.toDouble() in priceFrom..priceTo
        }
        categoryAdapter.submitList(filteredProducts)
    }

    private fun showPriceFilterDialog() {
        val filteredTshirts = categoryAdapter.currentList.filter { product ->
            product.productType == "T-SHIRTS"
        }
        val filteredAccessories = categoryAdapter.currentList.filter { product ->
            product.productType == "ACCESSORIES"
        }
        val filteredShoes = categoryAdapter.currentList.filter { product ->
            product.productType == "SHOES"
        }

        if (filteredTshirts.isEmpty() && filteredAccessories.isEmpty() && filteredShoes.isEmpty()) {

        } else {
            val priceFilterDialog = PriceFilterDialogFragment.newInstance()
            priceFilterDialog.setTargetFragment(this, 0)
            priceFilterDialog.show(parentFragmentManager, "PriceFilterDialog")
        }
    }


    private fun Navigation() {
        binding.fav.setOnClickListener {
            val intent = Intent(requireContext(), FavActivty::class.java)
            startActivity(intent)
        }


        binding.cart.setOnClickListener {
            val intent = Intent(requireContext(), CartActivity::class.java)
            startActivity(intent)
        }

        binding.search.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }


}