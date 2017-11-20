package Controllers;

import Models.ReservationInfo;
import Models.ReservationLabel;
import Models.ReservationManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainController {

    private ArrayList<ReservationLabel> labels; // all labels
    private ArrayList<ReservationLabel> selected; // being selected labels

    private ReservationManager manager;

    @FXML
    private GridPane timeGrid;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button okBtn;
    @FXML
    private Button removeBtn;
    @FXML
    private Button reportBtn;

    private Date date = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);

    public MainController(){
        selected = new ArrayList<>();
        labels = new ArrayList<>();
        manager = new ReservationManager();
    }

    @FXML
    public void initialize() {

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

        datePicker.setValue(LocalDate.now());
        loadReservationInfos();
        updateLabels();

    }

    public void loadReservationInfos() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:ReservationInfoDB.db";
            Connection connection = DriverManager.getConnection(dbURL);
            if (connection != null) {
                String query = "select * from ReservationInfos";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String dateTimeStr = resultSet.getString("dateTime");
                    int fieldNumber = resultSet.getInt("fieldNumber");
                    int fieldPrice = resultSet.getInt("fieldPrice");
                    String customerName = resultSet.getString("customerName");
                    String customerTel = resultSet.getString("customerTel");
                    date = dateFormat.parse(dateTimeStr);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                    ReservationInfo reservationInfo = new ReservationInfo(id, localDateTime, fieldNumber, fieldPrice, customerName, customerTel);

                    manager.addReservation(reservationInfo);
                }
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateLabels() {
        ArrayList<ReservationInfo> infos = manager.getReservations();

        for (ReservationLabel label : labels) {
            label.setText("Available");
            label.setAvailable();
        }

        for (ReservationInfo info : infos) {
            int fieldNumber = info.getFieldNumber(); // row
            int time = info.getDateTime().toLocalTime().getHour()-7; // column

            if (datePicker.getValue().equals(info.getDateTime().toLocalDate())) {
                for (ReservationLabel label : labels) {
                    if (label.getRow() == fieldNumber && label.getColumn() == time) {
                        label.setText(info.getCustomerName()+"\n"+info.getCustomerTel());
                        label.setReserved();
                    }
                }
            }
        }
    }

    @FXML
    public void okHandle() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReserveDialog/ReserveDialog.fxml"));
//        if(editIDField.getText() != ""){
        try {
            stage.initOwner(okBtn.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Customer Info");

            ReserveAlertController alertController = loader.getController();
            alertController.setStage(stage);
            alertController.setLabelsSlc(selected);
            alertController.setDate(datePicker.getValue());
            alertController.setManager(manager);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeHandle() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CancelDialog/CancelDialog.fxml"));
        try {
            stage.initOwner(removeBtn.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Confirm Cancel");

            CancelAlertController alertController = loader.getController();
            alertController.setStage(stage);
            alertController.setLabelsSlc(selected);
            alertController.setDate(datePicker.getValue());
            alertController.setManager(manager);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void reportHandle() {

    }

}











