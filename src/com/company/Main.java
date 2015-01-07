package com.company;

import autoitx4java.AutoItX;
import com.company.bg.BGBehaviour;
import com.jacob.com.LibraryLoader;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        File file = new File("src/jacob-1.18-M2-x64.dll"); //path to the jacob dll
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
        AutoItX x = new AutoItX();
        x.setOption("PixelCoordMode", "2");
        x.setOption("MouseCoordMode", "2");


        String wow = "World of Warcraft";
        x.winActivate(wow);
        x.winWaitActive(wow);


        BGBehaviour bg = new BGBehaviour(x);
        addHook();
        bg.start();
        TimeUnit.HOURS.sleep(48);
        bg.interrupt();
    }

    private static void addHook() {
        getLogger("").setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.getInstance().addNativeKeyListener(new GlobalKeyListenerExample());

        getLogger(BGBehaviour.class.getPackage().getName()).setLevel(Level.ALL);
    }

    private static class GlobalKeyListenerExample implements NativeKeyListener {

        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        }
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
            if (nativeKeyEvent.getKeyCode() == 63)
                System.exit(1);
        }
        public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        }
    }
}
