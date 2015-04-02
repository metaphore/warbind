package com.metaphore.war3keybinder;

import com.esotericsoftware.tablelayout.swing.Table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame implements KeyBinds.Listener {
    private final KeyBinds keyBinds;
    private final ActionEmulator actionEmulator;

    private BufferedImage iconOn;
    private BufferedImage iconOff;
    private TrayIcon trayIcon;
    private JLabel txtLabel;
    private SystemTray sysTray;

    public MainFrame() throws HeadlessException, AWTException {
        super("Warbind");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        initializeComponents();
        setUpTray();

        actionEmulator = new ActionEmulator();
        keyBinds = new KeyBinds(actionEmulator);
        keyBinds.setListener(this);
    }

    @Override
    public void onKeyBindsEnabledChanged(boolean enabled) {
        txtLabel.setText(enabled ? "enabled" : "disabled");
        txtLabel.setForeground(enabled ? new Color(0.15f, 0.65f, 0.15f) : new Color(0.75f, 0.25f, 0.25f));
        setIconImage(enabled ? iconOn : iconOff);
        trayIcon.setImage(enabled ? iconOn : iconOff);
    }

    private void initializeComponents() {
        try {
            iconOn = ImageIO.read(new File("images/icon_on.png"));
            iconOff = ImageIO.read(new File("images/icon_off.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtLabel = new JLabel("disabled");
        txtLabel.setFont(new Font(null, Font.PLAIN, 23));

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> exit());
        JButton btnSwitch = new JButton("Turn on/off");
        btnSwitch.addActionListener(e -> {
            keyBinds.switchBindings();
        });

        Table rootTable = new Table();
        rootTable.addCell(txtLabel);
        rootTable.row();
        rootTable.addCell(btnSwitch).width(220);
//        rootTable.row();
//        rootTable.addCell(btnClose).width(120);

        // FRAME PARAMS
        {
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setContentPane(rootTable);
            setLocationByPlatform(true);
            setResizable(false);
            setIconImage(iconOff);
//            setUndecorated(true);
            pack();
            setVisible(true);
        }
    }

    private void setUpTray() {
        sysTray = SystemTray.getSystemTray();

        PopupMenu popup = new PopupMenu();
        MenuItem itemTurnOnOff = new MenuItem("Turn on/off");
        itemTurnOnOff.addActionListener(e -> keyBinds.switchBindings());
        popup.add(itemTurnOnOff);
        MenuItem itemExit = new MenuItem("Exit");
        itemExit.addActionListener(e -> exit());
        popup.add(itemExit);

        trayIcon = new TrayIcon(iconOff, "Warbind", popup);
        trayIcon.addActionListener(e -> keyBinds.switchBindings());
        try {
            sysTray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
    }

    public void exit() {
        MainFrame frame = MainFrame.this;
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
}