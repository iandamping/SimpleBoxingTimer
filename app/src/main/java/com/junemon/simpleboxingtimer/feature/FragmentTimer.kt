package com.junemon.simpleboxingtimer.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.junemon.simpleboxingtimer.MainViewmodel
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.base.BaseFragment
import com.junemon.simpleboxingtimer.databinding.FragmentTimerBinding
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_INTEGER_VALUE
import com.junemon.simpleboxingtimer.util.clicks
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Created by Ian Damping on 06,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
@AndroidEntryPoint
class FragmentTimer : BaseFragment<FragmentTimerBinding>() {

    private val vm: MainViewmodel by viewModels()

    private val listRestTime by lazy { resources.getStringArray(R.array.rest_time) }
    private val listRoundTime by lazy { resources.getStringArray(R.array.round_time) }
    private val listWhichRound by lazy { resources.getStringArray(R.array.which_round) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTimerBinding
        get() = FragmentTimerBinding::inflate

    override fun viewCreated() {
        binding.apply {
            lifecycleOwner = this@FragmentTimer.viewLifecycleOwner
            mainViewModel = vm
        }.initView()
    }



    private fun FragmentTimerBinding.initView() {
        initNumberPicker()
        getNumberPicker()
        initRadioButton()
        inflateAdsView()

        clicks(btnReset) {
            vm.resetAll()
        }

        clicks(btnStart) {
            vm.startCounting()
        }
        clicks(btnStop) {
            vm.cancelAllTimer()
            if (btnStop.isVisible) {
                btnStop.visibility = View.GONE
                btnStart.visibility = View.VISIBLE
            }
        }
    }

    private fun FragmentTimerBinding.initNumberPicker() {
        npRestTime.apply {
            minValue = DEFAULT_INTEGER_VALUE
            maxValue = listRestTime.size - 1
            displayedValues = listRestTime
        }

        npRoundTime.apply {
            minValue = DEFAULT_INTEGER_VALUE
            maxValue = listRoundTime.size - 1
            displayedValues = listRoundTime
        }
        npWhichRound.apply {
            minValue = DEFAULT_INTEGER_VALUE
            maxValue = listWhichRound.size - 1
            displayedValues = listWhichRound
        }
    }

    private fun FragmentTimerBinding.getNumberPicker() {
        with(npRestTime) {
            if (value == DEFAULT_INTEGER_VALUE) {
                vm.setRestTime(TimerConstant.setCustomTime(0))
            }
            setOnValueChangedListener { _, _, newVal ->
                when (newVal) {
                    0 -> vm.setRestTime(TimerConstant.setCustomTime(0))
                    1 -> vm.setRestTime(TimerConstant.setCustomTime(15))
                    2 -> vm.setRestTime(TimerConstant.setCustomTime(30))
                    3 -> vm.setRestTime(TimerConstant.setCustomTime(60))
                    4 -> vm.setRestTime(TimerConstant.setCustomTime(90))
                    5 -> vm.setRestTime(TimerConstant.setCustomTime(120))
                    6 -> vm.setRestTime(TimerConstant.setCustomTime(150))
                    7 -> vm.setRestTime(TimerConstant.setCustomTime(180))
                }
            }
        }

        with(npRoundTime) {
            vm.setRoundTime(value)
            setOnValueChangedListener { _, _, newVal ->
                vm.setRoundTime(newVal)
            }
        }

        with(npWhichRound) {
            vm.setWhichRound(value + 1)
            setOnValueChangedListener { _, _, newVal ->
                vm.setWhichRound(newVal + 1)
            }
        }
    }

    private fun FragmentTimerBinding.initRadioButton() {
        radioGroupRoot.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioOff -> vm.setWarningValue(0)
                R.id.radioTenSec -> vm.setWarningValue(10)
                R.id.radioThirtySec -> vm.setWarningValue(30)
            }
        }
    }

    private fun FragmentTimerBinding.inflateAdsView() {
        MobileAds.initialize(requireContext()) {
            Timber.e("init ads!")
        }
        val request = AdRequest.Builder().build()
        detailAdView.loadAd(request)
    }


}