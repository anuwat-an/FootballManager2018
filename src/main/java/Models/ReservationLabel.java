package Models;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class ReservationLabel extends Label {

    private int row;
    private int column;
    private String text;

    private boolean selected;
    private boolean reserved;
    private boolean available;

    public ReservationLabel(int row, int column, String text) {
        super(text);
        this.row = row;
        this.column = column;
        this.text = text;

        this.available = true;

        this.setAlignment(Pos.CENTER);
    }

    public int getRow() { return this.row; }

    public int getColumn() { return this.column; }

    public boolean isSelected() { return this.selected; }
    public boolean isReserved() { return this.reserved; }
    public boolean isAvailable() { return this.available; }

    public void setSelected() {
        if (!isReserved()) {
            if (isSelected()) {
                this.selected = false;
                this.available = true;
                this.setStyle("-fx-background-color: none");
            } else {
                this.available = false;
                this.selected = true;
                this.setStyle("-fx-background-color: cornflowerblue");
            }
        }
    }

    public void setReserved() {
        if (!isReserved()) {
            this.reserved = true;
            this.selected = false;
            this.available = false;
            this.setStyle("-fx-background-color: red");
        }
    }

    public void setAvailable() {
        this.reserved = false;
        this.selected = false;
        this.available = true;
        this.setStyle("-fx-background-color: none");
    }

}
