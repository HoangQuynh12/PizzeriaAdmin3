package com.example.pizzeria.View.Order

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.pizzeria.Model.Order
import com.example.pizzeria.Model.OrderItem
import com.example.pizzeria.R
import com.example.pizzeria.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetails : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzeriaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
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
                                        text = "Order Details",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        this@OrderDetails.startActivity(Intent(this@OrderDetails, OrderManager::class.java))
                                    }) {
                                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        val orderID = intent.getStringExtra("OrderID")
                        if (orderID != null) {
                            OrderDetailsUI(orderID, Modifier.padding(paddingValues))
                        } else {
                            Toast.makeText(this@OrderDetails, "Order ID is missing", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderDetailsUI(id: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var order by remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(id) {
        val db = FirebaseFirestore.getInstance()
        val orderRef = db.collection("orders").document(id)
        orderRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                order = documentSnapshot.toObject(Order::class.java)
            } else {
                Toast.makeText(context, "Order not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error while retrieving order details", Toast.LENGTH_SHORT).show()
        }
    }

    order?.let { orderData ->
        LazyColumn(
            Modifier.padding(top = 60.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFEEF7FF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shadowElevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.box),
                                contentDescription = "",
                                modifier = Modifier.size(90.dp),
                                tint = Color(0xFF03AED2)
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Name:",
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                orderData.custumerName?.let {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Address:",
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                orderData.custumerAdd?.let {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Phone number:",
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                orderData.custumerPhone?.let {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Order Date:",
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                orderData.date?.let { date ->
                                    val formattedDate = SimpleDateFormat(
                                        "dd/MM/yyyy HH:mm",
                                        Locale("vi", "VN")
                                    ).format(date)
                                    Text(
                                        text = formattedDate,
                                        fontSize = 18.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total:",
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                orderData.total?.let {
                                    Text(
                                        text = "$ " + it,
                                        fontSize = 18.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Status:",
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                orderData.status?.let {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Order Items",
                                    fontSize = 17.sp,
                                    color = darkblue,
                                    fontWeight = FontWeight.Medium,
                                    textDecoration = TextDecoration.Underline
                                )
                            }
                            orderData.items?.forEach { item ->
                                OrderItemRow(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        ) {
            Image(
                painter = rememberImagePainter(item.image),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = item.name ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = blackcart
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Price: $ ${item.price ?: 0.0}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Quantity: ${item.quantity ?: 0}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Note: ${item.note ?: ""}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
