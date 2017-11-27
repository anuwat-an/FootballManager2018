package controllers;

import dataSources.DataSource;
import library.ToolsLibrary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ReservationInfo;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author INT
 */

public class ReportController {

    @FXML
    private DatePicker fromDatePicker, ToDatePicker;
    @FXML
    private Label totalPrice, warningDate;

    @FXML
    private TableView<ReportInfo> reportTable;
    @FXML
    private TableColumn<ReportInfo, Integer> stadiumColumn;
    @FXML
    private TableColumn<ReportInfo, Double>  priceColumn;
    @FXML
    private TableColumn<ReportInfo, String> nameColumn, telColumn, timeColumn;
    @FXML
    private ComboBox<String> statusCombo;

    private ObservableList<ReportInfo> list = FXCollections.observableArrayList();
    private DataSource dataSource;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<ReportInfo,String>("name"));
        telColumn.setCellValueFactory(new PropertyValueFactory<ReportInfo,String>("tel"));
        stadiumColumn.setCellValueFactory(new PropertyValueFactory<ReportInfo,Integer>("field"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<ReportInfo,String>("time"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<ReportInfo,Double>("price"));

        statusCombo.getItems().add("PAID");
        statusCombo.getItems().add("NOTPAID");
        statusCombo.getItems().add("CANCEL");

        fromDatePicker.setValue(LocalDate.now());
        ToDatePicker.setValue(LocalDate.now());

        reportTable.setItems(list);
    }

    @FXML
    private void updateReportRows() {

        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = ToDatePicker.getValue();
        if (fromDate.isAfter(toDate)) {
            warningDate.setText("Invalid Date");
            return;
        }
        if(statusCombo.getValue() == null){
            warningDate.setText("Please choose status!");
            return;
        }

        warningDate.setText("");
        list.clear();

        ArrayList<ReservationInfo> info = dataSource.loadReportInfo(statusCombo.getValue());
        for (ReservationInfo reservationInfo : info) {
            if (reservationInfo.getDateTime().toLocalDate().isBefore(fromDate) ||
                reservationInfo.getDateTime().toLocalDate().isAfter(toDate))
                continue;

            boolean isReported = false;
            for (ReportInfo report : list) {
                if (report.name.equals(reservationInfo.getCustomerName()) &&
                    report.field == reservationInfo.getFieldNumber()) {

                    String[] reportInfoTime = report.time.split(" - ");
                    int dbInfoTime = reservationInfo.getDateTime().getHour();

                    if (Integer.parseInt(reportInfoTime[1]) == dbInfoTime) {
                        report.time = reportInfoTime[0] + " - " + (dbInfoTime + 1);
                        isReported = true;
                    }
                    else if (Integer.parseInt(reportInfoTime[0]) == dbInfoTime+1) {
                        report.time = dbInfoTime + " - " + reportInfoTime[1];
                        isReported = true;
                    }

                    if (isReported) {
                        report.price += reservationInfo.getFieldPrice();
                        break;
                    }
                }
            }

            if (!isReported) {
                String name = reservationInfo.getCustomerName();
                String tel = reservationInfo.getCustomerTel();
                int field = reservationInfo.getFieldNumber();
                String time = reservationInfo.getDateTime().getHour() + " - " + (reservationInfo.getDateTime().getHour() + 1);
                double price = reservationInfo.getFieldPrice();
                list.add(new ReportInfo(name, tel, field, time, price));
            }
        }

        calculateTotalPrice();
    }

    private void calculateTotalPrice() {

        double total = 0;
        for (int i=0; i<list.size(); i++) {
            total += priceColumn.getCellData(i);
        }
        totalPrice.setText(total+"");

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public class ReportInfo {

        private int field;
        private String name, tel, time;
        private double price;

        public ReportInfo(String name, String tel, int field, String time, double price) {
            this.name = name;
            this.tel = tel;
            this.field = field;
            this.time = time;
            this.price = price;
        }

        public int getField() {
            return field;
        }

        public String getName() {
            return name;
        }

        public String getTel() {
            return tel;
        }

        public String getTime() {
            return time;
        }

        public double getPrice() {
            return price;
        }
    }

}
