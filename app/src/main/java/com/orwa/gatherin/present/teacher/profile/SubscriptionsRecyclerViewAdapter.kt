package com.orwa.gatherin.present.teacher.profile

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemSubscriptionsListBinding
import com.orwa.gatherin.model.profile.PacksResItem
import com.orwa.gatherin.model.teacher_home.PacakgeInfo
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.FREE_PACK
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_ANNUAL
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_ANNUAL1
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_MONTHLY
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.GOLDEN_PACK_MONTHLY1
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_ANNUAL
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_ANNUAL1
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_MONTHLY
import com.orwa.gatherin.present.teacher.profile.SubscriptionsListFragment.Companion.SILVER_PACK_MONTHLY1
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SubscriptionsRecyclerViewAdapter(
    private var values: List<PacksResItem>,
    private val callBack: (item: PacksResItem) -> Unit,
    private val ctx: Context,
    private val emptyVew: View,
    private val currentPack: PacakgeInfo?
) : RecyclerView.Adapter<SubscriptionsRecyclerViewAdapter.ViewHolder>() {

    private lateinit var binding: ItemSubscriptionsListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemSubscriptionsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                val item = values[bindingAdapterPosition]
                if (!item.canBuy) {
                    Toast.makeText(view.context, R.string.buy_package_failure, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    callBack.invoke(item)
                }
            }
        }

        fun bind(item: PacksResItem) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!item.canBuy && !item.isSubscribed) {//can't buy if value from server is false and already subscribed
                    itemView.foreground =
                        ContextCompat.getDrawable(itemView.context, R.color.inactive_pack_color)
                } else {
                    itemView.foreground = null

                }
            }

            Glide.with(itemView).load(item.image).into(binding.ivMedal)
            binding.tvSubscriptionName.setText(getFormattedPackName(item.packID))
            binding.tvCountSections.setText(getUpToNumSections(item.numberOfDepartment))
            binding.tvCountGroups.setText(getUpToNumGroups(item.numberOfGroup))
            binding.tvPrice.setText(
                ctx.getString(
                    R.string.formatted_pack_price,
                    item.price.toFloat()
                )
            )
            if (currentPack?.packAndroidID == item.packID) {
                binding.tvIsSubscribed.show()
            } else {
                binding.tvIsSubscribed.hide()

            }
        }

    }

    fun swapData(items: List<PacksResItem>) {
        values = items
        if (values.isEmpty()) {
            emptyVew.show()
        } else {
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

    fun getFormattedPackName(packId: String): String {
        val stringId = when (packId) {
            FREE_PACK -> R.string.free_pack
            SILVER_PACK_MONTHLY1 -> R.string.silver_pack_monthly
            SILVER_PACK_ANNUAL1 -> R.string.silver_pack_annual
            GOLDEN_PACK_MONTHLY1 -> R.string.golden_pack_monthly
            GOLDEN_PACK_ANNUAL1 -> R.string.golden_pack_annual
            else -> R.string.pack_undefined

        }
        return ctx.getString(stringId)
    }

    fun getUpToNumSections(num: Int): String {
        return ctx.getString(R.string.up_to_num_sections, num)
    }

    fun getUpToNumGroups(num: Int): String {
        return ctx.getString(R.string.up_to_num_groups, num)
    }


}