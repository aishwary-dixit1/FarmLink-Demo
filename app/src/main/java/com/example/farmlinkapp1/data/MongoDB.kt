package com.example.farmlinkapp1.data

import android.util.Log
import com.example.farmlinkapp1.model.Buyer
import com.example.farmlinkapp1.model.Category
import com.example.farmlinkapp1.model.Item
import com.example.farmlinkapp1.model.Review
import com.example.farmlinkapp1.model.SaleItem
import com.example.farmlinkapp1.model.Seller
import com.example.farmlinkapp1.model.User
import com.example.farmlinkapp1.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

object MongoDB : MongoDBRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        Log.d("fuck", "begin")
        configureRealm()
        //initializeDB()
    }

    override fun configureRealm() {
        Log.d("fuck", "entering")
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user = user,
                schema = setOf(
                    Category::class,
                    Item::class,
                    SaleItem::class,
                    User::class,
                    Review::class,
                    Buyer::class,
                    Seller::class,
                )
            )
                .initialSubscriptions { realm ->
                    add(
                        query = realm.query<Category>(),
                        name = "Categories",
                        updateExisting = true
                    )

                    add(
                        query = realm.query<Item>(),
                        name = "Items",
                        updateExisting = true
                    )

                    add(
                        query = realm.query<SaleItem>(),
                        name = "SaleItems",
                        updateExisting = true
                    )

                    add(
                        query = realm.query<Seller>(),
                        name = "Seller",
                        updateExisting = true
                    )

                    add(
                        query = realm.query<User>(query = "ownerId == $0", user.id),
                        name = "UserData",
                        updateExisting = true
                    )
                }
                //.log(LogLevel.ALL)
                .build()

            try {
                Log.d("fuck", "opening realm")
                realm = Realm.open(config)
                Log.d("fuck", realm.configuration.name)
            } catch (e: Exception) {
                Log.d("fuck", e.message!!)
            }

            Log.d("fuck", "done")
        }
    }

    override fun getAllCategories() : Flow<List<Category>> {
        Log.d("fuck", "get category")
        return realm.query<Category>().asFlow().map { it.list }
    }

    override fun getAllItemsByCategoryId(categoryId: ObjectId): Flow<List<Item>> {
        return realm.query<Item>("category._id == $0", categoryId).asFlow().map { it.list }
    }

    override fun getAllSaleItemsByItemId(itemId: ObjectId): Flow<List<SaleItem>> {
        return realm.query<SaleItem>("item._id == $0", itemId).asFlow().map { it.list }
    }

    private fun initializeDB() {
        CoroutineScope(Dispatchers.IO).launch {
            val subscription = realm.subscriptions.update {
                add(realm.query<Category>(), name = "categorySubscription", updateExisting = true)
            }
            try {
                subscription.waitForSynchronization()
            } catch(e: Exception) {
                Log.d("fuck", e.message ?: "fuck off")
            }
            realm.write {
                val category1 = Category().apply {
                    title = "Vegetables"
                    imageUrl = "https://i.postimg.cc/cCfgtDRV/vegetables.webp"
                }

                val category2 = Category().apply {
                    title = "Fruits"
                    imageUrl = "https://i.postimg.cc/xT0TPTnk/368872.jpg"
                }

                val category3 = Category().apply {
                    title = "Cereals"
                    imageUrl =
                        "https://i.postimg.cc/MGjdrwqR/stock-photo-cereal-grains-seeds-beans-379660966.jpg"
                }

                val category4 = Category().apply {
                    title = "Pulses"
                    imageUrl =
                        "https://i.postimg.cc/Qx6CsLnf/93b5a15f6ea7918d100b238e9995ddca.jpg"
                }

                //fruits
                val item1 = Item().apply {
                    title = "Apple"
                    imageUrl = "https://i.postimg.cc/W3sP7GfJ/farm-fresh-red-apple-156.jpg"
                    category = category2
                }

                val item2 = Item().apply {
                    title = "Mango"
                    imageUrl = "https://i.postimg.cc/T1Z2q81q/18847dd89b54bd5f4a0bfd77b7440b5d.jpg"
                    category = category2
                }

                val item3 = Item().apply {
                    title = "Pineapple"
                    imageUrl = "https://i.postimg.cc/ydp8x3zG/e190514f1b107b5b1ec278989492d197.jpg"
                    category = category2
                }

                val item4 = Item().apply {
                    title = "Orange"
                    imageUrl = "https://i.postimg.cc/HnJsyWYk/49ffa9b8fb5cddec251e2f5945fa208f.jpg"
                    category = category2
                }

                val seller1 = User().apply {
                    name = "Aishwary"
                    profilePicUrl = "https://i.postimg.cc/W3sP7GfJ/farm-fresh-red-apple-156.jpg"
                    address = "Mumbai, India"
                    phoneNumber = "9555909041"
                    seller = Seller().apply {
                        ratings = 4
                    }
                }

                val seller2 = User().apply {
                    name = "Sudha"
                    profilePicUrl = "https://i.postimg.cc/W3sP7GfJ/farm-fresh-red-apple-156.jpg"
                    address = "Delhi, India"
                    phoneNumber = "9794233033"
                    seller = Seller().apply {
                        ratings = 5
                    }
                }

                val seller3 = User().apply {
                    name = "Deependra"
                    profilePicUrl = "https://i.postimg.cc/W3sP7GfJ/farm-fresh-red-apple-156.jpg"
                    address = "Kolkata, India"
                    phoneNumber = "6392727418"
                    seller = Seller().apply {
                        ratings = 1
                    }
                }

                val saleItem1 = SaleItem().apply {
                    item = item1
                    seller = seller1.seller
                    quantityInKg = 10.0
                    pricePerKg = 100.0
                    distance = 10.0
                }

                val saleItem2 = SaleItem().apply {
                    item = item1
                    seller = seller2.seller
                    quantityInKg = 150.0
                    pricePerKg = 10.0
                    distance = 5.3
                }

                val saleItem3 = SaleItem().apply {
                    item = item1
                    seller = seller1.seller
                    quantityInKg = 40.0
                    pricePerKg = 100.0
                    distance = 1.9
                }

                val saleItem4 = SaleItem().apply {
                    item = item1
                    seller = seller3.seller
                    quantityInKg = 100.0
                    pricePerKg = 14.0
                    distance = 2.8
                }

                seller1.seller!!.itemsListed.addAll(listOf(saleItem1, saleItem3))
                seller1.seller!!.itemsListed.add(saleItem2)
                seller1.seller!!.itemsListed.add(saleItem4)
                category2.items.addAll(listOf(item1, item2, item3, item4))
                item1.saleItems.addAll(listOf(saleItem1, saleItem2, saleItem3, saleItem4))

                copyToRealm(category1, UpdatePolicy.ALL)
                copyToRealm(category2, UpdatePolicy.ALL)
                copyToRealm(category3, UpdatePolicy.ALL)
                copyToRealm(category4, UpdatePolicy.ALL)

                copyToRealm(item1, UpdatePolicy.ALL)
                copyToRealm(item2, UpdatePolicy.ALL)
                copyToRealm(item3, UpdatePolicy.ALL)
                copyToRealm(item4, UpdatePolicy.ALL)

                copyToRealm(seller1, UpdatePolicy.ALL)
                copyToRealm(seller2, UpdatePolicy.ALL)
                copyToRealm(seller3, UpdatePolicy.ALL)

                copyToRealm(saleItem1, UpdatePolicy.ALL)
                copyToRealm(saleItem2, UpdatePolicy.ALL)
                copyToRealm(saleItem3, UpdatePolicy.ALL)
                copyToRealm(saleItem4, UpdatePolicy.ALL)
            }
        }
    }

    override fun getItemById(itemId: ObjectId) : Item {
        return realm.query<Item>("_id == $0", itemId).first().find()!!
    }

    override fun getCategoryName(categoryId: ObjectId): String {
        return realm.query<Category>("_id == $0", categoryId).find().first().title
    }

    override fun getItemName(itemId: ObjectId): String {
        return realm.query<Item>("_id == $0", itemId).find().first().title
    }
}