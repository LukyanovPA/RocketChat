package com.pavellukyanov.rocketchat.presentation.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder(open val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    open fun bind(item: Any, listener: BaseAdapter.BaseAdapterListener?) {}
}