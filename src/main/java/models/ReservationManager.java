package models;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author INT
 */

public class ReservationManager {

    ArrayList<ReservationInfo> reservationInfo = new ArrayList<>();

    public void addReservation(ReservationInfo reservationInfo) {
        this.reservationInfo.add(reservationInfo);
    }

    public ReservationInfo getReservation(LocalDateTime date, int fieldNumber) {
        for (ReservationInfo info : reservationInfo) {
            if (date.equals(info.getDateTime()) && fieldNumber == info.getFieldNumber()) {
                return info;
            }
        }
        return null;
    }

    public void deleteReservation(int id) {
        for (ReservationInfo re : reservationInfo) {
            if (re.getId() == id) {
                this.reservationInfo.remove(re);
                break;
            }
        }
    }

    public ArrayList<ReservationInfo> getReservationInfo() {
        return this.reservationInfo;
    }

    public void setReservationInfo(ArrayList<ReservationInfo> info) {
        this.reservationInfo = info;
    }

}
