package com.junemon.simpleboxingtimer.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

data class GenericPair<out A, out B>(val data1: A, val data2: B)

data class GenericTriple<out A, out B, out C>(val data1: A, val data2: B, val data3:C)



fun Fragment.clicks(
    view: View,
    duration: Long = 300,
    isUsingThrottle: Boolean = true,
    onBound: () -> Unit
) {
    if (isUsingThrottle) {
        view.clickListener().throttleFirst(duration).onEach {
            onBound.invoke()
        }.launchIn(this.viewLifecycleOwner.lifecycleScope)
    } else {
        view.setOnClickListener {
            onBound.invoke()
        }
    }

}

private fun View.clickListener(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit).isSuccess
    }
    awaitClose { this@clickListener.setOnClickListener(null) }
}

fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        val mayEmit = currentTime - lastEmissionTime > windowDuration
        if (mayEmit) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}
