package com.example.pizzeria

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.pizzeria.ui.theme.PizzeriaTheme

val defaultPadding = 16.dp
val itemSpacing = 8.dp

@Composable
fun Login(onSignUpClick: () -> Unit) {
    val (userName, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (checked, onCheckedChange) = rememberSaveable { mutableStateOf(false) }
    val passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_login),
            contentDescription = "Login Icon",
            modifier = Modifier.size(200.dp)
        )
        HeaderText(
            text = "Sign In",
            modifier = Modifier
                .padding(vertical = defaultPadding)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = userName,
            onValueChange = setUsername,
            labelText = "Username",
            leadingIcon = Icons.Filled.Person,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),

            )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = setPassword,
            labelText = "Password",
            leadingIcon = Icons.Filled.Lock,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),

            )
        Spacer(Modifier.height(itemSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checked, onCheckedChange = onCheckedChange)
                Text("Remember me")
            }
            TextButton(onClick = {}) {
                Text("Forgot Password?")
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }
        AlternativeLoginOptions(
            onIconClick = { index ->
                when (index) {
                    0 -> {
                        Toast.makeText(context, "Instagram Login Click", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        Toast.makeText(context, "Github Login Click", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(context, "Google Login Click", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onSignUpClick = onSignUpClick,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        )
    }
}

@Composable
fun AlternativeLoginOptions(
    onIconClick: (index: Int) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val iconList = listOf(
//        R.drawable.instagram,
//        R.drawable.github,
//        R.drawable.google
//    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text("Or Sign in With")
//        Spacer(Modifier.height(itemSpacing))
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            iconList.forEachIndexed { index, iconResId ->
//                Icon(
//                    painter = painterResource(iconResId),
//                    contentDescription = "alternative Login",
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clickable {
//                            onIconClick(index)
//                        }
//                        .clip(CircleShape)
//                )
//                Spacer(Modifier.width(defaultPadding))
//            }
//        }
        Spacer(Modifier.height(itemSpacing))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an Account?")
            Spacer(Modifier.height(itemSpacing))
            TextButton(onClick = onSignUpClick) {
                Text("Sign Up")
            }
        }
    }
}
@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions,
){
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {Text(text = labelText)},
        leadingIcon = {if (leadingIcon != null) Icon(imageVector = leadingIcon,null)},
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(30),
    )

}
@Composable
fun HeaderText(
    text: String,
    modifier: Modifier = Modifier,
){
    Text(text = text,
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier)
}
@Preview(showBackground = true)
@Composable
fun PrevTextField(){
    LoginTextField(
        value = "",
        onValueChange = {},
        labelText = "Password",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
    )
}


@Preview(showSystemUi = true)
@Composable
fun PrevLogin() {
    PizzeriaTheme {
        Login({})
    }
}

