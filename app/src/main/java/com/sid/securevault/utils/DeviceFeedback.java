package com.sid.securevault.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

import com.sid.securevault.R;

public class DeviceFeedback {

    private static SoundPool sound;

    public static void clickSound(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sound = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        int soundId = sound.load(context, R.raw.button_sound, 1);
        sound.setOnLoadCompleteListener((_, _, _) -> sound.play(soundId, 0.2f, 0.2f, 0, 0, 1.0f));
    }

    public static void releaseSound() {
        if(sound != null) {
            sound.release();
            sound = null;
        }
    }

    public static void hapticFeedBack(Context context) {
        VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        Vibrator vibrator = vibratorManager.getDefaultVibrator();
        if(vibrator.hasVibrator()) {
            VibrationEffect effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(effect);
        }
    }

}
