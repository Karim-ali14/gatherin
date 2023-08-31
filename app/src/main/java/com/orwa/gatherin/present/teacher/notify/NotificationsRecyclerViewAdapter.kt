package com.orwa.gatherin.present.teacher.notify

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemNotificationBinding
import com.orwa.gatherin.model.notify.Notification
import com.orwa.gatherin.utils.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class NotificationsRecyclerViewAdapter(
    private var values: List<Notification>,
    private val onClickListener: NotificationClickListener?,
    private val ctx: Context,
    private val emptyVew:View
) : RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder>() {

    private var lang: Lang = Util.getCurrentLang(ctx)

    private lateinit var binding: ItemNotificationBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item:Notification){
            val title:String
            val body:String

            if(lang==Lang.ARABIC){
                title = item.titleAr
                body = item.bodyAr
            }else{
                title =item.titleEn
                body = item.bodyEn
            }
            binding.tvTitle.setText(getFormattedTitleString(title,body))
            binding.tvTime.setText(DateUtil.getDateFromUTC(item.updatedAt).toString())
        }
    }

    fun getFormattedTitleString(title:String, body:String): String {
        return ctx.getString(R.string.notification_content,title, body)
    }

    fun swapData(items:List<Notification>){
        values = items
        if(values.size==0){
            emptyVew.show()
        }else{
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

}