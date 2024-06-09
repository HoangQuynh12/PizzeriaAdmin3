package com.example.pizzeria.View.Category

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzeria.Model.CategoryData
import com.example.pizzeria.SelectItemImageSection
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.grayFont
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class AddType : ComponentActivity() {
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
                                    text = "Add Category",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {this@AddType.startActivity(Intent(this@AddType, CategoryDetails::class.java))  }) {
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
                            AddCategoryUI(LocalContext.current)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryUI(context: Context) {
    var expanded by remember { mutableStateOf(false) }

    val categoryName = remember { mutableStateOf("") }
    val categoryImage = remember { mutableStateOf("") }


    TextButton(onClick = {
        context.startActivity(Intent(context, CategoryDetails::class.java))
    }) {
        Text(
            text = "All Category",
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
            categoryImage.value = imageUrl
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = categoryName.value,
            onValueChange = { categoryName.value = it },
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
            label = { Text(text = "Category Name", color = grayFont) }
        )

        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                when {
                    TextUtils.isEmpty(categoryName.value) -> {
                        Toast.makeText(context, "Please enter the product name", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        addCategoryData(
                            categoryName.value,
                            categoryImage.value,
                            context
                        )
                        context.startActivity(Intent(context, CategoryDetails::class.java))
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

fun addCategoryData(
    categoryName: String,
    categoryImage: String,
    context: Context
) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbCategory: CollectionReference = db.collection("Category")
    val categoryId = UUID.randomUUID().toString()
    val category = CategoryData(categoryId, categoryName, categoryImage)

    dbCategory.add(category).addOnSuccessListener {
        Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener { e ->
        Toast.makeText(context, "Error adding product \n$e", Toast.LENGTH_SHORT).show()
    }
}


@Preview
@Composable
fun PreviewAddCategoryUI() {
    AddCategoryUI(LocalContext.current)
}