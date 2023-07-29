package com.mohamednader.shoponthego.Auth.SignUp.ViewModel

import com.mohamednader.shoponthego.MainDispatcherRule
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItem
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.Model.Pojo.Products.Image
import com.mohamednader.shoponthego.Model.Pojo.Products.Option
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.Variant
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.Rule
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.model.repo.FakeRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignUpViewModelTest() {
    var products = mutableListOf<Product>()
    var product = Product(10,
            "ths",
            "body",
            "vendor",
            "producttype",
            "credated",
            "handle",
            "updated",
            "publishe",
            "",
            "status",
            "published scope",
            "tags",
            "",
            listOf(Variant(15,
                    15,
                    "",
                    "",
                    "",
                    1,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "" + "",
                    "",
                    "",
                    "",
                    true,
                    "",
                    15,
                    "",
                    15.0,
                    "",
                    15,
                    14,
                    2,
                    true,
                    "")),
            listOf(Option(1, 1, "", 1, listOf(String()))),
            listOf(Image(1, 1, 1, "", "", "", 15, 2, "", listOf(), "")),
            Image(1, 1, 1, "", "", "", 1, 3, "", listOf(), ""))
    var smartCollection = SmartCollection("",
            ",",
            true,
            "",
            1,
            com.mohamednader.shoponthego.Model.Pojo.Products.brand.Image("" + "", "", 1, "", 4),
            "",
            "",
            listOf(Rule("", "", "")),
            "",
            ",",
            "",
            "")
    var smartCollections = mutableListOf<SmartCollection>()

    var discontorder = DiscountCodes(1, 1, "", 1, "", "")
    var discountCodes = mutableListOf<DiscountCodes>()
    val priceRule = PriceRules(id = 1,
            valueType = "percentage",
            value = "10",
            targetType = "line_item",
            title = "10% discount on all line items")
    val lineItems =
        mutableListOf(LineItem(id = 1, title = "Product A", price = "10.00", quantity = 2),
                LineItem(id = 2, title = "Product B", price = "20.00", quantity = 1))

    val shippingAddress = Address(firstName = "John",
            lastName = "Doe",
            address1 = "123 Main St",
            city = "Anytown",
            province = "CA",
            country = "Canada",
            zip = "A1B2C3")

    val billingAddress = Address(firstName = "Jane",
            lastName = "Doe",
            address1 = "456 First St",
            city = "Othertown",
            province = "CA",
            country = "Canada",
            zip = "X1Y2Z3")

    val address1 = Address(id = 1L,
            address1 = "123 Main St",
            city = "Anytown",
            province = "CA",
            country = "Canada",
            zip = "A1B 2C3",
            phone = "+1-555-555-5555",
            name = "John Doe",
            company = null,
            provinceCode = "CA",
            countryCode = "CA",
            default = true)

    val address2 = Address(id = 2L,
            address1 = "456 First St",
            city = "Othertown",
            province = "CA",
            country = "Canada",
            zip = "X1Y2Z3",
            phone = null,
            name = null,
            provinceCode = null,
            countryCode = null,
            default = false)

    val customer = Customer(id = 123,
            email = "john.doe@example.com",
            firstName = "John",
            lastName = "Doe",
            ordersCount = 5,
            state = "active",
            totalSpent = "500.00",
            lastOrderId = 12345L,
            note = "Some notes about the customer",
            verifiedEmail = true,
            lastOrderName = "Order #12345",
            currency = "USD",
            phone = "+1-555-555-5555",
            addresses = mutableListOf(address1),
            defaultAddress = address1)

    val draftOrder = DraftOrder(id = 123,
            note = "Some notes about this order",
            email = "customer@example.com",
            taxesIncluded = true,
            currency = "USD",
            taxExempt = false,
            name = "Draft Order 1",
            status = "open",
            lineItems = lineItems,
            shippingAddress = shippingAddress,
            billingAddress = billingAddress,
            invoiceUrl = "https://example.com/invoice",
            appliedDiscount = null,
            orderId = null,
            totalPrice = "40.00",
            subtotalPrice = "40.00",
            totalTax = "0.00",
            customer = customer)
    var draftOrders = mutableListOf<DraftOrder>()
    val customer1 = com.mohamednader.shoponthego.Model.Pojo.Customers.Customer(id = 12345L,
            email = "johndoe@example.com",
            firstName = "John",
            lastName = "Doe",
            ordersCount = 5,
            state = "CA",
            totalSpent = "$1000.00",
            lastOrderId = 54321L,
            note = "This customer is a VIP",
            verifiedEmail = true,
            lastOrderName = "Order #54321",
            currency = "USD",
            phone = "555-123-4567",
            addresses = mutableListOf(Address(id = 123L,
                    customerId = 456L,
                    firstName = "John",
                    lastName = "Doe",
                    company = null,
                    address1 = "123 Main St",
                    address2 = null,
                    city = "Los Angeles",
                    province = "CA",
                    country = "USA",
                    zip = "90001",
                    phone = "555-123-4567",
                    name = "Home",
                    provinceCode = "CA",
                    countryCode = "US",
                    countryName = "United States",
                    default = true)),
            Address(id = 123L,
                    customerId = 456L,
                    firstName = "John",
                    lastName = "Doe",
                    company = null,
                    address1 = "123 Main St",
                    address2 = null,
                    city = "Los Angeles",
                    province = "CA",
                    country = "USA",
                    zip = "90001",
                    phone = "555-123-4567",
                    name = "Home",
                    provinceCode = "CA",
                    countryCode = "US",
                    countryName = "United States",
                    default = true))
    val order = Order(billing_address = billingAddress,
            created_at = "2023-07-29T08:15:30Z",
            currency = "USD",
            current_subtotal_price = "$100.00",
            current_total_discounts = "$10.00",
            current_total_price = "$90.00",
            current_total_tax = "$5.00",
            customer = customer,
            email = "johndoe@example.com",
            id = 12345L,
            name = "Order #12345",
            number = 12345,
            order_number = 67890)
    var orders = mutableListOf<Order>()
    var priceRules = mutableListOf<PriceRules>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:org.junit.Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var repo: FakeRepo
    lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        repo = FakeRepo(products,
                smartCollections,
                discountCodes,
                priceRules,
                draftOrder,
                product,
                draftOrders,
                customer,
                listOf(),
                orders,
                customer)
        viewModel = SignUpViewModel(repo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createCustomer with valid input`() = runBlockingTest {
        // given
        val customer = SingleCustomerResponse(Customer(id = 123L,
                email = "john.doe@example.com",
                firstName = "John",
                lastName = "Doe",
                ordersCount = 5,
                state = "active",
                totalSpent = "500.00",
                lastOrderId = 12345L,
                note = "Some notes about the customer",
                verifiedEmail = true,
                lastOrderName = "Order #12345",
                currency = "USD",
                phone = "+1-555-555-5555",
                addresses = mutableListOf(Address(id = 1L,
                        address1 = "123 Main St",
                        address2 = null,
                        city = "Anytown",
                        province = "CA",
                        country = "Canada",
                        zip = "A1B 2C3",
                        phone = "+1-555-555-5555",
                        name = "John Doe",
                        company = null,
                        provinceCode = "CA",
                        countryCode = "CA",
                        default = true)),
                defaultAddress = Address(id = 1L,
                        address1 = "123 Main St",
                        address2 = null,
                        city = "Anytown",
                        province = "CA",
                        country = "Canada",
                        zip = "A1B 2C3",
                        phone = "+1-555-555-5555",
                        name = "John Doe",
                        company = null,
                        provinceCode = "CA",
                        countryCode = "CA",
                        default = true)))

        // when
        viewModel.createCustomer(customer)
        // Mock the network call to return successfully
        // then
        Thread.sleep(1000)

        val result = viewModel.customer.value
        assertEquals(ApiState.Success(customer.customer), result)
    }


}
