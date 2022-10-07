package com.pavellukyanov.rocketchat.presentation.widget

import com.pavellukyanov.rocketchat.presentation.base.BaseAdapter

abstract class CompositeAdapter : BaseAdapter<CompositeModel>() {
    override fun getItemViewType(position: Int): Int =
        data[position].viewType()
}