package com.junemon.simpleboxingtimer

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

@BindingAdapter("bindingTimerHelper")
fun setTimerHelper(view:TextView, data:String?){
    if (data!=null){
        view.visibility = View.VISIBLE
    } else if (data == "00:00" || data == null){
        view.visibility = View.GONE
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
