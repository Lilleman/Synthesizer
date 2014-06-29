package com.lilleman.synthesizer;

/**
 * Created by Christoffer on 2014-06-28.
 */
public class Audio {
    private int waveform = AudioWaveform.PULSE;
    private float frequency = 100.0f;
    private float amplitude = 0.5f;
    private float duration = 0.2f;
    private float release = 0.2f;

    private AudioModulator frequencyModulator;
    private AudioModulator amplitudeModulator;

    public float position = 0.0f;
    public boolean playing = false;
    public boolean releasing = false;
    public float[] samples;

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
        frequency = value < 1.0f ? 1.0f : value > 14080.0f ? 14080.0f : value;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float value) {
        amplitude = value < 0.0f ? 0.0f : value > 1.0f ? 1.0f : value;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float value) {
        duration = value < 0.0f ? 0.0f : value > 60.0f ? 60.0f : value;
    }

    public float getRelease() {
        return release;
    }

    public void setRelease(float value) {
        release = value < 0.0f ? 0.0f : value > 10.0f ? 10.0f : value;
    }

    public AudioModulator getFrequencyModulator() {
        return frequencyModulator;
    }

    public void setFrequencyModulator(AudioModulator value) {
        this.frequencyModulator = value;
    }

    public AudioModulator getAmplitudeModulator() {
        return amplitudeModulator;
    }

    public void setAmplitudeModulator(AudioModulator value) {
        this.amplitudeModulator = value;
    }
}
