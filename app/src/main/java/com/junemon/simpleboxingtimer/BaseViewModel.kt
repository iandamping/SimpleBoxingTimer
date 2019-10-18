package com.junemon.simpleboxingtimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
abstract class BaseViewModel:ViewModel() {
    protected var vmScope = viewModelScope
}