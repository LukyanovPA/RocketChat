package com.pavellukyanov.rocketchat.domain.utils

import com.pavellukyanov.rocketchat.domain.entity.State
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart

@OptIn(FlowPreview::class)
internal fun <T> Flow<T>.asState(): Flow<State<T>> =
    this.onStart { flowOf(State.Loading) }
        .flatMapMerge { t ->
            flowOf(State.Success(t))
        }