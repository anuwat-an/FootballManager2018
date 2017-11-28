package dataSources;

import models.ReservationInfo;
import library.ToolsLibrary;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author INT
 */

public class SQLiteSource implements DataSource {

    private Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:ReservationInfoDB.db";
            connection = DriverManager.getConnection(dbURL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public int getLastID() {
        int id = 0;
        try {
            Connection connection = connect();
            if (connection != null) {
                String query = "select max(id) from ReservationInfo";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                id = resultSet.getInt(1);

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void insertReservationInfo(ArrayList<ReservationInfo> info) {
        try {
            Connection connection = connect();
            if (connection != null) {
                for (ReservationInfo reservationInfo : info) {
                    String dateTimeStr = reservationInfo.getDateTime().getDayOfMonth() + "/" +
                            reservationInfo.getDateTime().getMonthValue() + "/" +
                            reservationInfo.getDateTime().getYear() + " " +
                            reservationInfo.getDateTime().getHour();

                    String query = "insert into ReservationInfo (dateTime,fieldNumber,fieldPrice,customerName,customerTel,status) " +
                            "values ('" + dateTimeStr + "'," +
                            reservationInfo.getFieldNumber() + "," +
                            reservationInfo.getFieldPrice() + "," +
                            "'" + reservationInfo.getCustomerName() + "'," +
                            "'" + reservationInfo.getCustomerTel() + "', 'NOTPAID')";
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setReservationStatus(ArrayList<Integer> ids, String status) {
        try {
            Connection connection = connect();
            if (connection != null) {
                for (int id : ids) {
                    String query = "update ReservationInfo set status='" + status + "' where id=" + id;
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ReservationInfo> loadReservationInfo(String status) {
        ArrayList<ReservationInfo> info = new ArrayList<>();

        try {
            Connection connection = connect();
            if (connection != null) {
                String query = "select * from ReservationInfo where status='" + status + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String dateTimeStr = resultSet.getString("dateTime");
                    int fieldNumber = resultSet.getInt("fieldNumber");
                    int fieldPrice = resultSet.getInt("fieldPrice");
                    String customerName = resultSet.getString("customerName");
                    String customerTel = resultSet.getString("customerTel");
                    Date date = ToolsLibrary.dateTimeFormat.parse(dateTimeStr);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                    ReservationInfo reservationInfo = new ReservationInfo(id, localDateTime, fieldNumber, fieldPrice, customerName, customerTel);

                    info.add(reservationInfo);
                }

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return info;
    }

}
