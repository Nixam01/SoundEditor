package audiolib;
import it.sauronsoftware.jave.*;
import java.io.File;


public class Converter {

    /** Data structures for the audio
     *  and Encoding attributes
     */
    private final AudioAttributes audioAttr = new AudioAttributes();
    private final EncodingAttributes encoAttrs = new EncodingAttributes();
    private final Encoder encoder = new Encoder();

    /** Default constructor of Converter
     * In default constructor values used for converter are as follows:
     * Bitrate is set as 256000 bps.
     * By default audio is encoded in 2 channels.
     * Default sampling rate is 48000 Hz.
     */
    public Converter(){
        audioAttr.setBitRate(256000);
        audioAttr.setChannels(2);
        audioAttr.setSamplingRate(48000);
    }

    /** Parametrized constructor of Converter
     * Set your own attributes for encoding
     * @param bitrate Bitrate to be used in a new file. For example a common value used in mp3 files is 128000 bps.
     * @param channels Number of channels to be encoded in converter. 1 for mono, 2 for stereo.
     * @param samplingRate Sampling rate to be used in a new file. Most common value is 44100 Hz
     */
    public Converter(Integer bitrate, Integer channels, Integer samplingRate) {
        audioAttr.setBitRate(bitrate);
        audioAttr.setChannels(channels);
        audioAttr.setSamplingRate(samplingRate);
    }

    /** Function to convert audio file to a .wav format in pcm_s16le codec.
     * All basic audio attributes are set in the constructor of converter.
     * @param source Source file that is going to be converted to a new format.
     * @param target Target file where new file will be created.
     */
    public void mp3ToWav(File source, File target){
        encoAttrs.setFormat("wav");
        audioAttr.setCodec("pcm_s16le");
        encoAttrs.setAudioAttributes(audioAttr);
        try{
            encoder.encode(source, target, encoAttrs);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** Function to convert audio file to a .mp3 format in mp2 codec.
     * All basic audio attributes are set in the constructor of converter.
     * @param source Source file that is going to be converted to a new format.
     * @param target Target file where new file will be created.
     */
    public void wavToMp3(File source, File target) {
        encoAttrs.setFormat("mp3");
        audioAttr.setCodec("mp2");
        encoAttrs.setAudioAttributes(audioAttr);
        try{
            encoder.encode(source, target, encoAttrs);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
