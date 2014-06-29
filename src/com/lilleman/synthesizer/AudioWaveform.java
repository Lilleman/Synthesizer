package com.lilleman.synthesizer;

/**
 * Created by Christoffer on 2014-06-28.
 */
public class AudioWaveform {
    public static final int PULSE = 0;
    public static final int SAWTOOTH = 1;
    public static final int SINE = 2;
    public static final int TRIANGLE = 3;

    public static boolean isValid(int waveform) {
        if (waveform == PULSE) return true;
        if (waveform == SAWTOOTH) return true;
        if (waveform == SINE) return true;
        if (waveform == TRIANGLE) return true;

        return false;
    }
}
