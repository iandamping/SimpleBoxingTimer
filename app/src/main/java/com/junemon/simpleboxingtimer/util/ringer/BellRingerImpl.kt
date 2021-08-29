package com.junemon.simpleboxingtimer.util.ringer

import android.content.Context
import android.media.MediaPlayer
import com.junemon.simpleboxingtimer.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Ian Damping on 29,August,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */

class BellRingerImpl @Inject constructor(@ApplicationContext private val context: Context) : BellRinger {

    private var bellRingerPlayer: MediaPlayer? = null

    override fun startBellSound() {
        if (bellRingerPlayer != null) {
            bellRingerPlayer?.stop()
            bellRingerPlayer?.reset()
            bellRingerPlayer?.release()
            bellRingerPlayer = null
        }
        bellRingerPlayer = MediaPlayer.create(context, R.raw.start_round_bell)
        bellRingerPlayer?.start()
    }

    override fun endBellSound() {
        if (bellRingerPlayer != null) {
            bellRingerPlayer?.stop()
            bellRingerPlayer?.reset()
            bellRingerPlayer?.release()
            bellRingerPlayer = null
        }
        bellRingerPlayer = MediaPlayer.create(context, R.raw.end_round_bell)
        bellRingerPlayer?.start()
    }

    override fun warningBellSound() {
        if (bellRingerPlayer != null) {
            bellRingerPlayer?.stop()
            bellRingerPlayer?.reset()
            bellRingerPlayer?.release()
            bellRingerPlayer = null
        }
        bellRingerPlayer = MediaPlayer.create(context, R.raw.warning_sound)
        bellRingerPlayer?.start()
    }
}