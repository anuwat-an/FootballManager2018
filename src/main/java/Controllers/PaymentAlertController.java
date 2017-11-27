package Controllers;

import Models.ReservationInfo;
import Models.ReservationLabel;
import Models.ReservationManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * @author INT
 */

public class PaymentAlertController {
    private Stage stage;
    private ReservationManager manager;
    private ArrayList<ReservationLabel> labelsSlc;
    private LocalDate localDate;

    private ArrayList<Integer> slcIDs = new ArrayList<>();

    private double price;

    @FXML
    private Label nameHolder;
    @FXML
    private Label hourHolder;
    @FXML
    private Label priceHolder;
    @FXML
    private TextField paidTextField;
    @FXML
    private Label changeLabel;
    @FXML
    private Label warningLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    @FXML
    public void submitAction(ActionEvent actionEvent) {

        double paid = Integer.parseInt(paidTextField.getText());
        if (price > paid) {
            warningLabel.setText("Insufficient fund");
            return;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:ReservationInfoDB.db";
            Connection connection = DriverManager.getConnection(dbURL);
            if (connection != null) {

                for (int id : slcIDs) {
                    String query = "update ReservationInfos set status='PAID' where id=" + id;
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);

                    manager.deleteReservation(id);
                }

                for (ReservationLabel l : labelsSlc) {
                    l.setAvailable();
                    if (l.isSelected())
                        l.setSelected();
                }

                labelsSlc.clear();
                connection.close();

            }

            warningLabel.setText("");
            double change = paid - price;
            changeLabel.setText(change+"");
            paidTextField.setEditable(false);
            submitButton.setDisable(true);
            cancelButton.setText("Exit");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void cancel() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setlabelsSlc(ArrayList<ReservationLabel> labelsSlc) {
        this.labelsSlc = labelsSlc;
        setInfoLabels();
    }

    private void setInfoLabels() {
        String customerName = "";
        int hours = 0;
        price = 0;

        for (ReservationLabel l : labelsSlc) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(l.getColumn()+7, 0));
            ReservationInfo info = manager.getReservation(localDateTime, l.getRow());

            customerName = info.getCustomerName();
            slcIDs.add(info.getId());
            hours += 1;
            price += info.getFieldPrice();
        }
        nameHolder.setText(customerName);
        hourHolder.setText(hours+"");
        priceHolder.setText(price+"");
    }

    public void setManager(ReservationManager manager) {
        this.manager = manager;
    }

    public void setDate(LocalDate date) {
        this.localDate = date;
    }
}
