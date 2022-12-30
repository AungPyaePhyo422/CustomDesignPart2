package com.example.customdesign.adapter

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

class MedicineItemKeyProvider(
    private val recyclerView: RecyclerView
    ) : ItemKeyProvider<Long>( SCOPE_CACHED
) {

    override fun getKey(position: Int): Long? =
        recyclerView.adapter?.getItemId(position)
    override fun getPosition(key: Long): Int =
        recyclerView.findViewHolderForItemId(key)?.absoluteAdapterPosition ?: RecyclerView.NO_POSITION

}