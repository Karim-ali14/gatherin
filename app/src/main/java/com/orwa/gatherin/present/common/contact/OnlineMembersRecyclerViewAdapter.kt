package com.orwa.gatherin.present.common.contact

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.RvItemChatUserBinding
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class OnlineMembersRecyclerViewAdapter(
    private var values: List<GroupMember>,
    private val ctx: Context
) : RecyclerView.Adapter<OnlineMembersRecyclerViewAdapter.ViewHolder>() {

    private val TAG = OnlineMembersRecyclerViewAdapter::class.java.simpleName

//    var lastLeaderPos: Int = -1

    private lateinit var binding: RvItemChatUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            RvItemChatUserBinding.inflate(
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

    override fun getItemCount(): Int{
//        Log.i(TAG,"MEMBERS_COUNT=${values.size}")
        return values.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name = binding.root.findViewById<TextView>(R.id.tvUserName)
        val userImg = binding.root.findViewById<ImageView>(R.id.ivChat)
        val isOnlineImg = binding.root.findViewById<ImageView>(R.id.ivOnline)

        fun bind(item: GroupMember) {
//            Log.i(TAG,"BIND_ITEM=${item.toString()}")
//            Log.i(TAG,"BIND_ITEM=${item.id}, ${item.name}")

            Util.loadImageWithPlaceholder(ctx, userImg, item.img)
            name.setText(item.name)
            if(item.online){
                isOnlineImg.show()
            }else{
                isOnlineImg.hide()
            }
        }
    }


//    fun swapData(items: List<GroupMember>) {
//        Log.i(TAG, "ITEMS=$items")
//        values = items
//        if (values.size == 0) {
//            emptyVew.show()
//        } else {
//            emptyVew.hide()
//        }
//        notifyDataSetChanged()
//    }

    fun setOnlineMembers(items:List<ChatUser>){
        for(x in values.indices){
            values[x].online = checkItemOnline(values[x],items)
        }
        notifyItemRangeChanged(0,values.size)


//        for(i in items.indices){
//            val isOnline = setMemberOnline(items[i].userId)
//            notifyItemChanged(i)
////            if(isOnline){
//                Log.i(TAG,"STATUS($i)=$isOnline")
////            }
//        }
//        notifyItemRangeChanged(0,items.size)

    }

//    fun setMemberOnline(id:Int):Boolean{
////        Log.i(TAG,"memberOnline=$id")
////        Log.i(TAG,"memberOnline=$values")
//        for(i in values.indices){
//            if(values[i].id==id){
//                values[i].online = true
//                return true
//            }
//        }
//        return false
//    }

    fun checkItemOnline(item:GroupMember, l:List<ChatUser>):Boolean{
        for( i in l){
            if( item.id==i.userId)
                return true
        }
        return false

    }


}