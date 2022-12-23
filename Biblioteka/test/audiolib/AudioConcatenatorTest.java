package audiolib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AudioConcatenatorTest {


    @Test
    void apply() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioTrack at1 = new AudioTrack(AudioSystem.getAudioInputStream(new File("africa-toto.wav")));
        AudioTrack at2 = new AudioTrack(AudioSystem.getAudioInputStream(new File("africa-toto.wav")));
        AudioConcatenator ac = new AudioConcatenator();
        ac.setParameters(at2);
        ac.setAudioTrack(at1);
        assertEquals(2*297978,ac.apply().getDuration(),1);
    }
}