package com.example.menupracticaltestapp.helpers

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

interface ViewBindingHolder<T : ViewBinding> {

    val binding: T

    fun initBinding(lifeCycleOwner: Fragment, binding: T, onBound: (() -> Unit)? = null): View
}

class ViewBindingHolderImpl<T : ViewBinding> : ViewBindingHolder<T>, DefaultLifecycleObserver {

    private var _binding: T? = null

    override val binding: T
        get() = _binding ?: throw IllegalStateException("This property is only valid between onCreateView and onDestroyView")

    override fun initBinding(lifeCycleOwner: Fragment, binding: T, onBound: (() -> Unit)?): View {
        lifeCycleOwner.viewLifecycleOwner.lifecycle.addObserver(this)
        _binding = binding
        onBound?.invoke()
        return binding.root
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        _binding = null
    }
}