package com.orwa.gatherin.present.student.section

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderGroupContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<PlaceholderItem> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()

    val names = listOf<String>("Group A","Group B", "Group C")
    val details = listOf<Boolean>(false,true,true)

    private val COUNT = 3

    init {
        // Add some sample items.
        for (i in 0..COUNT-1) {
            addItem(createPlaceholderItem(i))
        }
    }

    private fun addItem(item: PlaceholderItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createPlaceholderItem(position: Int): PlaceholderItem {
        return PlaceholderItem(position.toString(), names[position], details[position])
    }


    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val id: String, val name: String, val isLocked: Boolean)
}