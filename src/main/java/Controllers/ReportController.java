package Controllers;

import Models.ReservationInfo;

import Models.ReservationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

/**
 * @author INT
 */

public class ReportController {

    ReservationManager manager;
    private Date date = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    @FXML
    DatePicker fromDatePicker, ToDatePicker;
    @FXML
    Button okDateBtn;
    @FXML
    Label amountAll, warningDate;

    @FXML
    TableView<ReportInfo> reportTable;
    @FXML
    TableColumn<ReportInfo, Integer> stadiumColumn;
    @FXML
    TableColumn<ReportInfo, Double>  priceColumn;

    @FXML
    TableColumn<ReportInfo, String> nameColumn, telColumn, timeColumn;
    @FXML
    ComboBox statusCombo;

    ObservableList<ReportInfo> lst = FXCollections.observableArrayList();

    public void okHandle(ActionEvent actionEvent) {

    }

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


        reportTable.setItems(lst);
    }

    @FXML
    private void initReport() {

        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = ToDatePicker.getValue();
        if (fromDate.isAfter(toDate)) {
            // warn user
            warningDate.setText("Invalid Date");
            return;
        }
        if(statusCombo.getValue() == null){
            warningDate.setText("Choose Status !");
            return;
        }
        warningDate.setText("");
        lst.clear();
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:ReservationInfoDB.db";
            Connection connection = DriverManager.getConnection(dbURL);
            if (connection != null){

                String query = "select * from ReservationInfos where status='"
                        + statusCombo.getValue() +"' ORDER BY fieldNumber,dateTime" ;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    String[] dateTimeStr = resultSet.getString("dateTime").split(" ");
                    date = dateFormat.parse(dateTimeStr[0]);
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int fieldNumber = resultSet.getInt("fieldNumber");
                    int fieldPrice = resultSet.getInt("fieldPrice");
                    String customerName = resultSet.getString("customerName");
                    String customerTel = resultSet.getString("customerTel");

                    if (localDate.isBefore(fromDate) || localDate.isAfter(toDate))
                        continue;

                    boolean isIn = false;
                    for (ReportInfo info : lst) {
                        if (info.name.equals(customerName) && info.field == fieldNumber) {
                            if (Integer.parseInt(info.time.split(" - ")[1]) == Integer.parseInt(dateTimeStr[1])) {
                                info.time = info.time.split(" - ")[0] + " - " + (Integer.parseInt(dateTimeStr[1]) + 1);
                                info.price += fieldPrice;
                                isIn = true;
                                break;
                            } else if (Integer.parseInt(info.time.split(" - ")[0]) == (Integer.parseInt(dateTimeStr[1]) + 1)) {
                                info.time = dateTimeStr[1] + " - " + info.time.split(" - ")[1];
                                info.price += fieldPrice;
                                isIn = true;
                                break;
                            }
                        }
                    }

                    if (!isIn)
                        lst.add(new ReportInfo(customerName, customerTel, fieldNumber, dateTimeStr[1] + " - " + (Integer.parseInt(dateTimeStr[1]) + 1), fieldPrice));

                }
                connection.close();

                calAmount();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void calAmount() {

        double total = 0;
        for (int i=0; i<lst.size(); i++) {
            total += priceColumn.getCellData(i);
        }
        amountAll.setText(total+"");

    }

    public class ReportInfo{
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
