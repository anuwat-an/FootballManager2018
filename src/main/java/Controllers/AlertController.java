package Controllers;

import Models.ReservationInfo;
import Models.ReservationLabel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AlertController {

    public Stage stage;
    ReservationInfo reservationInfo;
    ArrayList<ReservationLabel> labelsSlc;
    public AlertController(){
        labelsSlc = new ArrayList<ReservationLabel>();

    }
    @FXML
    private Button okButton;


    public void confirmAction(ActionEvent event) {
        for (ReservationLabel l: labelsSlc) {
            l.setReserved();
//            reservationInfo = new ReservationInfo()
        }
        stage.close();
    }

    public void cancleAction(ActionEvent event) {
    }
}
