package com.junemon.simpleboxingtimer.util

import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.junemon.simpleboxingtimer.R

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
@BindingAdapter("bindingButtonStopViewHelper")
fun setButtonStopHelper(view:Button, isRunning: Boolean){
    if (!isRunning){
        if (view.isVisible) {
            view.visibility = View.GONE
        }
    } else view.visibility = View.VISIBLE
}

@BindingAdapter("bindingButtonStartViewHelper")
fun setButtonStartHelper(view:Button, isRunning: Boolean){
    if (!isRunning){
        view.visibility = View.VISIBLE
    } else if (view.isVisible) {
        view.visibility = View.GONE
    }
}

@BindingAdapter("bindingTimerHelper")
fun setTimerHelper(view:TextView, isRunning: Boolean){
    if (isRunning){
        view.visibility = View.VISIBLE
    } else view.visibility = View.GONE
}

@BindingAdapter("bindingLinearlayoutVisibiltyHelper")
fun setLinearlayoutVisibilityHelper(view:LinearLayout, isRunning: Boolean){
    if (isRunning){
        view.visibility = View.GONE
    } else view.visibility = View.VISIBLE
}

@BindingAdapter("bindingRadioButtonHelper")
fun setRadioButtonHelper(view:RadioButton, isRunning: Boolean){
    view.isEnabled = !isRunning
}

@BindingAdapter("bindingRadioGroupHelper")
fun setRadioButtonHelper(view:RadioGroup, isRunning: Boolean){
   if (!isRunning){
       view.clearCheck()
       view.check(R.id.radioOff)
   }
}

@BindingAdapter("bindingNumberPickerResetterHelper")
fun setResetNumberPickerHelper(view:NumberPicker, isRunning: Boolean){
    if (!isRunning){
        view.value = 0
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
