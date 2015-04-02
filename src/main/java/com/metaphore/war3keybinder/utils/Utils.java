package com.metaphore.war3keybinder.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Utils {
    public static File get(String path) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }

    public static void playSound(String fileName) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Utils.class.getResource("/sounds/" + fileName));
            clip.open(inputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-5.0f); // Reduce volume by 10 decibels.
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
