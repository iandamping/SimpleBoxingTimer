package com.junemon.simpleboxingtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ian.app.helper.util.logE
import com.junemon.simpleboxingtimer.TimerConstant.setCustomMinutes
import com.junemon.simpleboxingtimer.TimerConstant.setCustomSeconds
import com.junemon.simpleboxingtimer.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val vm: MainViewmodel by viewModel()
    private val listRestTime by lazy { resources.getStringArray(R.array.rest_time) }
    private val listRoundTime by lazy { resources.getStringArray(R.array.round_time) }
    private val listWhichRound by lazy { resources.getStringArray(R.array.which_round) }

    private var warningValue: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            initData(this)
            initView(this)
        }
    }

    private fun initData(binding: ActivityMainBinding) {
        vm.getNumberPickerData().observe(this@MainActivity, Observer { data ->
            binding.apply {
                btnStart.setOnClickListener {
                    when (data.second) {
                        0 -> vm.startTimer(setCustomSeconds(30))
                        1 -> vm.startTimer(setCustomMinutes(1))
                        2 -> vm.startTimer(setCustomMinutes(2))
                        3 -> vm.startTimer(setCustomMinutes(3))
                        4 -> vm.startTimer(setCustomMinutes(4))
                        5 -> vm.startTimer(setCustomMinutes(5))
                        6 -> vm.startTimer(setCustomMinutes(6))
                        7 -> vm.startTimer(setCustomMinutes(7))
                        8 -> vm.startTimer(setCustomMinutes(8))
                        9 -> vm.startTimer(setCustomMinutes(9))
                        10 -> vm.startTimer(setCustomMinutes(10))
                        else -> vm.startTimer(setCustomSeconds(30))
                    }
                }
            }
        })

        vm.warningValue.observe(this@MainActivity, Observer {
            warningValue = it
        })
    }

    private fun initView(binding: ActivityMainBinding) {
        binding.apply {
            initNumberPicker(this)
            initRadioButton(this)
            initTimer(this)


        }
    }

    private fun initTimer(binding: ActivityMainBinding) {
        binding.apply {
            vm.currentTime.observe(this@MainActivity, Observer {
                val value = DateUtils.formatElapsedTime(it)
                timerSet = if (value == "00:00"){
                    null
                }else
                    value
                invalidateAll()
                btnStart.setOnClickListener { vm.cancelTimer() }
            })
        }
    }


    private fun initNumberPicker(binding: ActivityMainBinding) {
        binding.apply {
            npRestTime.apply {
                minValue = 0
                maxValue = listRestTime.size - 1
                displayedValues = listRestTime
                vm.setRestTime(value)
                setOnValueChangedListener { _, _, newVal ->
                    vm.setRestTime(newVal)
                }

            }
            npRoundTime.apply {
                minValue = 0
                maxValue = listRoundTime.size - 1
                displayedValues = listRoundTime
                vm.setRoundTime(value)
                setOnValueChangedListener { _, _, newVal ->
                    vm.setRoundTime(newVal)
                }
            }
            npWhichRound.apply {
                minValue = 0
                maxValue = listWhichRound.size - 1
                displayedValues = listWhichRound
                vm.setWhichRound(value)
                setOnValueChangedListener { _, _, newVal ->
                    vm.setWhichRound(newVal)
                }
            }
        }
    }

    private fun initRadioButton(binding: ActivityMainBinding) {
        binding.apply {
            radioGroupRoot.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioOff -> vm.setWarningValue(0)
                    R.id.radioTenSec -> vm.setWarningValue(10)
                    R.id.radioThirtySec -> vm.setWarningValue(30)
                }
            }
        }
    }


}
