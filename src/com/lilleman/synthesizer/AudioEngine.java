package com.lilleman.synthesizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoffer on 2014-06-28.
 */
public class AudioEngine extends Thread {
    private AudioDevice ad;
    private boolean running = true;
    private long start = 0;

    public static final float[] PULSE = new float[44100];
    public static final float[] SAWTOOTH = new float[44100];
    public static final float[] SINE = new float[44100];
    public static final float[] TRIANGLE = new float[44100];

    private static final int BUFFER_SIZE = 2048;
    private static final float SAMPLE_TIME = 1.0f / 44100.0f;

    private float position = 0.0f;
    private float amplitude = 0.5f;
    private List<Audio> audioList = new ArrayList<Audio>();
    private float[] sampleList = new float[BUFFER_SIZE];
    private List<AudioProcessor> processorList = new ArrayList<AudioProcessor>();

    public AudioEngine() {
        for (int i = 0; i < 44100; i++) {
            float p = i / 44100.0f;
            SINE[i] = (float) Math.sin(Math.PI * 2.0f * p);
            PULSE[i] = p < 0.5f ? 1.0f : -1.0f;
            SAWTOOTH[i] = p < 0.5f ? p * 2.0f : p * 2.0f - 2.0f;
            TRIANGLE[i] = p < 0.25f ? p * 4.0f : p < 0.75f ? 2.0f - p * 4.0f : p * 4.0f - 4.0f;
        }
        ad = Gdx.audio.newAudioDevice(44100, false);
        start = System.currentTimeMillis();
    }

    public void play(Audio audio) {
        if (audio.playing == false) {
            audioList.add(audio);
        }

        long pos = System.currentTimeMillis() - start;
        audio.position = position - (pos * 0.001f);
        audio.playing = true;
        audio.releasing = false;
    }

    public void stop(Audio audio) {
        stop(audio, true);
    }

    public void stop(Audio audio, boolean allowRelease) {
        if (audio.playing == false) {
            return;
        }
        if (allowRelease) {
            audio.position = audio.getDuration();
            audio.releasing = true;
            return;
        }
        audio.playing = false;
        audio.releasing = false;
    }

    public void stopAll() {
        stopAll(true);
    }

    public void stopAll(boolean allowRelease) {
        if (allowRelease) {
            for (int i = 0; i < audioList.size(); i++) {
                Audio audio = audioList.get(i);
                audio.position = audio.getDuration();
                audio.releasing = true;
            }
            return;
        }
        for (int i = 0; i < audioList.size(); i++) {
            Audio audio = audioList.get(i);
            audio.playing = false;
            audio.releasing = false;
        }
    }

    @Override
    public void run() {
        while (running) {
            onSampleData();
        }
    }

    private void onSampleData() {
        float s;
        float[] f = new float[BUFFER_SIZE * 2];

        generateSamples();
        processSamples();

        for (int i = 0; i < BUFFER_SIZE; i++) {
            s = sampleList[i] * amplitude;
            f[i * 2] = s;
            f[i * 2 + 1] = s;
            sampleList[i] = 0;
        }

        ad.writeSamples(f, 0, f.length);

        long pos = System.currentTimeMillis() - start;
        position = pos * 0.001f;
    }

    private void generateSamples() {
        for (int i = 0; i < audioList.size(); i++) {
            Audio o = audioList.get(i);

            if (o.playing == false) {
                audioList.remove(o);
                continue;
            }

            for (int j = 0; j < BUFFER_SIZE; j++) {
                if (o.position < 0.0f) {
                    o.position += SAMPLE_TIME;
                    continue;
                }
                if (o.position >= o.getDuration()) {
                    if (o.position >= o.getDuration() + o.getRelease()) {
                        o.playing = false;
                        continue;
                    }
                    o.releasing = true;
                }
                float f = o.getFrequency();
                float a = o.getAmplitude();

                if (o.getFrequencyModulator() != null) {
                    f += o.getFrequencyModulator().process(o.position);
                }

                if (o.getAmplitudeModulator() != null) {
                    a += o.getAmplitudeModulator().process(o.position);
                }

                int p = (int) ((44100 * f * o.position) % 44100);

                float s = o.samples[p];

                if (o.releasing) {
                    s *= 1.0 - ((o.position - o.getDuration()) / o.getRelease());
                }

                sampleList[j] += s * a;

                o.position += SAMPLE_TIME;
            }
        }
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float value) {
        amplitude = value < 0.0f ? 0.0f : value > 1.0f ? 1.0f : value;
    }

    public void stopPlaying() {
        running = false;
    }

    public void addProcessor(AudioProcessor processor) {
        if (processorList.indexOf(processor) == -1) {
            processorList.add(processor);
        }
    }

    public void removeProcessor(AudioProcessor processor) {
        processorList.remove(processor);
    }

    private void processSamples() {
        for (int i = 0; i < processorList.size(); i++) {
            if (processorList.get(i).enabled) {
                processorList.get(i).process(sampleList);
            }
        }
    }
}
