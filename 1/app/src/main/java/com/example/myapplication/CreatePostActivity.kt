package com.example.myapplication

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.daos.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*
import android.content.Intent
import android.provider.MediaStore

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import com.google.firebase.storage.UploadTask

import android.widget.Toast

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener

import com.google.firebase.storage.StorageReference

import android.app.ProgressDialog
import com.google.firebase.storage.OnProgressListener
import java.util.*


class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao
    private lateinit var filePath: Uri
    private val PICK_IMAGE_REQUEST = 71
    val storage=FirebaseStorage.getInstance()
    val storageReference = storage.getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postDao = PostDao()

        addimg.setOnClickListener {

            chooseImage();

        }

        postButton.setOnClickListener {
            //uploadImage()
            val input = postInput.text.toString().trim()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }
        }

        setUpRecyclerView()



    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun setUpRecyclerView() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                img.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            //val progressDialog = ProgressDialog(this)
            //progressDialog.setTitle("Uploading...")
            //progressDialog.show()
            val ref = storageReference.child("images/" + UUID.randomUUID().toString())
            ref.putFile(filePath)
                .addOnSuccessListener {
                    //progressDialog.dismiss()
                    Toast.makeText(this@CreatePostActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    //progressDialog.dismiss()
                    Toast.makeText(this@CreatePostActivity, "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?>{
                    override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        //progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
                })
        }
    }
}