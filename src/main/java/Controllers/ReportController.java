package Controllers;

import Models.ReservationInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportController implements Initializable{
    @FXML
    DatePicker fromDatePicker, ToDatePicker;
    @FXML
    Button okDateBtn;
    @FXML
    Label amountAll;

    @FXML
    TableView<ReservationInfo> reportTable;
    @FXML
    TableColumn<ReservationInfo, Integer> iNum, stadiumColumn, priceColumn;
    @FXML
    TableColumn<ReservationInfo, String> nameColumn, telColumn, timeColumn;

    ObservableList<ReservationInfo> lst = FXCollections.observableArrayList(
      new ReservationInfo(1, LocalDateTime.of(2017,2,1,12,0),1, 700,"a","1234")
    );

    public void okHandle(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iNum.setCellValueFactory(new PropertyValueFactory<ReservationInfo,Integer>("iNum"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,String>("nameColumn"));
        telColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,String>("tel"));
        stadiumColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,Integer>("stadium"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,String>("time"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,Integer>("price"));

        reportTable.setItems(lst);
    }
}
