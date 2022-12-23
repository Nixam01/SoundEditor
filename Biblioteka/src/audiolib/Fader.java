package audiolib;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
public class Fader {

    /**
     * Function to apply fade effect to the source file.
     */
    public void fadeAudio(String source, String target, int fadeInDuration, int fadeOutDuration) {
        StringBuilder filterRgx = new StringBuilder("afade=t=in:ss=0:d=");
        filterRgx.append(fadeInDuration);
        filterRgx.append(",afade=t=out:st=");
        try {
            //fade recorder
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(target, 2);
            recorder.setAudioOption("crf", "0");
            recorder.setAudioQuality(0);
            //set bit rate
            recorder.setAudioBitrate(192000);
            //set sample
            recorder.setSampleRate(44100);
            //set channel
            recorder.setAudioChannels(2);
            //set encoder
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_PCM_S16LE);
            recorder.start();
            //load sample audio
            FFmpegFrameGrabber grabberOne = FFmpegFrameGrabber.createDefault(source);
            grabberOne.start();
            //make filter
            filterRgx.append(((int) grabberOne.getLengthInTime()/1000000L) - fadeOutDuration);
            filterRgx.append(":d=");
            filterRgx.append(fadeOutDuration);
            //create fade in or out filter
            FFmpegFrameFilter filter = new FFmpegFrameFilter(filterRgx.toString(), grabberOne.getAudioChannels());
            filter.setSampleFormat(grabberOne.getSampleFormat());
            filter.setAudioInputs(1);
            filter.start();
            Frame fOne, recordF;
            while (true) {
                //fetch sound sample
                fOne = grabberOne.grabSamples();
                if (fOne == null)
                    break;
                //put sound sample in fade filter
                if (fOne != null) {
                    filter.push(0, fOne);
                }
            }
            //read sound sample from filter ,and put int fade reocrder , save it as mp3
            while ((recordF = filter.pull()) != null) {
                recorder.record(recordF);
            }
            grabberOne.stop();
            filter.close();
            recorder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
