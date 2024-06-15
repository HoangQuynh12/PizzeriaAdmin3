package com.example.pizzeria.View.Revenue

import android.annotation.SuppressLint
import kotlinx.coroutines.tasks.await
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.pizzeria.MainActivity
import com.example.pizzeria.Model.Order
import com.example.pizzeria.R
import com.example.pizzeria.View.Order.OrderDetails
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blackcart
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.darkblue
import com.example.pizzeria.ui.theme.green
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


class Revenue : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
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
                                    text = "Revenue",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    this@Revenue.startActivity(Intent(this@Revenue, MainActivity::class.java))
                                }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                }
                            },
                        )
                    }

                    val orderList = remember { mutableStateListOf<Order>() }
                    val totalConfirmed = remember { mutableStateOf(0.0) }
                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val dbOrder: CollectionReference = db.collection("orders")

                    LaunchedEffect(Unit) {
                        val confirmedOrders = dbOrder.whereEqualTo("status", "Delivered").get().await()
                        var total = 0.0
                        for (document in confirmedOrders.documents) {
                            val order = document.toObject(Order::class.java)
                            order?.id = document.id
                            orderList.add(order ?: continue)
                            total += order?.total ?: 0.0
                        }
                        totalConfirmed.value = total
                    }

                    RevenueUI(LocalContext.current, orderList, totalConfirmed.value)
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueUI(context: Context, orderList: SnapshotStateList<Order>, totalConfirmed: Double) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 70.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = String.format("Revenue: $%.2f", totalConfirmed),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            Modifier.padding(vertical = 15.dp, horizontal = 15.dp)
        ) {
            itemsIndexed(orderList.sortedByDescending { it.date }) { index, item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = null,
                                modifier = Modifier.height(20.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            item.date?.let { date ->
                                val formattedDate = SimpleDateFormat(
                                    "dd/MM/yyyy HH:mm",
                                    Locale("vi", "VN")
                                ).format(date)
                                Text(
                                    text = formattedDate,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontSize = 15.sp,
                                    color = blackcart
                                )
                            }
                        }

                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEDFFF2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFC4FFCE),
                                shape = RoundedCornerShape(12.dp),
                            )
                            .padding(bottom = 2.dp),
                        shadowElevation = 6.dp,
                        onClick = {
                            val i = Intent(context, OrderDetails::class.java)
                            i.putExtra("OrderID", item.id)
                            context.startActivity(i)
                        }
                    ) {
                        Row {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxWidth()
                                    .weight(2f),
                            ) {
                                androidx.compose.material3.Surface(
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(bottom = 12.dp),
                                    color = green
                                ) {

                                    item.status?.let {
                                        Text(
                                            text = it,
                                            fontSize = 15.sp,
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(
                                                vertical = 4.dp,
                                                horizontal = 8.dp
                                            ),
                                            color = Color.White
                                        )
                                    }

                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 13.dp)
                                ) {
                                    Text(
                                        text = "Total:",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    item.total?.let {
                                        val formattedPrice = NumberFormat.getCurrencyInstance(Locale.US).format(it)
                                        Text(
                                            text = formattedPrice,
                                            fontSize = 17.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 5.dp)
                                ) {
                                    Text(
                                        text = "Customer:",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${item.custumerName}",
                                        fontSize = 17.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                        }

                    }

                }
            }
        }
    }
}
