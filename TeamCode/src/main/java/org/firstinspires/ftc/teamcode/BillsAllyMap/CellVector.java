package org.firstinspires.ftc.teamcode.BillsAllyMap;

public class CellVector {
    public int row = 0;
    public int col = 0;
    public boolean selected = false;

    public CellVector() {
    }

    public void set(int row, int col) {
        this.row = row;
        this.col = col;
        selected = true;
    }

    public void reset() {
        row = 0;
        col = 0;
        selected = false;
    }
}
