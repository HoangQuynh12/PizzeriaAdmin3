package com.example.pizzeria.View.User

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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzeria.MainActivity
import com.example.pizzeria.Model.UserData
import com.example.pizzeria.R
import com.example.pizzeria.ui.theme.Pink40
import com.example.pizzeria.ui.theme.Pink80
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.bg
import com.example.pizzeria.ui.theme.blue
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.darkblue
import com.example.pizzeria.ui.theme.red
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
                                c?.UserID = d.id
                                Log.e("TAG", "Course id is : " + c!!.UserID)
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
            .padding(0.dp, 70.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        LazyColumn {
            itemsIndexed(userList) { index, item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = blue,
                    modifier = Modifier
                        .height(220.dp)
                        .padding(8.dp),
                    shadowElevation = 10.dp,
                    onClick = {

                    }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = blue,
                            shape = CircleShape,
                            modifier = Modifier.size(100.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.user),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,

                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f)
                                .padding(horizontal = 15.dp, vertical = 0.dp),
                            verticalArrangement = Arrangement.Center
                        ) {


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),

                                ) {
                                Text(
                                    text = "Email: ",
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,

                                    )
                                Spacer(modifier = Modifier.height(3.dp))
                                userList[index]?.email?.let {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Pink40,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),

                                ) {
                                Text(
                                    text = "Password: ",
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,

                                    )
                                userList[index]?.password?.let {
                                    Text(
                                        text = it,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 18.sp,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Pink40,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),

                                ) {
                                Text(
                                    text = "UserType: ",
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,

                                    )
                                userList[index]?.userType?.let {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Pink40,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                IconButton(
                                    onClick = {
                                        val i = Intent(context, UpdateUser::class.java)
                                        i.putExtra("email", item.email)
                                        i.putExtra("UserID", item.UserID)
                                        i.putExtra("password", item.password)
                                        i.putExtra("userType", item.userType)
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
                                        deleteDataFromFirebase(item.UserID, context)
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
    }

}