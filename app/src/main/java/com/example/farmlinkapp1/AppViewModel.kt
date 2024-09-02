//package com.example.farmlinkapp1
//
//import androidx.lifecycle.ViewModel
//import com.example.farmlinkapp1.model.Category
//import com.example.farmlinkapp1.model.Item
//import dagger.hilt.android.lifecycle.HiltViewModel
//import io.realm.kotlin.Realm
//import io.realm.kotlin.ext.query
//import org.mongodb.kbson.ObjectId
//import javax.inject.Inject
//
//@HiltViewModel
//class AppViewModel @Inject constructor(
//    private val realm: Realm
//) : ViewModel() {
//
//    fun getCategoryName(categoryId: ObjectId): String {
//        return realm.query<Category>("_id == $0", categoryId).find().first().title
//    }
//
//    fun getItemName(itemId: ObjectId): String {
//        return realm.query<Item>("_id == $0", itemId).find().first().title
//    }
//}