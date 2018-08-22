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

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.impl.OnItemClickListener
import kotlinx.android.synthetic.main.item_content_image.view.iv_album_content_image as my_content_image

class Adapter(private val context: Context, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var albumFiles: List<AlbumFile>? = null

    fun notifyDataSetChanged(imagePathList: List<AlbumFile>) {
        this.albumFiles = imagePathList
        super.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ImageViewHolder(inflater.inflate(R.layout.item_content_image, parent, false), itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as ImageViewHolder).setData(albumFiles!![position])

    override fun getItemCount(): Int = if (albumFiles == null) 0 else albumFiles!!.size

    private class ImageViewHolder internal constructor(itemView: View, private val itemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ivImage: ImageView = itemView.my_content_image

        init {
            itemView.setOnClickListener(this)
        }

        fun setData(albumFile: AlbumFile) {
            Album.getAlbumConfig().albumLoader.load(ivImage, albumFile)
        }

        override fun onClick(v: View) {
            itemClickListener?.onItemClick(v, adapterPosition)
        }
    }
}