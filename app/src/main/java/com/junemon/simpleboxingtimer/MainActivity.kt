package com.junemon.simpleboxingtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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
                        0 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomSeconds(30), data.first!!, {
                                //round time
                                vm.startTimer(setCustomSeconds(30))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }

                        1 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(1), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(1))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        2 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(2), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(2))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        3 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(3), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(3))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        4 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(4), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(4))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        5 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(5), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(5))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        6 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(6), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(6))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        7 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(7), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(7))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        8 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(8), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(8))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        9 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(9), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(9))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        10 -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomMinutes(10), data.first!!, {
                                //round time
                                vm.startTimer(setCustomMinutes(10))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
                        else -> {
                            vm.runningDelay(this@MainActivity,data.third!!, setCustomSeconds(30), data.first!!, {
                                //round time
                                vm.startTimer(setCustomSeconds(30))
                            }, {
                                //rest time
                                vm.startTimer(data.first!!)
                            })
                        }
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
                timerSet = if (value == "00:00") {
                    null
                } else
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
                if (value == 0) {
                    vm.setRestTime(setCustomSeconds(0))
                }
                setOnValueChangedListener { _, _, newVal ->
                    when (newVal) {
                        0 -> vm.setRestTime(setCustomSeconds(0))
                        1 -> vm.setRestTime(setCustomSeconds(15))
                        2 -> vm.setRestTime(setCustomSeconds(30))
                        3 -> vm.setRestTime(setCustomSeconds(60))
                        4 -> vm.setRestTime(setCustomSeconds(90))
                        5 -> vm.setRestTime(setCustomSeconds(120))
                        6 -> vm.setRestTime(setCustomSeconds(150))
                        7 -> vm.setRestTime(setCustomSeconds(180))
                    }
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
                    vm.setWhichRound(newVal + 1)
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
