package audiolib;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * AudioTrack operation that allows to trim/cut audio represented by AudioTrack object
 */
public class AudioTrimmer extends AudioTrackOperation{
    private final int ONEMILISECONDINBYTES = 176;
    private int from;
    private int to;

    /**
     * parameters applied to operation
     * @param from begin pointer for cut in ms
     * @param to end pointer for cut in ms
     */
    public void setParameters(int from, int to){
        if (from < 0 || to > audioTrack.getDuration() || to <= from) throw new IllegalArgumentException();
        this.from = from * ONEMILISECONDINBYTES;
        this.to = to * ONEMILISECONDINBYTES;
    }
    @Override
    public AudioTrack apply() throws IOException, LineUnavailableException {
        AudioInputStream ais = audioTrack.getAudioInputStream();

        byte[] buffer = new byte[to-from];
        ais.skipNBytes(from);
        ais.read(buffer,0,to-from);
        return new AudioTrack(new AudioInputStream(new ByteArrayInputStream(buffer),ais.getFormat(),ais.getFrameLength()));
    }
}
