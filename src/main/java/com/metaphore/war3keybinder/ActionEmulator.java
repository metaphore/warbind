package com.metaphore.war3keybinder;

import com.metaphore.NativeInput;

import java.awt.*;
import java.awt.event.InputEvent;

public class ActionEmulator {
    private final int screenWidth;
    private final int screenHeight;
    private final Robot robot;
    private final NativeInput nativeInput;

    public ActionEmulator() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        screenWidth = gd.getDisplayMode().getWidth();
        screenHeight = gd.getDisplayMode().getHeight();

        nativeInput = new NativeInput();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error instantiating robot", e);
        }
    }

    public void useItem(int itemNum) {
        if (itemNum < 1 || itemNum > 6) {
            throw new IllegalArgumentException("item num must be within 1-6, current: " + itemNum);
        }

        int col = (itemNum + 1) % 2;
        int row = 2 - (itemNum - 1) / 2;

        Point p = Coords.getInvItemPosition(col, row, screenWidth, screenHeight);
        performClick(p.x, p.y);
    }

    public void useAbility(int abilityNum) {
        if (abilityNum < 1 || abilityNum > 7) {
            throw new IllegalArgumentException("ability num must be within 1-6, current: " + abilityNum);
        }

        int col = 0;
        int row = 0;
        switch (abilityNum) {
            case 1:
                col = 0;
                row = 0;
                break;
            case 2:
                col = 1;
                row = 0;
                break;
            case 3:
                col = 2;
                row = 0;
                break;
            case 4:
                col = 3;
                row = 0;
                break;
            case 5:
                col = 1;
                row = 1;
                break;
            case 6:
                col = 2;
                row = 1;
                break;
            case 7:
                col = 3;
                row = 1;
                break;
        }

        Point p = Coords.getActItemPosition(col, row, screenWidth, screenHeight);
        performClick(p.x, p.y);
    }

    public void orderToStop() {
        Point p = Coords.getActItemPosition(1, 2, screenWidth, screenHeight);
        performClick(p.x, p.y);
    }

    public void orderToHold() {
        Point p = Coords.getActItemPosition(2, 2, screenWidth, screenHeight);
        performClick(p.x, p.y);
    }

    public void orderToAttack() {
        Point p = Coords.getActItemPosition(3, 2, screenWidth, screenHeight);
        performClick(p.x, p.y);
    }

    public void performClick(int x, int y) {
        Point origLoc = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        nativeInput.performClick(origLoc.x, origLoc.y);
        robot.mouseMove(origLoc.x, origLoc.y);
    }
}
