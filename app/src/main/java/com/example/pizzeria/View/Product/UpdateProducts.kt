package com.example.pizzeria.View.Product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.example.pizzeria.Model.ProductData
import com.example.pizzeria.SelectItemImageSection
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.grayFont
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProducts : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzeriaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = blueColor,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "Update Product",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    this@UpdateProducts.startActivity(
                                        Intent(
                                            this@UpdateProducts,
                                            ProductDetails::class.java
                                        )
                                    )
                                }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 100.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        updateProductUI(
                            LocalContext.current,
                            intent.getStringExtra("productName"),
                            intent.getStringExtra("productType"),
                            intent.getDoubleExtra("productPrice", 0.0).toString(),
                            intent.getStringExtra("productDescription"),
                            intent.getStringExtra("productImage"),
                            intent.getStringExtra("productID")
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateProductUI(
    context: Context,
    name: String?,
    type: String?,
    price: String,
    description: String?,
    imgUrl: String?,
    productID: String?
) {
    val categories = remember { mutableStateOf(listOf<String>()) }

    // Fetch categories from Firebase
    LaunchedEffect(Unit) {
        loadCategories { categoryList ->
            categories.value = categoryList
        }
    }
    var expanded by remember { mutableStateOf(false) }

    val productName = remember { mutableStateOf(name) }
    val productType = remember { mutableStateOf(type) }
    val productPrice = remember { mutableStateOf(price?.toString() ?: "") }

    val productDescription = remember { mutableStateOf(description) }
    val productImage = remember { mutableStateOf(imgUrl) }

    val newImageUrl = remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SelectItemImageSection { imageUrl ->
            newImageUrl.value = imageUrl
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productName.value.orEmpty(),
            onValueChange = { productName.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blue
            ),
            label = { Text(text = "Product Name", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(7.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = productType.value.orEmpty(),
                    onValueChange = { productType.value = it },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(59.dp),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = blueColor,
                        unfocusedBorderColor = blue
                    ),
                    label = { Text(text = "Category", color = grayFont) }
                )

                ExposedDropdownMenu(
                    modifier = Modifier.background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.value.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                productType.value = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productPrice.value,
            onValueChange = { productPrice.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blue
            ),
            label = { Text(text = "Price", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productDescription.value.orEmpty(),
            onValueChange = { productDescription.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blue
            ),
            label = { Text(text = "Description", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                if (productName.value.isNullOrEmpty()) {
                    Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT).show()
                } else if (productType.value.isNullOrEmpty()) {
                    Toast.makeText(context, "Please enter category", Toast.LENGTH_SHORT).show()
                } else if (productPrice.value.isNullOrEmpty()) {
                    Toast.makeText(context, "Please enter price", Toast.LENGTH_SHORT).show()
                } else if (productDescription.value.isNullOrEmpty()) {
                    Toast.makeText(context, "Please enter description", Toast.LENGTH_SHORT).show()
                } else {
                    val price = productPrice.value?.toDoubleOrNull()
                    if (price != null) {
                        val imageUrl = newImageUrl.value ?: productImage.value
                        updateDataToFirebase(
                            productID,
                            productName.value,
                            productType.value,
                            price,
                            productDescription.value,
                            imageUrl,
                            context
                        )
                    } else {
                        Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = blueColor
            )
        ) {
            Text(text = "Update", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.size(10.dp))
        TextButton(
            onClick = {
                context.startActivity(Intent(context, ProductDetails::class.java))
            },
        ) {
            Text(
                text = "Exit",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = blueColor,
            )
        }
    }
}


private fun loadCategories(callback: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Category").get()
        .addOnSuccessListener { result ->
            val categoryList = result.map { document -> document.getString("categoryName") ?: "" }
            callback(categoryList)
        }
        .addOnFailureListener { exception ->
            // Handle the error appropriately
        }
}

private fun updateDataToFirebase(
    productID: String?,
    name: String?,
    type: String?,
    price: Double,
    description: String?,
    imgUrl: String?,
    context: Context
) {
    val updatedProduct = ProductData(productID, name, type, price, description, imgUrl)

    val db = FirebaseFirestore.getInstance()
    db.collection("Product").document(productID.toString()).set(updatedProduct)
        .addOnSuccessListener {
            Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, ProductDetails::class.java))
        }.addOnFailureListener {
            Toast.makeText(context, "Product update error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
}
