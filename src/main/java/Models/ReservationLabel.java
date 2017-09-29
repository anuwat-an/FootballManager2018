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
        this.setPrefSize(73.5,90);
        this.setMaxWidth(Double.MAX_VALUE);
    }

    public int getRow() { return this.row; }

    public int getColumn() { return this.column; }

    public boolean isSelected() { return this.selected; }
    public boolean isReserved() { return this.reserved; }
    public boolean isAvailable() { return this.available; }

    public void setSelected() {
        if (selected) {
            if (available) {
                setAvailable();
            }
            else if (reserved) {
                setReserved();
            }
            this.selected = false;
        }
        else {
            this.selected = true;
            this.setStyle("-fx-background-color: cornflowerblue");
        }
//        if (!isReserved()) {
//            if (isSelected()) {
//                this.selected = false;
//                this.available = true;
//                this.setStyle("-fx-background-color: none");
//            } else {
//                this.available = false;
//                this.selected = true;
//                this.setStyle("-fx-background-color: cornflowerblue");
//            }
//        }
    }

    public void setReserved() {
        this.selected = false;
        this.reserved = true;
        this.available = false;
        this.setStyle("-fx-background-color: red");
    }

    public void setAvailable() {
        this.selected = false;
        this.reserved = false;
        this.available = true;
        this.setStyle("-fx-background-color: none");
    }

}
