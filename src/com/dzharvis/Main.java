package com.dzharvis;

import autoitx4java.AutoItX;
import com.dzharvis.behaviour.bg.BGBehaviour;
import com.dzharvis.control.PlayerControls;
import com.dzharvis.graph.Node;
import com.dzharvis.parrot.Parrot;
import com.dzharvis.screen.ScreenReader;
import com.dzharvis.screen.radar.BgMapBuilder;
import com.dzharvis.screen.radar.Recorder;
import com.dzharvis.utils.Utils;
import com.jacob.com.LibraryLoader;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Logger.getLogger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static BgMapBuilder bgrec;


    public static void main(String[] args) throws InterruptedException, IOException, LineUnavailableException, UnsupportedAudioFileException {

        File file = new File("src/jacob-1.18-M2-x64.dll"); //path to the jacob dll
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
        AutoItX x = new AutoItX();
        x.setOption("PixelCoordMode", "2");
        x.setOption("MouseCoordMode", "2");


        ScreenReader reader = new ScreenReader(x);
        PlayerControls controls = new PlayerControls(x);

//        BGBehaviour bg = new BGBehaviour(reader, controls);
//        Recorder r = new Recorder(reader);
        bgrec = new BgMapBuilder(reader);
//        TimeUnit.MILLISECONDS.sleep(2000);
//        r.start();
        Parrot p = new Parrot(reader, controls);

        addHook();
        x.winActivate(WoW.WOW);
        x.winWaitActive(WoW.WOW);
//        bg.start();
        bgrec.start();
        System.out.println("ok");
        while(true){
            TimeUnit.MILLISECONDS.sleep(100);
            if(!reader.isOnBG()) continue;
//            reader.getInstanceID();
//            reader.getFacing();
            Node randomPlayerPos = bgrec.getRandomPlayerPos();
            if(randomPlayerPos == null) {
//                System.out.println("rand null");
                continue;
            }
//            System.out.println("rand not null");
            Node playerPos = bgrec.getCurrentPosition();
            if(playerPos == null) {
//                System.out.println("pl null");
                continue;
            }
            if(playerPos.equals(randomPlayerPos)) continue;
//            System.out.println("pl not null");
            p.play(Utils.findShortest(playerPos, randomPlayerPos));
        }
//        r.startRecoding();
//        TimeUnit.HOURS.sleep(15);
//        r.stopRecording();
//        bgrec.interrupt();
//        Parrot p = new Parrot(reader, controls);
//        p.play(r.getHistory());
//        bg.interrupt();
    }

    private static void addHook() {
        getLogger("").setLevel(Level.OFF);
        Stream.of(Logger.getLogger("").getHandlers()).forEach((h)->{
            h.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return record.getMessage()+"\n";
                }
            });
        });
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.getInstance().addNativeKeyListener(new GlobalKeyListenerExample());

        getLogger(BGBehaviour.class.getPackage().getName()).setLevel(Level.ALL);
        getLogger(Main.class.getPackage().getName()).setLevel(Level.ALL);
        Logger.getLogger(Recorder.class.getName()).setLevel(Level.ALL);
    }

    private static class GlobalKeyListenerExample implements NativeKeyListener {

        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        }
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
            if (nativeKeyEvent.getKeyCode() == 63){
                bgrec.interrupt();
                try {
                    bgrec.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(1);
            }
        }
        public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        }
    }
}
