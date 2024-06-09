package com.example.pizzeria.View.Category

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pizzeria.MainActivity
import com.example.pizzeria.Model.CategoryData
import com.example.pizzeria.View.Product.ProductCategory
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.red
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class CategoryDetails : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzeriaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
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
                                        text = "All Category",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        startActivity(Intent(this@CategoryDetails, MainActivity::class.java))
                                    }) {
                                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                    }
                                }
                            )
                        }
                    ) {
                        val categoryList = remember { mutableStateListOf<CategoryData>() }
                        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                        val dbCategory: CollectionReference = db.collection("Category")

                        dbCategory.get().addOnSuccessListener { queryDocumentSnapshot ->
                            if (!queryDocumentSnapshot.isEmpty) {
                                val list = queryDocumentSnapshot.documents
                                for (d in list) {
                                    val c: CategoryData? = d.toObject(CategoryData::class.java)
                                    c?.categoryID = d.id
                                    Log.e("TAG", "Course id is : " + c!!.categoryID)
                                    categoryList.add(c)
                                }
                            } else {
                                Toast.makeText(
                                    this@CategoryDetails,
                                    "There is no data in the Database",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@CategoryDetails,
                                "Error while retrieving data.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        CategoryDetailsUI(LocalContext.current, categoryList)
                    }
                }
            }
        }
    }
}

private fun deleteDataFromFirebase(categoryID: String?, context: Context) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Category").document(categoryID.toString()).delete().addOnSuccessListener {
        Toast.makeText(context, "Category removed", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, CategoryDetails::class.java))
    }.addOnFailureListener {
        Toast.makeText(context, "Error while deleting category", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailsUI(context: Context, categoryList: SnapshotStateList<CategoryData>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(onClick = { context.startActivity(Intent(context, AddType::class.java)) }) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
        }

        LazyColumn {
            itemsIndexed(categoryList) { index, item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = blue,
                    modifier = Modifier
                        .height(100.dp)
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shadowElevation = 20.dp
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.size(60.dp)
                        ) {
                            item.categoryImage?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 15.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = item.categoryName ?: "",
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    val intent = Intent(context, ProductCategory::class.java)
                                    intent.putExtra("categoryName", item.categoryName)
                                    context.startActivity(intent)
                                }
                            )
                        }
                        IconButton(
                            onClick = {
                                val i = Intent(context, UpdateCategory::class.java)
                                i.putExtra("categoryName", item.categoryName)
                                i.putExtra("categoryID", item.categoryID)
                                i.putExtra("categoryImage", item.categoryImage)
                                context.startActivity(i)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = {
                                deleteDataFromFirebase(item.categoryID, context)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = red
                            )
                        }
                    }
                }
            }
        }
    }
}
