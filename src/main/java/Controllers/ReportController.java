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

public class ReportController {
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

    @FXML
    public void initialize() {
        iNum.setCellValueFactory(new PropertyValueFactory<ReservationInfo,Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,String>("customerName"));
        telColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,String>("customerTel"));
        stadiumColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,Integer>("fieldNumber"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,String>("dateTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<ReservationInfo,Integer>("dateTimec"));

        reportTable.setItems(lst);
    }
}
