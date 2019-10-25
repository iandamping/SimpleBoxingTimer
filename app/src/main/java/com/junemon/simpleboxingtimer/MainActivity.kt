package com.junemon.simpleboxingtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.ian.app.helper.util.checkConnectivityStatus
import com.ian.app.helper.util.fullScreenAnimation
import com.ian.app.helper.util.logE
import com.ian.app.helper.util.startActivity
import com.junemon.simpleboxingtimer.TimerConstant.ONE_SECOND
import com.junemon.simpleboxingtimer.TimerConstant.setCustomMinutes
import com.junemon.simpleboxingtimer.TimerConstant.setCustomSeconds
import com.junemon.simpleboxingtimer.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
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
            inflateAdsView(this)
        }
    }

    private fun initData(binding: ActivityMainBinding) {
        binding.apply {
            vm.roundTimeValue.observe(this@MainActivity, Observer { roundTimeResult ->
                vm.restTimeValue.observe(this@MainActivity, Observer { restTimeResult ->
                    vm.whichRoundValue.observe(this@MainActivity, Observer { roundValueResult ->
                        vm.currentRound.observe(this@MainActivity, Observer { currentRounds ->
                            currentRound = "Round $currentRounds / $roundValueResult"
                            invalidateAll()
                        })
                        btnStart.setOnClickListener {
                            when (roundTimeResult) {
                                0 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomSeconds(30), restTimeResult!!, {
                                        //round time
                                        vm.startTimer(setCustomSeconds(30))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                1 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(1), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(1))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                2 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(2), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(2))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                3 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(3), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(3))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                4 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(4), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(4))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                5 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(5), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(5))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                6 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(6), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(6))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                7 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(7), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(7))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                8 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(8), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(8))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                9 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(9), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(9))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                10 -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomMinutes(10), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomMinutes(10))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                                else -> {
                                    vm.runningDelay(this@MainActivity, roundValueResult, setCustomSeconds(30), restTimeResult, {
                                        //round time
                                        vm.startTimer(setCustomSeconds(30))
                                    }, {
                                        //rest time
                                        vm.startRestTimer(restTimeResult)
                                    })
                                }
                            }

                        }
                    })
                })
            })

        }

        vm.warningValue.observe(this@MainActivity, Observer {
            warningValue = it
        })
    }

    private fun initView(binding: ActivityMainBinding) {
        binding.apply {
            initNumberPicker(this)
            initRadioButton(this)
            initTimer(this)

            btnReset.setOnClickListener {
                startActivity<MainActivity>()
                finish()
            }
        }
    }

    private fun initTimer(binding: ActivityMainBinding) {
        binding.apply {
            vm.isTimerRunning.observe(this@MainActivity, Observer { isRunning ->
                disableView(isRunning)
                vm.currentTime.observe(this@MainActivity, Observer { timeTicking ->
                    val value = DateUtils.formatElapsedTime(timeTicking)
                    if (warningValue != null) {
                        if (value == "00:$warningValue") {
                            vm.warningBellSound(this@MainActivity)
                        }
                    }
                    timerSet = value
                    invalidateAll()
                })

                vm.currentRestTime.observe(this@MainActivity, Observer { restTimeTicking ->
                    val value = DateUtils.formatElapsedTime(restTimeTicking)
                    when {
                        value == "00:00" -> {
                            timerSet = null
                            isRest = false
                        }
                        value != "00:00" -> {
                            isRest = true
                            timerSet = value
                        }
                        else -> {
                            timerSet = null
                            isRest = false
                        }
                    }
                    invalidateAll()
                })
            })
        }
    }

    private fun ActivityMainBinding.disableView(isRunning: Boolean) {
        btnStart.isEnabled = !isRunning
        radioOff.isEnabled = !isRunning
        radioTenSec.isEnabled = !isRunning
        radioThirtySec.isEnabled = !isRunning
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
                vm.setWhichRound(value + 1)
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

    private fun inflateAdsView(binding: ActivityMainBinding) {
        binding.apply {
            this@MainActivity.checkConnectivityStatus {
                if (it) {
                    MobileAds.initialize(this@MainActivity) {}
                    val request = AdRequest.Builder().build()
                    detailAdView.loadAd(request)
                }
            }

        }

    }
}
