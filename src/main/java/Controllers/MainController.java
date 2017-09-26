package Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;



public class MainController {

    @FXML
    private GridPane timeGrid;

    @FXML
    public void initialize(){
        for (int i = 0; i < timeGrid.getRowConstraints().size(); i++) {
            for (int j = 0; j <timeGrid.getColumnConstraints().size() ; j++) {
                if(j == 0 && i!=0){

                    timeGrid.add(new Label("STADIUM "+(i)),j,i);
                }
                else {
                    final Label l = new Label(""+j);
//                    l.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                        public void handle(MouseEvent event) {
//                            l.getGraphic().setStyle("-fx-text-fill: #c4d8de;");
//                        }
//                    });
                    timeGrid.add(l,j,i);
                    if(l.isMouseTransparent()){
                        l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent event) {
                                l.getGraphic().setStyle("-fx-text-fill: #c4d8de;");
                            }
                        });
                    }
                }
            }
        }

    }
}










