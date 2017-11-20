package Controllers;

import Models.ReservationInfo;
import Models.ReservationLabel;
import Models.ReservationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sun.applet.Main;

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

public class ReserveAlertController {

    private Stage stage;
    private ArrayList<ReservationLabel> labelsSlc;
    private ReservationManager manager;
    private LocalDate localDate;

    @FXML
    private TextField nameField;
    @FXML
    private TextField telField;

    private Date date = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);

    public ReserveAlertController(){
        labelsSlc = new ArrayList<ReservationLabel>();
    }

    @FXML
    public void confirmAction(ActionEvent event) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:ReservationInfoDB.db";
            Connection connection = DriverManager.getConnection(dbURL);
            if (connection != null) {

                for (ReservationLabel l: labelsSlc) {
                    String dateTimeStr = localDate.getDayOfMonth()+"/"+localDate.getMonthValue()+"/"+localDate.getYear()+" "+(l.getColumn()+7);
                    this.date = dateFormat.parse(dateTimeStr);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(this.date.toInstant(), ZoneId.systemDefault());

                    double price = 0;

                    String query = "insert into ReservationInfos (dateTime,fieldNumber,fieldPrice,customerName,customerTel) " +
                            "values ('"+dateTimeStr+"',"+
                            l.getRow()+","+
                            price+","+
                            "'"+nameField.getText()+"',"+
                            "'"+telField.getText()+"')";
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);

                    query = "select max(id) from ReservationInfos";
//                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    int id = resultSet.getInt(1);
                    ReservationInfo reservationInfo = new ReservationInfo(id, localDateTime, l.getRow(), price, nameField.getText(), telField.getText());
                    manager.addReservation(reservationInfo);

                    l.setText(nameField.getText()+"\n"+telField.getText());
                    l.setReserved();
                    l.setSelected();
                }
                labelsSlc.clear();
                connection.close();

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        stage.close();
    }

    @FXML
    public void cancelAction(ActionEvent event) {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLabelsSlc(ArrayList<ReservationLabel> labelsSlc) {
        this.labelsSlc = labelsSlc;
    }

    public void setManager(ReservationManager manager) {
        this.manager = manager;
    }

    public void setDate(LocalDate date) {
        this.localDate = date;
    }

}