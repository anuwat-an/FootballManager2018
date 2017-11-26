package Models;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * @author INT
 */

public class ReservationLabel extends Label {

    private int row;
    private int column;

    private boolean selected;

    private boolean reserved;
    private boolean available;

    public ReservationLabel(int row, int column, String text) {
        super(text);
        this.row = row;
        this.column = column;

        this.available = true;

        this.setAlignment(Pos.CENTER);
        this.setPrefSize(75,130);
        this.setMinWidth(75);
        this.setMinHeight(130);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMaxHeight(Double.MAX_VALUE);
    }

    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }

    public boolean isSelected() { return this.selected; }

    public boolean isReserved() { return this.reserved; }
    public boolean isAvailable() { return this.available; }

    public void setSelected() {
        if (available && !selected) {
            this.setStyle("-fx-background-color: cornflowerblue");
        }
        else if (available && selected) {
            this.setStyle("-fx-background-color: none");
        }
        else if (reserved && !selected) {
            this.setStyle("-fx-background-color: orange");
        }
        else if (reserved && selected) {
            this.setStyle("-fx-background-color: red");
        }
        this.selected = !this.selected;
    }

    public void setReserved() {
        this.reserved = true;
        this.available = false;
        this.setStyle("-fx-background-color: red");
    }

    public void setAvailable() {
        this.reserved = false;
        this.available = true;
        this.setText("Available");
        this.setStyle("-fx-background-color: none");
    }

}
