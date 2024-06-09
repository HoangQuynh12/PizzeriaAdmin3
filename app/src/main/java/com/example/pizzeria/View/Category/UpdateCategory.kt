package com.example.pizzeria.View.Category

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
import com.example.pizzeria.Model.CategoryData
import com.example.pizzeria.SelectItemImageSection
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.grayFont
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCategory : ComponentActivity() {
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
                                    text = "Update Category",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    this@UpdateCategory.startActivity(
                                        Intent(
                                            this@UpdateCategory,
                                            CategoryDetails::class.java
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
                        updateCategoryUI(
                            LocalContext.current,
                            intent.getStringExtra("categoryName"),
                            intent.getStringExtra("categoryImage"),
                            intent.getStringExtra("categoryID")
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateCategoryUI(
    context: Context,
    name: String?,
    imgUrl: String?,
    categoryID: String?
) {

    var expanded by remember { mutableStateOf(false) }

    val categoryName = remember { mutableStateOf(name) }
    val categoryImage = remember { mutableStateOf(imgUrl) }

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
            value = categoryName.value.orEmpty(),
            onValueChange = { categoryName.value = it },
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
            label = { Text(text = "Category Name", color = grayFont) }
        )

        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                if (categoryName.value.isNullOrEmpty()) {
                    Toast.makeText(context, "Please enter category name", Toast.LENGTH_SHORT).show()
                }  else {

                    val imageUrl = newImageUrl.value ?: categoryImage.value
                    updateDataToFirebase(
                        categoryID,
                        categoryName.value,
                        imageUrl,
                        context
                    )
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
                context.startActivity(Intent(context, CategoryDetails::class.java))
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


private fun updateDataToFirebase(
    categoryID: String?,
    name: String?,
    imgUrl: String?,
    context: Context
) {
    val updatedCategory = CategoryData(categoryID, name, imgUrl)

    val db = FirebaseFirestore.getInstance()
    db.collection("Category").document(categoryID.toString()).set(updatedCategory)
        .addOnSuccessListener {
            Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, CategoryDetails::class.java))
        }.addOnFailureListener {
            Toast.makeText(context, "Category update error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
}
