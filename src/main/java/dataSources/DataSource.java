package dataSources;

import models.ReservationInfo;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * @author INT
 */

public interface DataSource {

    ArrayList<ReservationInfo> loadReservationInfo();
    int getLastID();
    void insertReservationInfo(ArrayList<ReservationInfo> info);
    void setReservationStatus(ArrayList<Integer> ids, String status);
    ArrayList<ReservationInfo> loadReportInfo(String status);

}
