package com.pavellukyanov.rocketchat.presentation.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pavellukyanov.rocketchat.R
import dagger.android.support.AndroidSupportInjection
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding, VM : BaseViewModel<*>>(
    private val viewModelClass: Class<VM>
) : BottomSheetDialogFragment() {
    lateinit var binding: VB

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    protected lateinit var vm: VM

    private lateinit var blurEngine: BlurDialogEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
        blurEngine = BlurDialogEngine(requireActivity()).apply {
            setDownScaleFactor(2f)
            setBlurRadius(5)
            setBlurActionBar(true)
            setUseRenderScript(true)
        }
        vm = ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflateViewBinding(inflater, container)
        onBind()
        return binding.root
    }

    open fun onBind() {}

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        blurEngine.onResume(retainInstance)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        blurEngine.onDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        blurEngine.onDetach()
    }

    override fun onDestroyView() {
        dialog?.setDismissMessage(null)
        super.onDestroyView()
    }
}