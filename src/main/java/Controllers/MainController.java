package Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;



public class MainController {

    @FXML
    private GridPane timeGrid;

    @FXML
    private Button okBtn;
    @FXML
    public void initialize(){

        for (int i = 0; i < timeGrid.getRowConstraints().size(); i++) {
            for (int j = 0; j <timeGrid.getColumnConstraints().size() ; j++) {

                if(j == 0 && i!=0){

                    timeGrid.add(new Label("STADIUM "+(i)),j,i);
                }
                else {
                    final Label l = new Label("test");

                    l.setPrefSize(90,70);
                    l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent event) {
                            l.setStyle("-fx-background-color: cornflowerblue");

                        }
                    });

                    timeGrid.add(l,j,i);
//
                }

            }
        }


    }


}










