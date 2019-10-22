package com.junemon.simpleboxingtimer

import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ian.app.helper.util.gone
import com.ian.app.helper.util.visible


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

@BindingAdapter("bindingTimerHelper")
fun setTimerHelper(view:TextView, data:String?){
    if (data!=null){
        view.visible()
    } else if (data == "00:00" || data == null){
        view.gone()
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
