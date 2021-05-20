package com.junemon.simpleboxingtimer.util

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.junemon.simpleboxingtimer.R

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

@BindingAdapter("bindingTimerHelper")
fun setTimerHelper(view:TextView, data:String?){
    when {
        data!=null -> {
            view.visibility = View.VISIBLE
        }
        data == "00:00" -> {
            view.visibility = View.GONE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("handleBackgroundHelper")
fun handleBackground(view:LinearLayout, restState:Boolean){
    if (restState){
        view.setBackgroundResource(R.drawable.bg_rest_numberpicker_dialog)
    }else{
        view.setBackgroundResource(R.drawable.bg_numberpicker_dialog)
    }
}
