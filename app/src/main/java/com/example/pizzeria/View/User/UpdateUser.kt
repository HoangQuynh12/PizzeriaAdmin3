package com.example.pizzeria.View.User

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
import com.example.pizzeria.Model.UserData
import com.example.pizzeria.ui.theme.PizzeriaTheme
import com.example.pizzeria.ui.theme.blueColor
import com.example.pizzeria.ui.theme.grayFont
import com.google.firebase.firestore.FirebaseFirestore

class UpdateUser : ComponentActivity() {
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
                                    text = "Update User",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    this@UpdateUser.startActivity(
                                        Intent(
                                            this@UpdateUser,
                                            UserInformation::class.java
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
                        UpdateUserUI(
                            context = LocalContext.current,
                            email = intent.getStringExtra("email"),
                            password = intent.getStringExtra("password"),
                            userType = intent.getStringExtra("userType") ?: "",
                            UserID = intent.getStringExtra("UserID")
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserUI(
    context: Context,
    email: String?,
    password: String?,
    userType: String,
    UserID: String?
) {
    val userTypes = arrayOf("User", "Admin")
    var expanded by remember { mutableStateOf(false) }

    var userEmail by remember { mutableStateOf(email.orEmpty()) }
    var userPassword by remember { mutableStateOf(password.orEmpty()) }
    var selectedUserType by remember { mutableStateOf(userType) }

    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blueColor
            ),
            label = { Text(text = "Email", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blueColor
            ),
            label = { Text(text = "Password", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(7.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                OutlinedTextField(
                    value = selectedUserType,
                    onValueChange = { selectedUserType = it },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                        .fillMaxWidth()
                        .height(59.dp),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = blueColor,
                        unfocusedBorderColor = blueColor
                    ),
                    label = { Text(text = "UserType", color = grayFont) }
                )

                ExposedDropdownMenu(
                    modifier = Modifier.background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userTypes.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedUserType = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                when {
                    userEmail.isEmpty() -> Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                    userPassword.isEmpty() -> Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                    selectedUserType.isEmpty() -> Toast.makeText(context, "Please select user type", Toast.LENGTH_SHORT).show()
                    else -> updateDataToFirebase(UserID, userEmail, userPassword, selectedUserType, context)
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
                context.startActivity(Intent(context, UserInformation::class.java))
            },
        ) {
            Text(
                text = "Exit",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = blueColor
            )
        }
    }
}

private fun updateDataToFirebase(
    UserID: String?,
    email: String?,
    password: String?,
    userType: String,
    context: Context
) {
    val updatedUser = UserData(UserID, email, password, userType)

    val db = FirebaseFirestore.getInstance()
    db.collection("User").document(UserID.orEmpty()).set(updatedUser)
        .addOnSuccessListener {
            Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, UserInformation::class.java))
        }.addOnFailureListener {
            Toast.makeText(context, "Update error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
}
