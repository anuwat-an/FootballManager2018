package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class AlertController {
    ArrayList<Label> labelsSlc;
    public AlertController(){
        labelsSlc = new ArrayList<Label>();
    }
    @FXML
    private Button okButton;




    public void infoDoneAction(ActionEvent event) {

    }

    public void cancleDoneAction(ActionEvent event) {
    }

}
