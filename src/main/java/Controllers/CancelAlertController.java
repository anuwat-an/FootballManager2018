package Controllers;

import Models.ReservationInfo;
import Models.ReservationLabel;
import Models.ReservationManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class CancelAlertController {

    private Stage stage;
    private ArrayList<ReservationLabel> labelsSlc;
    private ReservationManager manager;
    private LocalDate localDate;

    @FXML
    public void cancelHandle(ActionEvent actionEvent) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:ReservationInfoDB.db";
            Connection connection = DriverManager.getConnection(dbURL);
            if (connection != null) {

                for (ReservationLabel l: labelsSlc) {
                    LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(l.getColumn()+7, 0));
                    ReservationInfo info = manager.getReservation(localDateTime, l.getRow());

                    String query = "update ReservationInfos set status='CANCEL' where id=" + info.getId();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);

                    manager.deleteReservation(info.getId());

                    l.setAvailable();
                    if (l.isSelected())
                        l.setSelected();
                }
                labelsSlc.clear();
                connection.close();

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stage.close();
    }

    @FXML
    public void no() {
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
