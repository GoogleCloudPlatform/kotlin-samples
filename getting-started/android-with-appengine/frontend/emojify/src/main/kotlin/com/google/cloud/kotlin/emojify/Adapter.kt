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

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.cloud.kotlin.emojify.databinding.ItemContentImageBinding
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile

class AlbumFileDiffCallback : DiffUtil.ItemCallback<AlbumFile>() {

    override fun areItemsTheSame(oldItem: AlbumFile, newItem: AlbumFile): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: AlbumFile, newItem: AlbumFile): Boolean {
        return oldItem == newItem
    }
}

class Adapter(private val clickListener: (AlbumFile) -> Unit) : ListAdapter<AlbumFile, Adapter.ViewHolder>(AlbumFileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_content_image, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), clickListener)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var itemContentImageBinding: ItemContentImageBinding

        fun bind(albumFile: AlbumFile, clickListener: (AlbumFile) -> Unit) {
            Album.getAlbumConfig().albumLoader.load(itemContentImageBinding.ivAlbumContentImage, albumFile)
            itemView.setOnClickListener { clickListener(albumFile) }
        }
    }
}
