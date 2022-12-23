package audiolib;



import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.SequenceInputStream;

public class AudioConcatenator extends AudioTrackOperation{
    private AudioTrack appendTrack;

    /**
     * parameters applied to operation
     * @param appendTrack AudioTrack that will be appended to the base track
     */
    public void setParameters(AudioTrack appendTrack){
        this.appendTrack = appendTrack;
    }
    @Override
    public AudioTrack apply() throws LineUnavailableException, IOException {
        AudioInputStream ais1 = audioTrack.getAudioInputStream();
        AudioInputStream ais2 = appendTrack.getAudioInputStream();
        return new AudioTrack(new AudioInputStream(new SequenceInputStream(ais1, ais2),ais1.getFormat(),ais1.getFrameLength()+ais2.getFrameLength()));
    }
}
