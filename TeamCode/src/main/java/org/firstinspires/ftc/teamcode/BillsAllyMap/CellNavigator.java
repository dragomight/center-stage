package org.firstinspires.ftc.teamcode.BillsAllyMap;

public class CellNavigator {
    public int row = 0;
    public int col = 0;

    private final int maxRow;
    private final int minRow;
    private final int maxCol;
    private final int minCol;

    public CellNavigator(int minRow, int maxRow, int minCol, int maxCol) {
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        this.minRow = minRow;
        this.minCol = minCol;
    }

    public void navDown() {
        if (row > minRow) {
            row--;
            if (col == maxCol-1) {
                col--;
            }
        }
    }

    public void navUp() {
        if (row+1 < maxRow) {
            row++;
            if (col == maxCol-1) {
                col--;
            }
        }
    }

    public void navLeft() {
        if (col > minCol) {
            col--;
        }
    }

    public void navRight() {
        if (isOdd(row)) {
            if (col+1 < maxCol) {
                col++;
            }
        }
        else {
            if (col+2 < maxCol) {
                col++;
            }
        }
    }

    private boolean isOdd(int x) {
        return x % 2 != 0;
    }
}
