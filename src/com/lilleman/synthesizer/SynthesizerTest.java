package com.lilleman.synthesizer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class SynthesizerTest extends ApplicationAdapter {
    Audio audio;
    AudioDelay delay;
    AudioEngine s;

    @Override
    public void create() {
        audio = new Audio();
        delay = new AudioDelay();

        audio.setWaveform(AudioWaveform.SINE);
        audio.setAmplitude(0.4f);
        audio.setDuration(0.0f);
        audio.setRelease(0.5f);

        delay.setTime(0.5f);
        delay.setGain(0.5f);

        s = new AudioEngine();
        s.addProcessor(delay);

        s.start();

        Gdx.input.setInputProcessor(new InputHandler());
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {
        super.dispose();
        s.stopPlaying();
        while (true) {
            try {
                s.join();
                break;
            } catch (Exception e) {
            }
        }
    }

    public class InputHandler extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            audio.setFrequency(200 + 200 * ((float) Math.random()));
            s.play(audio);
            return super.touchDown(screenX, screenY, pointer, button);
        }
    }
}
