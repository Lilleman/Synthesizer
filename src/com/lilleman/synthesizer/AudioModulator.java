package com.lilleman.synthesizer;

/**
 * Created by Christoffer on 2014-06-28.
 */
public class AudioModulator {
    private int waveform = AudioWaveform.SINE;
    private float frequency = 4.0f;
    private float amplitude = 1.0f;
    private float shift = 0.0f;
    private float[] samples;

    public float process(float time) {
        int p = 0;
        float s = 0.0f;

        if (shift != 0.0f) {
            time += (1.0 / frequency) * shift;
        }

        p = (int) ((44100 * frequency * time) % 44100);
        s = samples[p];

        return s * amplitude;
    }

    public int getWaveform() {
        return waveform;
    }

    public void setWaveform(int value) {
        if (AudioWaveform.isValid(value) == false) {
            return;
        }
        switch (value) {
            case AudioWaveform.PULSE:
                samples = AudioEngine.PULSE;
                break;
            case AudioWaveform.SAWTOOTH:
                samples = AudioEngine.SAWTOOTH;
                break;
            case AudioWaveform.SINE:
                samples = AudioEngine.SINE;
                break;
            case AudioWaveform.TRIANGLE:
                samples = AudioEngine.TRIANGLE;
                break;
        }
        waveform = value;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float value) {
        frequency = value < 0.01f ? 0.01f : value > 100.0f ? 100.0f : value;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float value) {
        amplitude = value < 0.0f ? 0.0f : value > 8000.0f ? 8000.0f : value;
    }

    public float getShift() {
        return shift;
    }

    public void setShift(float value) {
        shift = value < 0.0f ? 0.0f : value > 1.0f ? 1.0f : value;
    }
}
