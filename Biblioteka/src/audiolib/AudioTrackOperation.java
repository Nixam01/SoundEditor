
package audiolib;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * abstract class as pattern for every AudioTrack operation
 */
public abstract class AudioTrackOperation {
    AudioTrack audioTrack;

    /**
     * choosing an AudioTrack that will be modified
     * @param audioTrack
     */
    public void setAudioTrack(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
    }

    /**
     * Applying modification
     * @return modified AudioTrack object
     * @throws LineUnavailableException if the line cannot be opened due to resource restrictions
     * @throws IOException if an I/O exception occurs during reading of the stream
     */
    public abstract AudioTrack apply() throws LineUnavailableException, IOException;
}
