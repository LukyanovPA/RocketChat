package com.pavellukyanov.rocketchat.presentation.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavellukyanov.rocketchat.utils.SameItem

abstract class BaseAdapter<D : Any> : RecyclerView.Adapter<BaseViewHolder>() {

    open var data: List<D>
        get() = differ.currentList
        set(value) {
            differ.submitList(value.toList())
        }

    private var differCallback: DiffUtil.ItemCallback<D> = object : DiffUtil.ItemCallback<D>() {
        override fun areItemsTheSame(oldItem: D, newItem: D): Boolean {
            if (oldItem is SameItem && newItem is SameItem) {
                return oldItem.isSame(newItem)
            }
            return newItem::class.java.simpleName == oldItem::class.java.simpleName
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: D, newItem: D): Boolean {
            return newItem == oldItem
        }
    }

    private val differ: AsyncListDiffer<D> = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(holder, position), getListener(holder, position))
    }

    override fun getItemCount() = data.size

    open fun getItem(holder: BaseViewHolder, position: Int): Any = data[position]

    abstract fun getListener(holder: BaseViewHolder, position: Int): BaseAdapterListener?

    interface BaseAdapterListener
}