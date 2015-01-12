package com.dzharvis.screen.radar;

import com.dzharvis.screen.ScreenReader;
import com.dzharvis.utils.Position;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Recorder extends Thread {

    private static final Logger log = Logger.getLogger(Recorder.class.getName());
    private final ScreenReader reader;
    private volatile boolean recording = false;
    private LinkedList<Position> history = new LinkedList<>();

    public Recorder(ScreenReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (recording) {
                history.add(reader.getCurrentPosition());
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startRecoding(){
        recording = true;
        try {
            playSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(){
        recording = false;
        try {
            playSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public List<Position> getHistory(){
        return history;
    }

    public static void playSound() throws LineUnavailableException {
        byte[] buf = new byte[ 1 ];
        AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();
        for( int i = 0; i < 100 * (float )44100 / 1000; i++ ) {
            double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
            int k = 200;
            buf[ 0 ] = (byte )( Math.sin( angle ) * k );
            sdl.write( buf, 0, 1 );
        }
        sdl.drain();
        sdl.stop();
    }

}
