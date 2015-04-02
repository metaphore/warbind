package com.metaphore.war3keybinder;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class KeyBinds implements HotkeyListener, IntellitypeListener {
    private static final int ID_SWITCH = 1;

    private final Map<Integer, Action> actionMap;
    private final List<Integer> boundHotkeys;
    private final ActionEmulator actionEmulator;
    private final JIntellitype intelliType;

    private boolean enabled = false;
    private Listener listener;

    public KeyBinds(ActionEmulator actionEmulator) {
        this.actionEmulator = actionEmulator;
        actionMap = new HashMap<>();
        boundHotkeys = new ArrayList<>(32);
        intelliType = initIntellitype();

        intelliType.addIntellitypeListener(this);
        intelliType.addHotKeyListener(this);
        intelliType.registerHotKey(ID_SWITCH, "F5");

        initActions();
    }

    private JIntellitype initIntellitype() {
        String libName;
        String archProp = System.getProperty("sun.arch.data.model");
        switch (archProp) {
            case "32":
                libName = "JIntellitype.dll";
                break;
            case "64":
                libName = "JIntellitype64.dll";
                break;
            default:
                throw new RuntimeException("Unexpected arch: " + archProp);

        }
        System.out.println("libName = " + libName);

        File temp;
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(libName);
            byte[] buffer = new byte[1024];
            int read;
            temp = File.createTempFile(libName, "");
            FileOutputStream fos = new FileOutputStream(temp);

            while((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//            System.out.println("temp absolute: " + temp.getAbsolutePath());
        JIntellitype.setLibraryLocation(temp.getAbsolutePath());
        return JIntellitype.getInstance();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void initKeyBinds() {
        boundHotkeys.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("keybinds.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                processKeyBind(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setEnabled(true);
    }

    private void processKeyBind(String line) {
        line = line.trim();
        if (line.length() == 0 || line.startsWith("#")) return;   // Comments start with #

        String[] split = StringUtils.split(line, ':');
        if (split.length != 2) {
            System.out.println("Empty keybind or wrong format: " + line);
            return;
        }

        String action = split[0].trim();
        String hotkey = split[1].trim();
        int actionHash = hash(action);

        boundHotkeys.add(actionHash);
        intelliType.registerHotKey(actionHash, hotkey);
    }

    public void discardKeyBinds() {
        Iterator<Integer> iter = boundHotkeys.iterator();
        while (iter.hasNext()) {
            int keyHash = iter.next();
            intelliType.unregisterHotKey(keyHash);
            iter.remove();
        }

        setEnabled(false);
    }

    @Override
    public void onHotKey(int identifier) {
        if (identifier == ID_SWITCH) {
            switchBindings();
            return;
        }

        Action action = actionMap.get(identifier);
        if (action != null) {
            action.run();
        } else {
            System.out.println("WARNING: Unhandled hotkey id: " + identifier);
        }
    }

    @Override
    public void onIntellitype(int command) {
        System.out.println("onIntellitype " + command);
    }

    public void switchBindings() {
        if (enabled) {
            discardKeyBinds();
        } else {
            initKeyBinds();
        }
    }

    private void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;

        this.enabled = enabled;
        if (listener != null) {
            listener.onKeyBindsEnabledChanged(enabled);
        }
    }

    private int hash(String input) {
        int hash=7;
        for (int i=0; i < input.length(); i++) {
            hash = hash*31+input.charAt(i);
        }
        return hash;
    }

    private static class Action implements Runnable {
        private final String key;
        private final Runnable runnable;

        public Action(String key, Runnable runnable) {
            this.key = key;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
        }

        @Override
        public String toString() {
            return "key: "+ key;
        }
    }

    public interface Listener {
        void onKeyBindsEnabledChanged(boolean enabled);
    }

    //region Actions
    private void addAction(String actionKey, Runnable runnable) {
        int keyHash = hash(actionKey);
        Action action = new Action(actionKey, runnable);
        actionMap.put(keyHash, action);
    }

    private void initActions() {
        addAction("inv1", () -> actionEmulator.useItem(1));
        addAction("inv2", () -> actionEmulator.useItem(2));
        addAction("inv3", () -> actionEmulator.useItem(3));
        addAction("inv4", () -> actionEmulator.useItem(4));
        addAction("inv5", () -> actionEmulator.useItem(5));
        addAction("inv6", () -> actionEmulator.useItem(6));

        addAction("abil1", () -> actionEmulator.useAbility(1));
        addAction("abil2", () -> actionEmulator.useAbility(2));
        addAction("abil3", () -> actionEmulator.useAbility(3));
        addAction("abil4", () -> actionEmulator.useAbility(4));
        addAction("abil5", () -> actionEmulator.useAbility(5));
        addAction("abil6", () -> actionEmulator.useAbility(6));
        addAction("abil7", () -> actionEmulator.useAbility(7));

        addAction("hold", () -> actionEmulator.orderToHold());
        addAction("stop", () -> actionEmulator.orderToStop());
        addAction("attack", () -> actionEmulator.orderToAttack());
    }
    //endregion
}
