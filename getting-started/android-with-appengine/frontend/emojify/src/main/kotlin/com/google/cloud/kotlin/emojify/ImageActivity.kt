/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.kotlin.emojify

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import android.util.NoSuchPropertyException
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.google.cloud.kotlin.emojify.databinding.ActivityListContentBinding
import com.google.cloud.kotlin.emojify.databinding.ToolbarBinding
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.yanzhenjie.album.widget.divider.Api21ItemDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.ArrayList
import java.util.Properties

class ImageActivity : AppCompatActivity() {

    private lateinit var backendUrl: String
    private lateinit var adapter: Adapter
    private val albumFiles: MutableList<AlbumFile> = mutableListOf()
    private val job = Job()
    private var emjojifiedUrl: String = ""
    private var imageId: String = ""
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var toolbarBinding: ToolbarBinding
    private lateinit var activityListContentBinding: ActivityListContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        setSupportActionBar(toolbarBinding.toolbar)

        activityListContentBinding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@ImageActivity, 3)
            adapter = Adapter { previewImage(0) }
            val divider = Api21ItemDivider(Color.TRANSPARENT, 10, 10)
            addItemDecoration(divider)
        }

        val properties = Properties()
        properties.load(assets.open("application.properties"))
        val projectId = properties["cloud.project.id"] ?: throw NoSuchPropertyException("property 'cloud.project.id' doesn't exist in application.properties!")
        backendUrl = "https://$projectId.appspot.com"
        show("First, select picture to emojify!")
        selectImage()
    }

    private fun show(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun updateUI(fct: () -> Unit) = this.runOnUiThread(java.lang.Runnable(fct))

    private fun callEmojifyBackend() {
        val queue = Volley.newRequestQueue(this)
        val url = "${this.backendUrl}/emojify?objectName=$imageId"
        updateUI { show("Image uploaded to Storage!") }
        val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val statusCode = response["statusCode"]
                    if (statusCode != "OK") {
                        updateUI {
                            show("Oops!")
                            activityListContentBinding.tvMessage.text = response["errorMessage"].toString()
                        }
                        Log.i("backend response", "${response["statusCode"]}, ${response["errorCode"]}")
                    } else {
                        updateUI {
                            show("Yay!")
                            activityListContentBinding.tvMessage.text = getString(R.string.waiting_over)
                        }
                        emjojifiedUrl = response["emojifiedUrl"].toString()
                        downloadAndShowImage()
                    }
                    deleteSourceImage()
                },
                { err ->
                    updateUI {
                        show("Error calling backend!")
                        activityListContentBinding.tvMessage.text = getString(R.string.backend_error)
                    }
                    Log.e("backend", err?.message ?: "unknown")
                    deleteSourceImage()
                })
        request.retryPolicy = DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)
    }

    private fun deleteSourceImage() = storageRef.child(imageId).delete()
            .addOnSuccessListener { Log.i("deleted", "Source image successfully deleted!") }
            .addOnFailureListener { err -> Log.e("delete", err.message ?: "unknown") }

    private fun uploadImage(path: String) {
        val file = Uri.fromFile(File(path))
        imageId = "${System.currentTimeMillis()}.jpg"
        val imgRef = storageRef.child(imageId)
        updateUI {
            activityListContentBinding.imageView.visibility = View.GONE
            activityListContentBinding.tvMessage.text = getString(R.string.waiting_msg_1)
        }
        imgRef.putFile(file, StorageMetadata.Builder().setContentType("image/jpg").build())
                .addOnSuccessListener {
                    updateUI { activityListContentBinding.tvMessage.text = getString(R.string.waiting_msg_2) }
                    callEmojifyBackend()
                }
                .addOnFailureListener { err ->
                    updateUI {
                        show("Cloud Storage error!")
                        activityListContentBinding.tvMessage.text = getString(R.string.storage_error)
                    }
                    Log.e("storage", err.message ?: "unknown")
                }
    }

    private fun downloadAndShowImage() {
        val url = emjojifiedUrl
        updateUI {
            Glide.with(this)
                    .load(url)
                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontTransform()
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .skipMemoryCache(true))
                    .into(activityListContentBinding.imageView)
        }
        activityListContentBinding.imageView.visibility = View.VISIBLE
    }

    private fun load(path: String) =
            CoroutineScope(Dispatchers.IO + job).launch {
                uploadImage(path)
            }

    private fun selectImage() {
        Album.image(this)
            .singleChoice()
            .camera(true)
            .columnCount(2)
            .widget(
                Widget.newDarkBuilder(this)
                    .title(toolbarBinding.toolbar.title.toString())
                    .build()
            )
            .onResult { result ->
                albumFiles.clear()
                albumFiles.addAll(result)
                activityListContentBinding.tvMessage.visibility = View.VISIBLE
                if (result.size > 0) load(result[0].path)
            }
            .onCancel {
                finish()
            }
            .start()
    }

    private fun previewImage(position: Int) {
        if (albumFiles.isEmpty()) Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show()
        else {
            Album.galleryAlbum(this)
                .checkable(false)
                .checkedList(albumFiles as ArrayList<AlbumFile>)
                .currentPosition(position)
                .widget(
                    Widget.newDarkBuilder(this)
                        .title(toolbarBinding.toolbar.title.toString())
                        .build()
                )
                .onResult { result ->
                    albumFiles.clear()
                    albumFiles.addAll(result)
                    adapter.submitList(albumFiles)
                    activityListContentBinding.tvMessage.visibility = if (result.size > 0) View.VISIBLE else View.GONE
                }
                .start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_album_image, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> selectImage()
            R.id.menu_eye -> previewImage(0)
        }
        return true
    }
}
