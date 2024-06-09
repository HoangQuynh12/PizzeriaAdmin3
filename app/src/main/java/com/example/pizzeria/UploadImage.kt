package com.example.pizzeria

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.pizzeria.ui.theme.blueColor
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

fun uploadToStorage(uri: Uri, context: Context, type: String, onImageUploaded: (String) -> Unit) {
    val storage = com.google.firebase.ktx.Firebase.storage
    // Create a storage reference from our app
    val storageRef = storage.reference

    val uniqueImageName = UUID.randomUUID().toString()
    val spaceRef: StorageReference = storageRef.child("$uniqueImageName.jpg")

    val byteArray: ByteArray? = context.contentResolver
        .openInputStream(uri)
        ?.use { it.readBytes() }

    byteArray?.let {
        val uploadTask = spaceRef.putBytes(byteArray)
        uploadTask.addOnFailureListener {
            Toast.makeText(
                context,
                "Tải file thất bại",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnSuccessListener { _ ->
            spaceRef.downloadUrl.addOnSuccessListener { uri ->
                onImageUploaded(uri.toString())
            }.addOnFailureListener {
                // Handle failures
                Toast.makeText(
                    context,
                    "Lấy URL thất bại",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun SelectItemImageSection(onImageSelected: (String) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val getImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
            // Call uploadToStorage function with appropriate type
            uploadToStorage(uri, context, "image") { imageUrl ->
                onImageSelected(imageUrl) // Pass the URL to the callback function
            }
        }
    }
    Surface(
        modifier = Modifier
            .size(150.dp).border(2.dp, blueColor),
        shadowElevation = 3.dp,
    ) {

        IconButton(onClick = {
            getImage.launch("image/*")
        }) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
        }
        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Chọn Ảnh",
//                    modifier = Modifier
//                        .size(150.dp),
                contentScale = ContentScale.Crop,
            )
        }

// Hiển thị trước ảnh đã chọn

    }
}