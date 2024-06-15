package com.example.pizzeria

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.pizzeria.Model.Order
import com.example.pizzeria.View.Category.CategoryDetails
import com.example.pizzeria.View.Order.OrderDetails
import com.example.pizzeria.View.Order.OrderManager
import com.example.pizzeria.View.Product.ProductDetails
import com.example.pizzeria.View.Revenue.Revenue
import com.example.pizzeria.View.User.UserInformation
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.bg
import com.example.pizzeria.ui.theme.blue1
import com.example.pizzeria.ui.theme.blue2
import com.example.pizzeria.ui.theme.darkblue
import com.example.pizzeria.ui.theme.menu
import com.example.pizzeria.ui.theme.menu1
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzeriaTheme {
                HomeScreen(LocalContext.current)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(context: Context){

    var productCount by remember { mutableStateOf(0L) }
    var orderCount by remember { mutableStateOf(0L) }
    var totalConfirmed by remember { mutableStateOf(0.0) }

    // Lấy dữ liệu từ Firestore và cập nhật các giá trị
    LaunchedEffect(Unit) {
        productCount = getProductCount()
        orderCount = getOrderCount()
        totalConfirmed = getTotalConfirmed()
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = bg)
            .padding(bottom = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout {
            val (topImg, profile) = createRefs()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .constrainAs(topImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .background(
                        color = blue2,
                        shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
                    )
            )
            Row(
                modifier = Modifier
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(start = 14.dp)
                        .weight(0.7f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Hello",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Admin Pizzeria",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.profilead),
                    contentDescription = "",
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(CircleShape)
                        .border(border = BorderStroke(3.dp, menu), shape = CircleShape)
                        .clickable { }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .shadow(3.dp, shape = RoundedCornerShape(20.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .constrainAs(profile) {
                        top.linkTo(topImg.bottom)
                        bottom.linkTo(topImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Surface(
                    onClick = {
                        context.startActivity(Intent(context, ProductDetails::class.java))
                    },
                    color = blue1,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(top = 3.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(45.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_category),
                                contentDescription = "",
                                tint = darkblue,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Products",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = productCount.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                    }
                }
                Surface(
                    onClick = {
                        context.startActivity(Intent(context, OrderManager::class.java))
                    },
                    color = blue1,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(top = 3.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(45.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_pending),
                                contentDescription = "",
                                tint = darkblue,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Orders",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = orderCount.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                    }
                }
                Surface(
                    onClick = {
                        context.startActivity(Intent(context, Revenue::class.java))
                    },
                    color = blue1,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(top = 3.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(45.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_money),
                                contentDescription = "",
                                tint = darkblue,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Revenue",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = String.format("$%.2f", totalConfirmed),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Surface(
                onClick = {
                    context.startActivity(Intent(context, ProductDetails::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue2,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu1, shape = RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_category),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Products",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }
            Surface(
                onClick = {
                    context.startActivity(Intent(context, OrderManager::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue2,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu1, shape = RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_order),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp, start = 2.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Order Manager",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }
            Surface(
                onClick = {
                    context.startActivity(Intent(context, Revenue::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue2,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu1, shape = RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_money),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Revenue Manger",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp, horizontal = 16.dp)
        ) {
            Surface(
                onClick = {
                    context.startActivity(Intent(context, CategoryDetails::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue2,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu1, shape = RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_category),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category Manager",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }

            Surface(
                onClick = {
                    context.startActivity(Intent(context, UserInformation::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue2,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu1, shape = RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_creater),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "User Manger",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }

        }
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}

// Hàm để lấy số lượng người dùng
suspend fun getProductCount(): Long {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("Product")
    val querySnapshot = userCollection.get().await()
    return querySnapshot.size().toLong()
}

// Hàm để lấy số lượng đơn hàng
suspend fun getOrderCount(): Long {
    val db = FirebaseFirestore.getInstance()
    val orderCollection = db.collection("orders")
    val querySnapshot = orderCollection.get().await()
    return querySnapshot.size().toLong()
}

// Hàm để tính tổng total của các đơn hàng đã xác nhận
suspend fun getTotalConfirmed(): Double {
    val db = FirebaseFirestore.getInstance()
    val orderCollection = db.collection("orders")
    val querySnapshot = orderCollection.whereEqualTo("status", "Delivered").get().await()

    var totalConfirmed = 0.0
    for (document in querySnapshot) {
        val order = document.toObject<Order>()
        totalConfirmed += order.total ?: 0.0
    }
    return totalConfirmed
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun PreviewHome(){
    PizzeriaTheme{
        HomeScreen(LocalContext.current)
    }
}