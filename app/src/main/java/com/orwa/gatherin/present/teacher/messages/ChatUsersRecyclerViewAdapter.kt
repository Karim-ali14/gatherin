package com.orwa.gatherin.present.teacher.messages

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemChatUserBinding
import com.orwa.gatherin.model.chat.ChatUsersListResItem
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show
import java.util.*
import kotlin.collections.ArrayList


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ChatUsersRecyclerViewAdapter(
    private var values: List<ChatUsersListResItem>,
    private val userOnClickListener : (chatUser:ChatUsersListResItem)->Unit,
    private val ctx: Context,
    private val emptyVew: TextView
) : RecyclerView.Adapter<ChatUsersRecyclerViewAdapter.ViewHolder>() {

    private var newValues: ArrayList<ChatUsersListResItem> = ArrayList()

    private val TAG = ChatUsersRecyclerViewAdapter::class.java.simpleName

//    var lastLeaderPos: Int = -1

    private lateinit var binding: ItemChatUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemChatUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        
        val item = newValues[position]
        Log.i(TAG,"BIND_ITEM=$item")
        holder.bind(item)

    }

    override fun getItemCount(): Int{
            return newValues.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val fullName:TextView = view.findViewById(R.id.tvUserName)
        val unreadMessage = view.findViewById<TextView>(R.id.tvUnreadMessages)

        init {
            view.setOnClickListener {

                val chatUser = newValues[bindingAdapterPosition]

                userOnClickListener.invoke(chatUser)

            }
        }

        fun bind(item: ChatUsersListResItem) {
//            Util.loadImage(ctx, binding.ivProfile, item.picture)
            Util.loadImageWithPlaceholder(ctx, binding.ivProfile, item.picture,R.drawable.ic_error_copy_2)

            Log.i(TAG,"FULL_NAME=${item.fullName}")
            fullName.setText(item.fullName)
            unreadMessage.setText(itemView.context.getString(R.string.unread_messages,item.countOfUnReadChat))
        }
    }



    fun swapData(items: List<ChatUsersListResItem>) {
        Log.i(TAG, "ITEMS=$items")
        values = items
        if (values.size == 0) {
            emptyVew.show()
        } else {
            emptyVew.text = ctx.getString(R.string.search_member_no_matches)
            emptyVew.hide()
        }
        newValues.clear()
        newValues.addAll(values)
        notifyDataSetChanged()
    }

    fun searchForMember(query:String){
        if(query.isNotEmpty()){
            val result = values.filter { it.fullName.contains(query,ignoreCase = true) }
            Log.i(TAG,"filter=$result")
            if(result.isEmpty()){
                emptyVew.show()
            }else{
                emptyVew.hide()
            }
            newValues.clear()
            newValues.addAll(result)
        }else{
            emptyVew.hide()

            newValues.clear()
            newValues.addAll(values)
        }
        notifyDataSetChanged()
    }




}