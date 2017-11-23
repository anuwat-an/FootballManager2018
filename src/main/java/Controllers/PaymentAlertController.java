package Controllers;

import Models.ReservationLabel;
import Models.ReservationManager;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class PaymentAlertController {
    private Stage stage;
    private ReservationManager manager;
    private ArrayList<ReservationLabel> labelsSlc;
    public void submitAction(ActionEvent actionEvent) {


    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setlabelsSlc(ArrayList<ReservationLabel> labelsSlc) {
        this.labelsSlc = labelsSlc;
    }

    public void setManager(ReservationManager manager) {
        this.manager = manager;
    }
}
