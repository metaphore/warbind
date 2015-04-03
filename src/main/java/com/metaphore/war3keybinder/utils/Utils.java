package com.metaphore.war3keybinder.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static File copyToTempFile(String resourcePath) {
        File temp;
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            byte[] buffer = new byte[1024];
            int read;
            temp = File.createTempFile(resourcePath, "");
            FileOutputStream fos = new FileOutputStream(temp);

            while((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO close on finally

        return temp;
    }
}
