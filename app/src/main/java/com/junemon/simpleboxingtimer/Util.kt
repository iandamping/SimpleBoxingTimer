package com.junemon.simpleboxingtimer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Created by Ian Damping on 25,October,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */

inline fun <reified T : Activity> FragmentActivity.startActivity(
    options: Bundle? = null, noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Activity> Context.startActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(ctx: Context): Intent = Intent(ctx, T::class.java)

