package org.firstinspires.ftc.teamcode.BillsAllyMap;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.checker.units.qual.A;

public class BackMap {
    public static final int maxColCount = 7;
    public static final int rowCount = 10;
    private int colCount = maxColCount;

    private ElapsedTime runtime;

    private StringBuilder sb = new StringBuilder();

    public CellVector firstSlot;
    public CellVector secondSlot;
    public CellNavigator navigator;

    private CellContent[][] map = new CellContent[rowCount][maxColCount];

    public BackMap() {
        navigator = new CellNavigator(0, rowCount, 0, maxColCount);
        runtime = new ElapsedTime();
        firstSlot = new CellVector();
        secondSlot = new CellVector();

        for (int i = rowCount-1; i >= 0; i--) {
            for (int j = 0; j < maxColCount; j++) {
                map[i][j] = CellContent.EMPTY;
            }
        }
    }

    public void start() {
        runtime.reset();
    }

    public CellContent getCurrentCell() {
        return map[navigator.row][navigator.col];
    }

    public void setFirstCell() {
        if (map[navigator.row][navigator.col] == CellContent.EMPTY) {
            if (firstSlot.selected) {
                map[firstSlot.row][firstSlot.col] = CellContent.EMPTY;
            }
            map[navigator.row][navigator.col] = CellContent.FIRST_PIXEL;
            firstSlot.set(navigator.row, navigator.col);
            firstSlot.selected = true;
        }
    }

    public void setSecondCell() {
        if (map[navigator.row][navigator.col] == CellContent.EMPTY) {
            if (secondSlot.selected) {
                map[secondSlot.row][secondSlot.col] = CellContent.EMPTY;
            }
            map[navigator.row][navigator.col] = CellContent.SECOND_PIXEL;
            secondSlot.set(navigator.row, navigator.col);
            secondSlot.selected = true;
        }
    }

    public void fillSelectedCells() { // TODO: Can separate this into one function for each
        if (firstSlot.selected) {
            map[firstSlot.row][firstSlot.col] = CellContent.WHITE;
            firstSlot.reset();
        }
        if (secondSlot.selected) {
            map[secondSlot.row][secondSlot.col] = CellContent.WHITE;
            secondSlot.reset();
        }
    }

    public void clearCell() {
        int row = navigator.row;
        int col = navigator.col;
        map[row][col] = CellContent.EMPTY;
        if (row == firstSlot.row && col == firstSlot.col) {
            firstSlot.reset();
        }
        else if (row == secondSlot.row && col == secondSlot.col) {
            secondSlot.reset();
        }
    }

    public String printMap() {
        sb = new StringBuilder();

        //Build Map
        for (int i = rowCount-1; i >= 0; i--) {
            sb.append("|");

            if (isOdd(i+1)) {
                sb.append("      ");
                for (int j = 0; j < maxColCount-1; j++) {
                    buildSlot(i, j);
                    if (j < maxColCount-2) {
                        sb.append("    ");
                    }
                }
                sb.append("     ");
            }
            else {
                for (int j = 0; j < maxColCount; j++) {
                    buildSlot(i, j);
                    if (j < maxColCount-1) {
                        sb.append("    ");
                    }
                }
            }
            sb.append("|\n");
        }
        //    A
        //     A
        //    A

        return sb.toString();
              /*"|(O)    (O)    (O)    (O)    (O)    (O)    (O)|\n" +
                "|     (O)    (O)    (O)    (O)    (O)    (O)     |\n" +
                "|(w)    (O)    (O)    (O)    (O)    (O)    (O)|\n" +
                "|     (O)    (O)    (O)    (O)    (O)    (O)     |\n" +
                "|(O)    (O)    (O)    (O)    (O)    (O)    (O)|\n" +
                "|     (O)    (O)    (O)    (O)    (O)    (O)     |\n" +
                "|(2)    (O)    (O)    (O)    (O)    (O)    (O)|\n" +
                "|     (O)    (O)    (O)    (O)    (O)    (O)     |\n" +
                "|(O)    (O)    (O)    (O)    (O)    (O)    (O)|\n" +
                "|     (1)    (O)    (O)    (O)    (O)    (O)     |\n";
        */
    }

    private void buildSlot(int row, int col) {
        if (row == navigator.row && col == navigator.col) {
            sb.append("[");
        }
        else {
            sb.append("  ");
        }
        if (map[row][col] == CellContent.WHITE) {
            sb.append("w");
        }
        else if (map[row][col] == CellContent.FIRST_PIXEL) {
            sb.append("1");
        }
        else if (map[row][col] == CellContent.SECOND_PIXEL) {
            sb.append("2");
        }
        else {
            sb.append("O");
        }
        if (row == navigator.row && col == navigator.col) {
            sb.append("]");
        }
        else {
            sb.append("  ");
        }
    }

    public String printSong() {
        if (runtime.seconds() > 2) {
            if (runtime.seconds() > 4) {
                if (runtime.seconds() > 6) {
                    if (runtime.seconds() > 8) {
                        if (runtime.seconds() > 10) {
                            if (runtime.seconds() > 12) {
                                if (runtime.seconds() > 16) {
                                    runtime.reset();
                                }
                                return "I'm the Map!";
                            }
                            return "I can get you there, I bet";
                        }
                        return "If there's a place you got to get";
                    }
                    return "I'm the Map, I'm the Map";
                }
                return "I'm the Map";
            }
            return "I'm the one you need to know";
        }
        return "If there's a place you got to go";
    }

    private boolean isOdd(int x) {
        return x % 2 != 0;
    }
}
