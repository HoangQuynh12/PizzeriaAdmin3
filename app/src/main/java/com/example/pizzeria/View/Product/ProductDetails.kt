package com.example.pizzeria.View.Product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pizzeria.MainActivity
import com.example.pizzeria.Model.ProductData
import com.example.pizzeria.ui.theme.Pink40
import com.example.pizzeria.ui.theme.Pink80
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blue1
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.darkblue
import com.example.pizzeria.ui.theme.delete
import com.example.pizzeria.ui.theme.red
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore



class ProductDetails : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzeriaTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ) {
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
                                    text = "All Product",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    this@ProductDetails.startActivity(Intent(this@ProductDetails, MainActivity::class.java))
                                }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                }
                            },
//                            actions = {
//                                var expanded by remember { mutableStateOf(false) }
//                                val productType = remember { mutableStateOf("") }
//                                val context = LocalContext.current
//
//                                IconButton(onClick = { expanded = true }) {
//                                    Icon(imageVector = Icons.Filled.List, contentDescription = null)
//                                }
//                            }
                        )
                    }

                    var productList = mutableStateListOf<ProductData>()
                    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val dbProducts: CollectionReference = db.collection("Product")

                    dbProducts.get().addOnSuccessListener { queryDocumentSnapshot ->
                        if (!queryDocumentSnapshot.isEmpty){
                            val list = queryDocumentSnapshot.documents
                            for (d in list){
                                val c: ProductData? = d.toObject(ProductData::class.java)
                                c?.productID = d.id
                                Log.e("TAG", "Course id is : " + c!!.productID)
                                productList.add(c)
                            }
                        }else{
                            Toast.makeText(
                                this@ProductDetails,
                                "There is no data in the Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@ProductDetails,
                            "Error while retrieving data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    ProductDetailsUI(LocalContext.current, productList)

                }
            }
        }
    }
}
private fun deleteDataFromFirebase(productID: String?, context: Context) {

    val db = FirebaseFirestore.getInstance();
    db.collection("Product").document(productID.toString()).delete().addOnSuccessListener {
        Toast.makeText(context, "Product removed", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, ProductDetails::class.java))
    }.addOnFailureListener {
        Toast.makeText(context, "Error while deleting produc", Toast.LENGTH_SHORT).show()
    }

}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsUI(context: Context, productList: SnapshotStateList<ProductData>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 5.dp, top = 70.dp, end = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(onClick = { context.startActivity(Intent(context, AddProduct::class.java)) }) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
        }

        LazyColumn {
            itemsIndexed(productList) { index, item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = blue1,
                    modifier = Modifier
                        .padding(5.dp),
                    shadowElevation = 10.dp,
                    onClick = {
                        val i = Intent(context, UpdateProducts::class.java)
                        i.putExtra("productName", item?.productName)
                        i.putExtra("productType", item?.productType)
                        i.putExtra("productPrice", item?.productPrice)
                        i.putExtra("productDescription", item?.productDescription)
                        i.putExtra("productImage", item?.productImage)
                        i.putExtra("productID", item?.productID)

                        context.startActivity(i)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(width = 160.dp, height = 160.dp)
                        ) {
                            productList[index]?.productImage?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f)
                                .padding(horizontal = 15.dp, vertical = 0.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.wrapContentSize(),
                                color = blue
                            ) {
                                productList[index]?.productType?.let {
                                    Text(
                                        text = it,
                                        fontSize = 10.sp,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(
                                            vertical = 4.dp,
                                            horizontal = 8.dp
                                        ),
                                        color = Color.DarkGray
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            productList[index]?.productName?.let {
                                Text(
                                    text = it,
                                    fontSize = 17.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            productList[index]?.productPrice?.let {
                                Text(text = "$ "+it,
                                    fontSize = 17.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "5.0",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )
                            }
                        }

                    }
                    Row() {
                        Surface(
                            onClick = {
                                deleteDataFromFirebase(item.productID, context)
                            },
                            shape = CircleShape,
                            color = delete
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "",
                                tint = Color(0xFFFFFFFF),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(2.dp),
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProductDetailsUI(){
    ProductDetailsUI(LocalContext.current, SnapshotStateList())
}
