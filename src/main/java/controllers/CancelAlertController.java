package controllers;

import models.ReservationInfo;
import models.ReservationLabel;
import models.ReservationManager;
import dataSources.DataSource;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * @author INT
 */

public class CancelAlertController {

    private Stage stage;
    private ArrayList<ReservationLabel> selectedLabels;
    private ReservationManager manager;
    private LocalDate localDate;
    private DataSource dataSource;

    @FXML
    /**
     * @1 create arraylist to store ids for changing status in database
     * @2 loop each selected label, get info corresponding label
     * @3 delete the info from manager, add info's id to arraylist
     * @4 set label to "Available"
     * @5 update status of info in database, clear selected label arraylist, close window
     */
    public void cancelHandle() {
        /* @1 */
        ArrayList<Integer> ids = new ArrayList<>();
        /* @2 */
        for (ReservationLabel l: selectedLabels) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(l.getColumn()+7, 0));
            ReservationInfo info = manager.getReservation(localDateTime, l.getRow());

            /* @3 */
            int id = info.getId();
            manager.deleteReservation(id);
            ids.add(id);

            /* @4 */
            l.setAvailable();
            if (l.isSelected())
                l.setSelected();
        }
        /* @5 */
        dataSource.setReservationStatus(ids, "CANCEL");
        selectedLabels.clear();

        stage.close();
    }

    @FXML
    public void no() {
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
