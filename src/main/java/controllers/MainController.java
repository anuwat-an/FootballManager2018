package controllers;

import dataSources.DataSource;
import dataSources.SQLiteSource;
import models.ReservationInfo;
import models.ReservationLabel;
import models.ReservationManager;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author INT
 */

public class MainController {

    @FXML
    private GridPane timeGrid;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button okBtn, removeBtn, reportBtn, payBtn;

    private ArrayList<ReservationLabel> labels; // all labels
    private ArrayList<ReservationLabel> selected; // being selected labels

    private ReservationManager manager;
    private DataSource dataSource;

    public MainController(){
        selected = new ArrayList<>();
        labels = new ArrayList<>();

        manager = new ReservationManager();
        dataSource = new SQLiteSource();
    }

    @FXML
    /**
     * @1 insert labels into time grid
     * @2 set datePicker to present
     * @3 load reservation info from database and assign to manager
     * @4 update labels in time grid
     */
    public void initialize() {
        /* @1 */
        for (int i=1; i<timeGrid.getRowConstraints().size(); i++) {
            for (int j=1; j<timeGrid.getColumnConstraints().size() ; j++) {

                ReservationLabel l = new ReservationLabel(i, j, "Available");

                    l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent event) {
                            if (l.isSelected()) {
                                selected.remove(l);
                            } else {
                                selected.add(l);
                            }
                            l.setSelected();
                        }
                    });

                    this.labels.add(l);
                    this.timeGrid.add(l,j,i);

            }
        }

        /* @2 */
        datePicker.setValue(LocalDate.now());
        /* @3 */
        manager.setReservationInfo(dataSource.loadReservationInfo("NOTPAID"));
        /* @4 */
        updateLabels();
    }

    @FXML
    /**
     * @1 set labels in time grid to "Available"
     * @2 get reservation info from manager
     * @3 set labels match info's date, time, and field to "Reserved"
     */
    public void updateLabels() {
        /* @1 */
        for (ReservationLabel label : labels) {
//            label.setText("Available");
            label.setAvailable();
            if (label.isSelected())
                label.setSelected();
        }

        /* @2 */
        ArrayList<ReservationInfo> info = manager.getReservationInfo();

        /* @3 */
        for (ReservationInfo reservationInfo : info) {
            int fieldNumber = reservationInfo.getFieldNumber(); // row
            int time = reservationInfo.getDateTime().toLocalTime().getHour()-7; // column

            if (datePicker.getValue().equals(reservationInfo.getDateTime().toLocalDate())) {
                for (ReservationLabel label : labels) {
                    if (label.getRow() == fieldNumber && label.getColumn() == time) {
                        label.setText(reservationInfo.getCustomerName()+"\n"+reservationInfo.getCustomerTel());
                        label.setReserved();
                    }
                }
            }
        }
    }

    @FXML
    /**
     * @1 check if there are selected fields and are available
     * @2 show customer info dialog
     */
    public void okHandle() {
        /* @1 */
        if (selected.size() == 0) {
            createAlert("Error! No fields are selected.", "Please select available fields.");
            return;
        }
        for (ReservationLabel label : selected) {
            if (label.isReserved()) {
                createAlert("Error! Reserved fields are selected.", "Please unselect reserved fields.");
                return;
            }
        }

        /* @2 */
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reserveDialog/ReserveDialog.fxml"));
        try {
            stage.initOwner(okBtn.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Customer Info");
            stage.initModality(Modality.WINDOW_MODAL);

            ReserveAlertController alertController = loader.getController();
            alertController.setStage(stage);
            alertController.setManager(manager);
            alertController.setSelectedLabels(selected);
            alertController.setDate(datePicker.getValue());
            alertController.setDataSource(dataSource);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * @1 check if there are selected fields and are reserved
     * @2 show confirm remove dialog
     */
    public void removeHandle() {
        /* @1 */
        if (selected.size() == 0) {
            createAlert("Error! No fields are selected.", "Please select reserved fields.");
            return;
        }
        for (ReservationLabel label : selected) {
            if (label.isAvailable()) {
                createAlert("Error! Available fields are selected.", "Please unselect available fields.");
                return;
            }
        }

        /* @2 */
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cancelDialog/CancelDialog.fxml"));
        try {
            stage.initOwner(removeBtn.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Confirm Cancel");
            stage.initModality(Modality.WINDOW_MODAL);

            CancelAlertController alertController = loader.getController();
            alertController.setStage(stage);
            alertController.setManager(manager);
            alertController.setSelectedLabels(selected);
            alertController.setDate(datePicker.getValue());
            alertController.setDataSource(dataSource);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * @1 show report window
     */
    public void reportHandle() {
        /* @1 */
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reportView/ReportView.fxml"));
        try {
            stage.initOwner(reportBtn.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Report");
            stage.initModality(Modality.WINDOW_MODAL);

            ReportController controller = loader.getController();
            controller.setDataSource(dataSource);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * @1 check if there are selected fields and are reserved
     * @2 show payment dialog
     */
    public void payAction() {
        /* @1 */
        if (selected.size() == 0) {
            createAlert("Error! No fields are selected.", "Please select reserved fields.");
            return;
        }
        for (ReservationLabel label : selected) {
            if (label.isAvailable()) {
                createAlert("Error! Available fields are selected.", "Please unselect available fields.");
                return;
            }
        }

        /* @2 */
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PayDialog.fxml"));
        try {
            stage.initOwner(payBtn.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Payment");
            stage.initModality(Modality.WINDOW_MODAL);

            PaymentAlertController alertController = loader.getController();
            alertController.setStage(stage);
            alertController.setManager(manager);
            alertController.setDate(datePicker.getValue());
            alertController.setSelectedLabels(selected);
            alertController.setDataSource(dataSource);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * show alert modal box
     * @param title
     * @param message
     */
    private void createAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}











