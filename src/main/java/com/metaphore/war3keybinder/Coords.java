package com.metaphore.war3keybinder;

import java.awt.*;

public class Coords {
    private static final float INV_ROW0 = .9722f;
    private static final float INV_ROW1 = .9048f;
    private static final float INV_ROW2 = .8413f;
    private static final float INV_COL0 = .6671f;
    private static final float INV_COL1 = .7177f;

    private static final float ACT_ROW0 = .9603f;
    private static final float ACT_ROW1 = .8888f;
    private static final float ACT_ROW2 = .8174f;
    private static final float ACT_COL0 = .7981f;
    private static final float ACT_COL1 = .8516f;
    private static final float ACT_COL2 = .9082f;
    private static final float ACT_COL3 = .9589f;

    private static final Point tmpPoint = new Point();

    public static Point getInvItemPosition(int column, int row, int screenWidth, int screenHeight) {
        int x = (int)(screenWidth * resolveInvCol(column));
        int y = (int)(screenHeight * resolveInvRow(row));
        tmpPoint.setLocation(x, y);
        return tmpPoint;
    }

    private static float resolveInvCol(int column) {
        switch (column) {
            case 0: return INV_COL0;
            case 1: return INV_COL1;
            default:
                throw new IllegalArgumentException("Wrong inventory column: " + column);
        }
    }

    private static float resolveInvRow(int row) {
        switch (row) {
            case 0: return INV_ROW0;
            case 1: return INV_ROW1;
            case 2: return INV_ROW2;
            default:
                throw new IllegalArgumentException("Wrong inventory row: " + row);
        }
    }

    public static Point getActItemPosition(int col, int row, int screenWidth, int screenHeight) {
        int x = (int)(screenWidth * resolveActCol(col));
        int y = (int)(screenHeight * resolveActRow(row));
        tmpPoint.setLocation(x, y);
        return tmpPoint;
    }

    private static float resolveActCol(int col) {
        switch (col) {
            case 0: return ACT_COL0;
            case 1: return ACT_COL1;
            case 2: return ACT_COL2;
            case 3: return ACT_COL3;
            default:
                throw new IllegalArgumentException("Wrong action column: " + col);
        }
    }

    private static float resolveActRow(int row) {
        switch (row) {
            case 0: return ACT_ROW0;
            case 1: return ACT_ROW1;
            case 2: return ACT_ROW2;
            default:
                throw new IllegalArgumentException("Wrong action row: " + row);
        }
    }
}
