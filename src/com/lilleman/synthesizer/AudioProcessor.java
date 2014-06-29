package com.lilleman.synthesizer;

/**
 * Created by Christoffer on 2014-06-29.
 */
public abstract class AudioProcessor {
    public boolean enabled = true;

    public abstract void process(float[] samples);
}
