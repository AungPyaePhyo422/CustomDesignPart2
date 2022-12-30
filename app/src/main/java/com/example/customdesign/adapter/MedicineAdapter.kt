package com.example.customdesign.adapter

import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.customdesign.data.Medicine
import com.example.customdesign.databinding.ItemMedicineListBinding

//You can simply give MedicineAdapter
class MedicineAdapter(private val event: ItemListener) :
    ListAdapter<Medicine, MedicineAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<Medicine>() {
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            // this should be oldTtem.id == newItem.id to compare "areItemsTheSame"
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return oldItem == newItem
        }
    }
    ) {


    init {
        setHasStableIds(true)
    }

    var selectionTracker : SelectionTracker<Long>? = null
    override fun getItemId(position: Int): Long = position.toLong()


    inner class MyViewHolder(
        val binding: ItemMedicineListBinding,
        private val listener: ItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private var hotspot : Rect

        init {

            val rect = Rect()
            hotspot = rect



            binding.btnAddCount.setOnClickListener {
                //You can also see let,also,run,with,..etc useful extension functions.
                getItem(bindingAdapterPosition).apply {
                    listener.onItemCountIncrease(this)
                    //rebinding when clicking every adding and subtracting actions can be solved but there is better solutions.
                    binding.tvMedicineCount.text = this.count.toString()
                }
            }

            binding.btnRemoveCount.setOnClickListener {
                getItem(bindingAdapterPosition).apply {
                    listener.onItemCountDecrease(this)
                    binding.tvMedicineCount.text = this.count.toString()
                }

            }

        }

        //give model name as object name
        fun bind(getCurrentItem: Medicine) {

            binding.apply {
//                if (selectionTracker!!.isSelected(getItemDetails().selectionKey) ){
//                    itemView.setBackgroundColor(Color.BLACK)
//                }
//                else{
//                    itemView.setBackgroundColor(Color.WHITE)
//                }



                binding.checkBox.setOnCheckedChangeListener { v, b ->
                    Log.d("GG", b.toString())
                    if (b) {
                        selectionTracker?.select(adapterPosition.toLong())

                        binding.checkBox.isChecked = true
                    } else {
                        selectionTracker?.deselect(adapterPosition.toLong())

                        binding.checkBox.isChecked = false
                    }
                }

                checkBox.isChecked = selectionTracker!!.isSelected(getItemDetails().selectionKey)

                tvMedicineName.text = getCurrentItem.name
                tvMedicinePrice.text = " ${getCurrentItem.price} MMK"
                tvMedicineCount.text = getCurrentItem.count.toString()
            }
        }

        // new implementation
        // started

        fun getItemDetails() : ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {

                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long = itemId
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            ItemMedicineListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view, event)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

    }

}

//You can simply give ItemListener
interface ItemListener {
    //I would use onItemCountIncrease instead of addCountClickListener .This is not a listener but a method.
    fun onItemCountIncrease(item: Medicine)

    fun onItemCountDecrease(item: Medicine)

    //I don't usually use this kind of usage personally.This is another way.
    fun getIfSelectAll(): Boolean

}