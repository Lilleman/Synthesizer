package com.lilleman.synthesizer;

/**
 * Created by Christoffer on 2014-06-29.
 */
public class AudioDelay extends AudioProcessor {
    private float[] buffer;
    private int bufferSize = 0;
    private int bufferIndex = 0;
    private float time = 0.0f;
    private float gain = 0.8f;

    public AudioDelay() {
        this(0.5f);
    }

    public AudioDelay(float time) {
        this.setTime(time);
    }

    @Override
    public void process(float[] samples) {
        for (int i = 0; i < samples.length; i++) {
            float v = buffer[bufferIndex];
            v *= gain;
            v += samples[i];

            buffer[bufferIndex] = v;
            bufferIndex++;

            if (bufferIndex == bufferSize) {
                bufferIndex = 0;
            }

            samples[i] = v;
            i++;
        }
    }

    public float getTime() {
        return time;
    }

    public void setTime(float value) {
        value = value < 0.0001f ? 0.0001f : value > 8.0f ? 8.0f : value;

        if (time == value) {
            return;
        }

        time = value;

        bufferSize = ((int) (Math.floor(44100 * time)));
        buffer = new float[bufferSize];
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float value) {
        gain = value < 0.0f ? 0.0f : value > 1.0f ? 1.0f : value;
    }
}
