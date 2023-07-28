package com.mohamednader.shoponthego.fake

import com.example.example.AddressesCustomers
import com.example.example.CustomerDraftsOrders
import com.example.example.Customers
import com.example.example.DraftOrders
import com.mohamednader.shoponthego.Database.LocalSource
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Products.Image
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.Rule
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Model.order.BillingAddress
import com.mohamednader.shoponthego.Model.order.Customer
import com.mohamednader.shoponthego.Model.order.LineItem
import com.mohamednader.shoponthego.Model.order.OrderX
import com.mohamednader.shoponthego.Network.RemoteSource
import com.mohamednader.shoponthego.SharedPrefs.SharedPrefsSource
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RepoTest {

    private lateinit var remoteSource: RemoteSource
    private lateinit var localSource: LocalSource
    private lateinit var sharedPrefsSource: SharedPrefsSource
    private lateinit var repository: Repository


    @Before
    fun setup() {
        remoteSource = mock()
        localSource = mock()
        sharedPrefsSource = mock()
        repository = Repository(remoteSource, localSource, sharedPrefsSource)
    }

    @Test
    fun testGetAllBrands() = runBlocking {

        val mockBrands = listOf(SmartCollection(
            admin_graphql_api_id = "gid://shopify/SmartCollection/1",
            body_html = "Test body HTML 1",
            disjunctive = true,
            handle = "test-collection-1",
            id = 1L,
            image = com.mohamednader.shoponthego.Model.Pojo.Products.brand.Image(
                alt = "Test Image 1",
                created_at = "2023-07-28T12:34:56",
                height = 200,
                src = "https://example.com/image1.jpg",
                width = 300
            ),
            published_at = "2023-07-28T12:00:00",
            published_scope = "web",
            rules = listOf(
                Rule(column = "tag", relation = "equals", condition = "sale")
            ),
            sort_order = "best-selling",
            template_suffix = Any(),
            title = "Test Collection 1",
            updated_at = "2023-07-28T12:34:56"
        ))

        // When
        whenever(remoteSource.getAllBrands()).thenReturn(flowOf(mockBrands))

        val result = repository.getAllBrands().single()
        assertEquals(mockBrands,result)

    }

    @Test
    fun testGetAllProduct() = runBlocking {
        val image = Image(
            id = 1,
            productId = 1,
            position = 1,
            createdAt = "2023-07-28T12:34:56Z",
            updatedAt = "2023-07-28T12:34:56Z",
            alt = null,
            width = 800,
            height = 600,
            src = "https://example.com/image.jpg",
            variantIds = emptyList(),
            adminGraphqlApiId = "graphql_id"
        )

        val mockProduct = listOf( Product(
            1 , "" ,"" , "" , "" , "" , "" , ""
            ,"","","","","","",variants = listOf(),
            options = listOf(), images = listOf(), image = image ))

        //when
        whenever(remoteSource.getAllProducts()).thenReturn(flowOf(mockProduct))

        val result = repository.getAllProducts().single()
        assertEquals(mockProduct,result)

    }

    @Test
    fun getAllPriceRules() = runBlocking {

        val mockPriceRules = listOf(PriceRules(
            1,
            "",
            "",
            "",""
        ))
        whenever(remoteSource.getAllPriceRules()).thenReturn(flowOf(mockPriceRules))
        val result = repository.getAllPriceRules().single()
        assertEquals(mockPriceRules,result)
    }

    @Test
    fun getAllCurrencies() = runBlocking {

        val mockCurrencies = listOf(CurrencyInfo(
            "",
            ""
        ))
        whenever(remoteSource.getAllCurrencies()).thenReturn(flowOf(mockCurrencies))
        val result = repository.getAllCurrencies().single()
        assertEquals(mockCurrencies,result)
    }

    @Test
    fun getAllOrders() = runBlocking {
        val billingAdrees = BillingAddress("","","",
            "","",""
        ,"","")
        val customer = Customer("","","","",
            1,"",2,"",
        "",true)
        val lineItems = listOf( LineItem(1,"","",2,
            "","",
            2,"",""))
        val mockOrders = listOf(OrderX(billingAdrees,"","", "",
            "","","",customer,"","",1,
        lineItems ,"",1, 1,"","",
            "","", 1))

        //when
        whenever(remoteSource.getAllOrders()).thenReturn(flowOf( mockOrders))
        val result = repository.getAllOrders().single()
        assertEquals(mockOrders,result)
    }

    @Test
    fun getAllCustomer() = runBlocking {

        val addressList = ArrayList<AddressesCustomers>()
        addressList.add(
            AddressesCustomers(1,
                1, "", "", "", "",
                "", "", "", "")
        )

        val customer = listOf( Customers(1,"","",
            "","","",1,
            "","","",true,
            "","","",addressList,
        ))

        //when
        whenever(remoteSource.getAllCustomer()).thenReturn(flowOf(customer))
        val result = repository.getAllCustomer().single()
        assertEquals(customer,result)
    }


    @Test
    fun getAllDraftsOrders() = runBlocking {

        val customerDraftsOrders =  CustomerDraftsOrders(
            1,"",true,"","",
            "","",1,"",
            "",1,true,"",""
        )

        val draftOrder = listOf(DraftOrders(
            1,"",true,"","","",
        1,"","",customerDraftsOrders))

        //when
        whenever(remoteSource.getAllDraftsOrders()).thenReturn(flowOf(draftOrder))
        val result = repository.getAllDraftsOrders().single()
        assertEquals(draftOrder,result)
    }

//
//    @Test
//    fun addDraftOrder() = runBlocking {
//
//        val draftOrder = DraftOrder(
//            1L, "", "", true, "", "", "", "", true, "", "", "", listOf(),
//            ShippingAddress(), com.mohamednader.shoponthego.Model.Pojo.BillingAddress(), "",
//            AppliedDiscount(), "", ShippingLine(), listOf(), "", listOf(), "", "", "",
//            "", "", com.mohamednader.shoponthego.Model.Pojo.customer.Customer()
//        )
//
//        whenever(remoteSource.addDraftOrder(SingleDraftOrderResponse(draftOrder)))
//    }

//@Test
//fun getAllCustomerById() = runBlocking {
//
//    val addressList = ArrayList<AddressesCustomers>()
//    addressList.add(
//        AddressesCustomers(1,
//            1, "", "", "", "",
//            "", "", "", "")
//    )
//
//    val customer =  Customer("","","","",1,"",1,""
//        ,"",true)
//
//    //when
//    whenever(remoteSource.getCustomerByID(1)).thenReturn(flowOf(customer))
//    val result = repository.getCustomerByID(1).single()
//    assertEquals(customer,result)
//}

}
