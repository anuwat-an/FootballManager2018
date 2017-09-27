package Controllers;

import Models.ReservationLabel;
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
import java.util.ArrayList;


public class MainController {

    public ArrayList<ReservationLabel> getSelected() {
        return selected;
    }

    private ArrayList<ReservationLabel> selected;
    public MainController(){
        selected = new ArrayList<ReservationLabel>();
    }

    @FXML
    private GridPane timeGrid;
    @FXML
    private DatePicker datePicker;s
    @FXML
    private Button okBtn;
    @FXML
    public void initialize(){

        for (int i = 1; i <= timeGrid.getRowConstraints().size(); i++) {
            for (int j = 0; j <timeGrid.getColumnConstraints().size() ; j++) {

                if(j == 0 && i!=0){

                    timeGrid.add(new Label("STADIUM "+(i)),j,i);

                }

                else {
                    ReservationLabel l = new ReservationLabel(i, j, "test");

                    l.setPrefSize(73.5,90);
                    l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent event) {
                            if (l.isSelected()) {
                                l.setSelected();
                                selected.remove(l);
                            }
                            else {
                                l.setSelected();
                                selected.add(l);
                            }
                        }
                    });

                    timeGrid.add(l,j,i);
//
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
                alertController.labelsSlc = selected;
                alertController.stage = stage;

                stage.showAndWait();
//                showAppoint();



            } catch (IOException e) {
                e.printStackTrace();
            }
        }



}











