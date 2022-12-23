package audiolib;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AudioTrackTest {
    AudioTrack track = new AudioTrack(AudioSystem.getAudioInputStream(new File("africa-toto.wav")));

    AudioTrackTest() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
    }


    @org.junit.jupiter.api.Test
    void getDuration() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        assertEquals(297978,track.getDuration());
    }

    @org.junit.jupiter.api.Test
    void getCurrentTime() {
        assertEquals(0 ,track.getCurrentTime());
    }

    @Test
    void getAudioInputStream() {
        assertNotNull(track.getAudioInputStream());
    }

    @org.junit.jupiter.api.Test
    void play() throws AudioTrack.StateException {
        track.play();
        assertEquals(AudioTrack.State.PLAY,track.getState());
    }

    @org.junit.jupiter.api.Test
    void pause() throws AudioTrack.StateException {
        track.pause();
        assertEquals(AudioTrack.State.PAUSE,track.getState());
    }

    @org.junit.jupiter.api.Test
    void stop() throws AudioTrack.StateException, LineUnavailableException, IOException {
        track.play();
        track.stop();
        assertEquals(AudioTrack.State.STOP,track.getState());
    }

    @Test
    void close() throws IOException, AudioTrack.StateException, LineUnavailableException {
        track.play();
        track.stop();
        assertEquals(AudioTrack.State.STOP,track.getState());
    }
    @org.junit.jupiter.api.Test
    void jump() throws LineUnavailableException, IOException {
        track.jump(5000000);
        assertEquals(5000,track.getCurrentTime());
    }
}