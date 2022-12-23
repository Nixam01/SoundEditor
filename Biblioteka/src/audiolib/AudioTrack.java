
package audiolib;



import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;


/**
 * AudioTrack object represents wave audio file which may be played or modified using other methods in library.
 */
public class AudioTrack {

    private AudioInputStream audioInputStream;
    private Clip clip;
    private State state;
    private final byte[] buffer;

    /**
     *
     * @return state of track at the moment (whether being played, paused or stopped)
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @return duration of the AudioTrack in ms
     */
    public long getDuration() {
        return clip.getMicrosecondLength()/1000;
    }

    /**
     *
     * @return pointer for track in ms
     */
    public long getCurrentTime(){
        return clip.getMicrosecondPosition()/1000;
    }
    public AudioInputStream getAudioInputStream() {
        resetAudioInputStream();
        return audioInputStream;
    }

    /**
     *
     * @param audioInputStream AudioInputStream representing wav audio file. May be received from file by AudioSystem.getAudioInputStream(File file)
     * @throws LineUnavailableException if the line cannot be opened due to resource restrictions
     * @throws IOException if an I/O exception occurs during reading of the stream
     */
    public AudioTrack(AudioInputStream audioInputStream) throws LineUnavailableException, IOException {
        this.audioInputStream = audioInputStream;
        buffer = audioInputStream.readAllBytes();
        reset();
        state = State.STOP;
    }

    /**
     * plays an AudioTrack for user
     * @throws StateException if track in state: play
     */
    public void play() throws StateException {
        if (state == State.PLAY) throw new StateException("Track is already being played");
        clip.start();
        state = State.PLAY;
    }

    /**
     * pauses an AudioTrack for user
     * @throws StateException if track already in state: pause
     */
    public void pause() throws StateException {
        if (state == State.PAUSE) throw new StateException("Track is already paused");
        clip.stop();
        state = State.PAUSE;
    }

    /**
     * stops an AudioTrack for user
     * @throws LineUnavailableException if the line cannot be opened due to resource restrictions
     * @throws IOException if an I/O exception occurs during reading of the stream
     * @throws StateException if track already in state: stop
     */
    public void stop() throws LineUnavailableException, IOException, StateException {
        if (state == State.STOP) throw new StateException("Track is already stopped");
        clip.stop();
        jump(0);
        state = State.STOP;
    }

    /**
     * sets AudioTrack position to the specified moment
     * @param time pointer in ms
     * @throws LineUnavailableException if the line cannot be opened due to resource restrictions
     * @throws IOException if an I/O exception occurs during reading of the stream
     */
    public void jump(long time) throws LineUnavailableException, IOException {
        if (time<0 || time > clip.getMicrosecondLength()/1000) throw new IllegalArgumentException();
        clip.stop();
        clip.close();
        reset();
        clip.setMicrosecondPosition(time);
        state = State.PAUSE;
    }

    /**
     * closes the whole track, makes it unavailable to use later
     * @throws IOException if an I/O exception occurs during reading of the stream
     */
    public void close() throws IOException {
        audioInputStream.close();
        clip.close();
    }

    private void reset() throws LineUnavailableException, IOException {
        resetAudioInputStream();
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }
    private void resetAudioInputStream(){
        audioInputStream = new AudioInputStream(new ByteArrayInputStream(buffer),audioInputStream.getFormat(),audioInputStream.getFrameLength());
    }

    /**
     * representing three possible states of AudioTrack
     */
    enum State{PLAY,PAUSE,STOP}

    /**
     * exception occurs when trying to reuse some method connected with playing audio
     */
    class StateException extends Exception{
        public StateException() {}
        public StateException(String message){
            super(message);
        }
    }

    /**
     *
     * @param fileName name of file where audio will be saved with extension, f.e. abc.wav
     * @throws IOException if an I/O exception occurs during reading of the stream
     */

    public void save(String fileName) throws IOException {

        resetAudioInputStream();

        try{
            if(fileName.split("\\.")[1].equals("mp3")){
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,new File("temp.wav"));
                Converter converter = new Converter();
                converter.wavToMp3(new File("temp.wav"), new File(fileName));
                File fl = new File("temp.wav");
                fl.delete();
            } else if(fileName.split("\\.")[1].equals("wav")){
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,new File(fileName));
            } else {
                throw new InvalidPropertiesFormatException("Wrong format");
            }
        } catch (IOException ex) {
            throw new IOException("Stream not found!");
        }
    }

    private static void main() {
        AudioInputStream a1 = new AudioInputStream("africa-toto.mp3");
    }
}
