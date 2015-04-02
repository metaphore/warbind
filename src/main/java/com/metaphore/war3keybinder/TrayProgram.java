package com.metaphore.war3keybinder;

import com.metaphore.war3keybinder.utils.Utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TrayProgram implements KeyBinds.Listener {
    private final ActionEmulator actionEmulator;
    private final KeyBinds keyBinds;
    private BufferedImage iconOn;
    private BufferedImage iconOff;
    private TrayIcon trayIcon;

    public TrayProgram() {
        actionEmulator = new ActionEmulator();
        keyBinds = new KeyBinds(actionEmulator);
        keyBinds.setListener(this);

        try {
            iconOn = ImageIO.read(TrayProgram.class.getResource("/images/icon_on.png"));
            iconOff = ImageIO.read(TrayProgram.class.getResource("/images/icon_off.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpTray();

//        keyBinds.switchBindings();
    }

    private void setUpTray() {
        SystemTray sysTray = SystemTray.getSystemTray();

        PopupMenu popup = new PopupMenu();
        MenuItem itemTurnOnOff = new MenuItem("Turn on/off");
        itemTurnOnOff.addActionListener(e -> keyBinds.switchBindings());
        popup.add(itemTurnOnOff);
        MenuItem itemExit = new MenuItem("Exit");
        itemExit.addActionListener(e -> System.exit(-1));
        popup.add(itemExit);

        trayIcon = new TrayIcon(iconOff, "Warbind", popup);
        trayIcon.addActionListener(e -> keyBinds.switchBindings());
        try {
            sysTray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException("Error adding tray icon");
        }
    }

    @Override
    public void onKeyBindsEnabledChanged(boolean enabled) {
        trayIcon.setImage(enabled ? iconOn : iconOff);
        Utils.playSound(enabled ? "on.wav" : "off.wav");
    }

    public static void main(String[] args) {
        new TrayProgram();
    }
}
