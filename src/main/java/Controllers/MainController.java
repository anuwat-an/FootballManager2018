package Controllers;

import Models.ReservationInfo;
import Models.ReservationLabel;
import Models.ReservationManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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

    private ArrayList<ReservationLabel> labels;
    private ArrayList<ReservationLabel> selected;

    private ReservationManager manager;

    @FXML
    private GridPane timeGrid;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button okBtn;

    private Date date = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);

    public MainController(){
        selected = new ArrayList<>();
        labels = new ArrayList<>();
        manager = new ReservationManager();
    }

    @FXML
    public void initialize(){

        for (int i = 1; i <= timeGrid.getRowConstraints().size(); i++) {
            for (int j = 0; j <timeGrid.getColumnConstraints().size() ; j++) {

                if(j == 0 && i!=0){

                    Label l = new Label("STADIUM "+(i));
                    l.setStyle("-fx-font-size: 13");
                    timeGrid.add(l,j,i);

                }

                else {
                    ReservationLabel l = new ReservationLabel(i, j, "Available");

                    l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent event) {
                            if (l.isSelected()) {
                                selected.remove(l);
                            }
                            else {
                                selected.add(l);
                            }
                            l.setSelected();
                        }
                    });

                    labels.add(l);
                    timeGrid.add(l,j,i);

                }

            }
        }

        datePicker.setValue(LocalDate.now());

        loadReservationInfos();

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
                    String dateTimeStr = resultSet.getString("dateTime");
                    int fieldNumber = resultSet.getInt("fieldNumber");
                    int fieldPrice = resultSet.getInt("fieldPrice");
                    String customerName = resultSet.getString("customerName");
                    String customerTel = resultSet.getString("customerTel");

                    this.date = dateFormat.parse(dateTimeStr);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(this.date.toInstant(), ZoneId.systemDefault());
                    ReservationInfo reservationInfo = new ReservationInfo(localDateTime, fieldNumber, fieldPrice, customerName, customerTel);

                    manager.addReservation(reservationInfo);
                }
            }
            connection.close();

            updateLabels();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void updateLabels() {
        ArrayList<ReservationInfo> infos = manager.getReservations();

        for (ReservationLabel label : labels) {
            label.setText("Available");
            label.setAvailable();
        }
        for (ReservationInfo info : infos) {
            int fieldNumber = info.getFieldNumber();
            int time = info.getDateTime().toLocalTime().getHour()-7;

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


    public void okHandle(ActionEvent event) {
        putInfo();
    }

    private void putInfo() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AlertDialog_css/AlertDialog_css.fxml"));
//        if(editIDField.getText() != ""){
        try {
            stage.initOwner(okBtn.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Appointment list");

            AlertController alertController = loader.getController();
            alertController.setLabelsSlc(selected);
            alertController.setStage(stage);
            alertController.setDate(datePicker.getValue());
            alertController.setManager(manager);

            System.out.println(manager.getReservations().size());

            stage.showAndWait();
//            showAppoint();
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

}











