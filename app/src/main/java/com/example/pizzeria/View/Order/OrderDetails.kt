package com.example.pizzeria.View.Order

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = blue,
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
                    Image(
                        painter = painterResource(id = R.drawable.order),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Name: ",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Pink40
                        )
                        orderData.custumerName?.let {
                            Text(
                                text = it,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Address: ",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Pink40
                        )
                        orderData.custumerAdd?.let {
                            Text(
                                text = it,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Order Date: ",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Pink40
                        )
                        orderData.date?.let { date ->
                            val formattedDate = SimpleDateFormat(
                                "dd/MM/yyyy HH:mm",
                                Locale.getDefault()
                            ).format(date)
                            Text(
                                text = formattedDate,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Status: ",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Pink40
                        )
                        orderData.status?.let {
                            Text(
                                text = it,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Phone Number: ",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Pink40
                        )
                        orderData.custumerPhone?.let {
                            Text(
                                text = it,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Total: ",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Pink40
                        )
                        orderData.total?.let {
                            Text(
                                text = "$ " + it,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Order Items:",
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Pink40
                    )
                    orderData.items?.forEach { item ->
                        Spacer(modifier = Modifier.height(6.dp))
                        OrderItemRow(item)
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
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
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
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Pink40
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Description: ${item.description ?: ""}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Price: $ ${item.price ?: 0.0}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Quantity: ${item.quantity ?: 0}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
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
