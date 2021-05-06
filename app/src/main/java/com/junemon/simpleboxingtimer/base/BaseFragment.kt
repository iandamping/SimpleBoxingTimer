package com.junemon.simpleboxingtimer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by Ian Damping on 30,July,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
abstract class BaseFragment<out VB : ViewDataBinding> : Fragment() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    abstract fun viewCreated()

    abstract fun activityCreated()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated()
        activityCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun onFailGetValue(e: Exception) {
        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
    }
}