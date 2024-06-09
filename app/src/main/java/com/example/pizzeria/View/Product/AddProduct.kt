package com.example.pizzeria.View.Product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzeria.Model.ProductData
import com.example.pizzeria.SelectItemImageSection
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.grayFont
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class AddProduct : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzeriaTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = blueColor,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "Add Product",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {this@AddProduct.startActivity(Intent(this@AddProduct, ProductDetails::class.java)) }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AddProductUI(LocalContext.current)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductUI(context: Context) {
    var expanded by remember { mutableStateOf(false) }

    val productName = remember { mutableStateOf("") }
    val productType = remember { mutableStateOf("") }
    val productPrice = remember { mutableStateOf("") }
    val productDescription = remember { mutableStateOf("") }
    val productImage = remember { mutableStateOf("") }
    val categories = remember { mutableStateOf(listOf<String>()) }

    // Fetch categories from Firebase
    LaunchedEffect(Unit) {
        loadCategories { categoryList ->
            categories.value = categoryList
        }
    }

    TextButton(onClick = {
        context.startActivity(Intent(context, ProductDetails::class.java))
    }) {
        Text(
            text = "All Product",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = blueColor,
        )
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = null,
            tint = blueColor
        )
    }

    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SelectItemImageSection { imageUrl ->
            productImage.value = imageUrl
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productName.value,
            onValueChange = { productName.value = it },
            modifier = Modifier
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
            label = { Text(text = "Product Name", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(10.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = productType.value,
                    onValueChange = { productType.value = it },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(59.dp)
                        .menuAnchor(),
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
                .height(59.dp),
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
            value = productDescription.value,
            onValueChange = { productDescription.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
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

        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                when {
                    TextUtils.isEmpty(productName.value) -> {
                        Toast.makeText(context, "Please enter the product name", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(productType.value) -> {
                        Toast.makeText(context, "Please enter the category", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(productPrice.value) -> {
                        Toast.makeText(context, "Please enter the price", Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(productDescription.value) -> {
                        Toast.makeText(context, "Please enter the description", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val price = productPrice.value.toDoubleOrNull()
                        if (price != null) {
                            addProductData(
                                productName.value,
                                productType.value,
                                price,
                                productDescription.value,
                                productImage.value,
                                context
                            )
                            context.startActivity(Intent(context, ProductDetails::class.java))
                        } else {
                            Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = blueColor
            )
        ) {
            Text(text = "Add", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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

fun addProductData(
    productName: String,
    productType: String,
    productPrice: Double,
    productDescription: String,
    productImage: String,
    context: Context
) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbProducts: CollectionReference = db.collection("Product")
    val productId = UUID.randomUUID().toString()
    val product = ProductData(productId, productName, productType, productPrice, productDescription, productImage)

    dbProducts.document(productId).set(product)
        .addOnSuccessListener {
        Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener { e ->
        Toast.makeText(context, "Error adding product \n$e", Toast.LENGTH_SHORT).show()
    }
}

@Preview
@Composable
fun PreviewAddProductUI() {
    AddProductUI(LocalContext.current)
}