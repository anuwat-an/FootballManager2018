package controllers;

import models.ReservationInfo;
import models.ReservationLabel;
import models.ReservationManager;
import dataSources.DataSource;
import library.ToolsLibrary;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author INT
 */

public class ReserveAlertController {

    @FXML
    private TextField nameField, telField;

    private Stage stage;
    private LocalDate localDate;
    private ReservationManager manager;
    private ArrayList<ReservationLabel> selectedLabels;
    private DataSource dataSource;

    private double reservationPrice = 600;

    @FXML
    /**
     * @1 get last id from database and +1
     * @2 create arraylist to store info for adding to database
     * @3 loop each selected labels and create Reservation object, add to manager and arraylist
     * @4 set label to "Reserved"
     * @5 add info to database using arraylist, clear selected labels arraylist, close window
     */
    public void confirmAction() {
        try {
            /* @1 */
            int lastID = dataSource.getLastID()+1;
            /* @2 */
            ArrayList<ReservationInfo> info = new ArrayList<>();
            /* @3 */
            for (ReservationLabel l : selectedLabels) {
                String dateTimeStr = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear() + " " + (l.getColumn() + 7);
                Date date = ToolsLibrary.dateTimeFormat.parse(dateTimeStr);
                LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                double price = this.reservationPrice;

                ReservationInfo reservationInfo = new ReservationInfo(lastID, localDateTime, l.getRow(), price, nameField.getText(), telField.getText());
                manager.addReservation(reservationInfo);
                info.add(reservationInfo);

                /* @4 */
                l.setText(nameField.getText()+"\n"+telField.getText());
                l.setReserved();
                if (l.isSelected())
                    l.setSelected();

                lastID += 1;
            }
            /* @5 */
            dataSource.insertReservationInfo(info);
            selectedLabels.clear();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        stage.close();
    }

    @FXML
    public void cancelAction() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSelectedLabels(ArrayList<ReservationLabel> selectedLabels) {
        this.selectedLabels = selectedLabels;
    }

    public void setManager(ReservationManager manager) {
        this.manager = manager;
    }

    public void setDate(LocalDate date) {
        this.localDate = date;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
