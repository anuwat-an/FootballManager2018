package controllers;

import models.ReservationInfo;
import models.ReservationLabel;
import models.ReservationManager;
import dataSources.DataSource;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * @author INT
 */

public class PaymentAlertController {

    @FXML
    private Label nameHolder, hourHolder, priceHolder, changeLabel, warningLabel;
    @FXML
    private TextField paidTextField;
    @FXML
    private Button submitButton, cancelButton;

    private Stage stage;
    private ReservationManager manager;
    private ArrayList<ReservationLabel> selectedLabels;
    private LocalDate localDate;

    private double totalPrice;
    private ArrayList<Integer> ids;
    private DataSource dataSource;

    public PaymentAlertController() {
        totalPrice = 0;
        ids = new ArrayList<>();
    }

    @FXML
    public void submitAction(ActionEvent actionEvent) {

        double paid = 0;
        try {
            paid = Double.parseDouble(paidTextField.getText());
        } catch (NumberFormatException e) {
            warningLabel.setText("Insufficient fund");
            return;
        }
        if (totalPrice > paid) {
            warningLabel.setText("Insufficient fund");
            return;
        }

        warningLabel.setText("");
        double change = paid - totalPrice;
        changeLabel.setText(change+"");
        paidTextField.setEditable(false);
        submitButton.setDisable(true);
        cancelButton.setText("Done");

        for (int id : ids) {
            manager.deleteReservation(id);
        }

        dataSource.setReservationStatus(ids, "PAID");

        for (ReservationLabel l : selectedLabels) {
            l.setAvailable();
            if (l.isSelected())
                l.setSelected();
        }

        selectedLabels.clear();

    }

    @FXML
    public void cancel() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSelectedLabels(ArrayList<ReservationLabel> selectedLabels) {
        this.selectedLabels = selectedLabels;
        setInfoLabels();
    }

    private void setInfoLabels() {
        String customerName = "";
        int hours = 0;
        totalPrice = 0;

        for (ReservationLabel l : selectedLabels) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(l.getColumn()+7, 0));
            ReservationInfo info = manager.getReservation(localDateTime, l.getRow());

            customerName = info.getCustomerName();
            ids.add(info.getId());
            hours += 1;
            totalPrice += info.getFieldPrice();
        }
        nameHolder.setText(customerName);
        hourHolder.setText(hours+"");
        priceHolder.setText(totalPrice+"");
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
