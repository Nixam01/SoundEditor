package audiolib;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AudioTrimmerTest {

    @Test
    void setParameters() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioTrimmer at = new AudioTrimmer();
        at.setAudioTrack(new AudioTrack(AudioSystem.getAudioInputStream(new File("africa-toto.wav"))));
        assertThrows(IllegalArgumentException.class,()->at.setParameters(300,200));
    }

    @Test
    void apply() {
    }
}