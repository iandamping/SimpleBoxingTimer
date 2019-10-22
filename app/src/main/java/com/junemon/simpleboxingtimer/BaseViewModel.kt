package com.junemon.simpleboxingtimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
abstract class BaseViewModel:ViewModel() {
    protected var vmScope = viewModelScope
    protected var fetchingJob = Job()
    protected var uiScope = CoroutineScope(Dispatchers.Main + fetchingJob)
}