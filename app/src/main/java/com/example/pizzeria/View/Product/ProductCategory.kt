package com.example.pizzeria.View.Product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pizzeria.Model.ProductData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ProductCategory : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val categoryName = intent.getStringExtra("categoryName") ?: ""
            var productList by remember { mutableStateOf<List<ProductData>>(emptyList()) }

            ProductCategoryUI(LocalContext.current, categoryName, productList)
            loadProductsByType(categoryName) { products ->
                productList = products
            }
        }
    }

    private fun loadProductsByType(type: String, callback: (List<ProductData>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Product")
            .whereEqualTo("productType", type)
            .get()
            .addOnSuccessListener { queryDocumentSnapshot ->
                val productList = queryDocumentSnapshot.documents.mapNotNull { document ->
                    document.toObject(ProductData::class.java)?.apply { productID = document.id }
                }
                callback(productList)
            }
            .addOnFailureListener {
                // Handle the error appropriately
            }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCategoryUI(context: Context, categoryName: String, productList: List<ProductData>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 70.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn {
            itemsIndexed(productList) { index, item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Blue,
                    modifier = Modifier
                        .height(100.dp)
                        .padding(8.dp),
//                    elevation = 10.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item?.productName ?: "",
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = {
                                // Handle edit action
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                // Handle delete action
                            }
                        ) {
                            Icon(

                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}
