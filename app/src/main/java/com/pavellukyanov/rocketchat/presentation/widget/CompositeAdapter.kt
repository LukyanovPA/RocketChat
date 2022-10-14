package com.pavellukyanov.rocketchat.presentation.widget

import com.pavellukyanov.rocketchat.presentation.base.BaseAdapter

abstract class CompositeAdapter<T : CompositeModel>(
    baseAdapterListener: BaseAdapterListener<T>?
) : BaseAdapter<T>(baseAdapterListener) {
    override fun getItemViewType(position: Int): Int =
        data[position].viewType()
}