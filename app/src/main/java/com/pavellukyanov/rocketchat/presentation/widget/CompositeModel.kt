package com.pavellukyanov.rocketchat.presentation.widget

abstract class CompositeModel : SameItem {
    abstract fun viewType(): Int
    open fun item(): Any = this
    override fun isSame(item: SameItem) = this === item
}
