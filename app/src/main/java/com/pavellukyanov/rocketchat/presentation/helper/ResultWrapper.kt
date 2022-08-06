package com.pavellukyanov.rocketchat.presentation.helper

import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ResultWrapper<I, O>(
    fragment: Fragment? = null,
    activity: AppCompatActivity? = null,
    contract: ActivityResultContract<I, O>
) {

    private var resultHandler: ((o: O) -> Unit)? = null

    private val launcher = fragment?.registerForActivityResult(contract) { result: O ->
        resultHandler?.invoke(result)
    } ?: activity?.registerForActivityResult(contract) { result: O ->
        resultHandler?.invoke(result)
    }

    fun launch(input: I, handler: ((o: O) -> Unit)) {
        resultHandler = handler
        launcher?.launch(input)
    }
}