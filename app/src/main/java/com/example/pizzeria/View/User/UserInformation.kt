package com.example.pizzeria.View.User

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import coil.compose.rememberAsyncImagePainter
import com.example.pizzeria.MainActivity
import com.example.pizzeria.Model.UserData
import com.example.pizzeria.R
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blackcart
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.delete
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore



class UserInformation : ComponentActivity() {
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
                                    text = "All User",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    this@UserInformation.startActivity(Intent(this@UserInformation, MainActivity::class.java))
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

                    var userList = mutableStateListOf<UserData>()
                    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val dbUser: CollectionReference = db.collection("User")

                    dbUser.get().addOnSuccessListener { queryDocumentSnapshot ->
                        if (!queryDocumentSnapshot.isEmpty){
                            val list = queryDocumentSnapshot.documents
                            for (d in list){
                                val c: UserData? = d.toObject(UserData::class.java)
                                c?.userID = d.id
                                Log.e("TAG", "Course id is : " + c!!.userID)
                                userList.add(c)
                            }
                        }else{
                            Toast.makeText(
                                this@UserInformation,
                                "There is no data in the Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@UserInformation,
                            "Error while retrieving data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    UserInformationUI(LocalContext.current, userList)

                }
            }
        }
    }
}
private fun deleteDataFromFirebase(UserID: String?, context: Context) {

    val db = FirebaseFirestore.getInstance();
    db.collection("User").document(UserID.toString()).delete().addOnSuccessListener {
        Toast.makeText(context, "User removed", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, UserInformation::class.java))
    }.addOnFailureListener {
        Toast.makeText(context, "Error while deleting user", Toast.LENGTH_SHORT).show()
    }

}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInformationUI(context: Context, userList: SnapshotStateList<UserData>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 75.dp, 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        LazyColumn {
            itemsIndexed(userList) { index, item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .padding(end = 5.dp),
//                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier.height(20.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            userList[index]?.role?.let {
                                Text(
                                    text = it,
                                    fontSize = 15.sp,
                                    color = blackcart,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(12.dp),
                            )
                            .padding(bottom = 2.dp),
                        shadowElevation = 6.dp,
                        onClick = {

                        }
                    ) {

                        Row(
                            modifier = Modifier
                                .padding(bottom = 10.dp, start = 10.dp),
                        ) {
                            androidx.compose.material.Surface(
                                shape = RoundedCornerShape(9.dp),
                                modifier = Modifier
                                    .padding(top = 22.dp)
                                    .size(width = 70.dp, height = 70.dp)
                            ) {

                                userList[index]?.image?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(it),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 20.dp)
                                    .fillMaxWidth()
                                    .weight(2f),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Name:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.fullName?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Phone:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.phoneNumber?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Email:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.email?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Address:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.address?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(0.3f),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.End
                            ) {
                                Button(
                                    onClick = {
                                deleteDataFromFirebase(item.userID, context)
                                    },
                                    contentPadding = PaddingValues(),
                                    shape = CircleShape,
                                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = delete, contentColor = Color.White),
                                    modifier = Modifier
                                        .width(23.dp)
                                        .height(23.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        null,
                                        modifier = Modifier
                                            .size(18.dp),
                                        tint = Color.White
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
