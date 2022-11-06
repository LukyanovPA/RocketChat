package com.pavellukyanov.rocketchat.presentation.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import dagger.android.support.AndroidSupportInjection
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<STATE : Any, EVENT : Any, EFFECT : Any, VB : ViewBinding, VM : BaseViewModel<STATE, EVENT, EFFECT>>(
    private val viewModelClass: Class<VM>
) : BottomSheetDialogFragment() {
    lateinit var binding: VB

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VM>

    protected lateinit var vm: VM

    private lateinit var blurEngine: BlurDialogEngine

    protected val navigator = BaseNavigator(childFragmentManager)

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
        lifecycleScope.launch {
            vm.state.collect(::handleViewState)
        }
        lifecycleScope.launch {
            vm.effect.collect(::effect)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflateViewBinding(inflater, container)
        onBind()
        return binding.root
    }

    private fun handleViewState(state: State<STATE>) {
        when (state) {
            is State.Loading -> {}
            is State.Success -> {
                render(state.state)
            }
            is State.Error -> onError(state.error)
            is State.ErrorMessage -> navigator.showGlobalErrorDialog(state.errorMessage)
        }
    }

    open fun action(event: EVENT) {
        vm.action(event)
    }

    protected open fun effect(effect: EFFECT) {}

    private fun onError(error: Throwable) {
        when (error) {
            is ApiException.UnauthorizedException -> navigator.replace(SignInFragment.newInstance(), SignInFragment.TAG)
            else -> error.message?.let(navigator::showGlobalErrorDialog)
        }
    }

    abstract fun render(state: STATE)

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