package com.orwa.gatherin.present.teacher.notify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.orwa.gatherin.R
import com.orwa.gatherin.model.notify.Notification

class MovieAdapter :
    PagingDataAdapter<Notification, MovieAdapter.MovieViewHolder>(MovieComparator){

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        Log.i("Adapter","_test_onBindViewHolder")
//        holder.itemView.movie_title.text = getItem(position)!!.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        )
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view)

    object MovieComparator : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

}