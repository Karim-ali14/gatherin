package com.orwa.gatherin.present.student.chats

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemChatMasterBinding
import com.orwa.gatherin.model.chat.ChatUsersListResItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MasterChatRecyclerViewAdapter(
    private var values: List<ChatUsersListResItem>,
    private val userOnClickListener : (chatUser:ChatUsersListResItem)->Unit,
    private val ctx: Context,
    private val emptyVew: View
) : RecyclerView.Adapter<MasterChatRecyclerViewAdapter.ViewHolder>() {

    private val TAG = MasterChatRecyclerViewAdapter::class.java.simpleName

//    var lastLeaderPos: Int = -1

    private lateinit var binding: ItemChatMasterBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemChatMasterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {

                val chatUser = values[bindingAdapterPosition]

                userOnClickListener.invoke(chatUser)

            }
        }

        fun bind(item: ChatUsersListResItem) {
            binding.tvMasterName.setText(getFormattedName(item.fullName))
        }
    }



    fun swapData(items: List<ChatUsersListResItem>) {
//        Log.i(TAG, "ITEMS=$items")
        values = items
        if (values.size == 0) {
            emptyVew.show()
        } else {
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

    fun getFormattedName(name:String):String{
        return ctx.getString(R.string.master_chat_item_name,name)
    }

}