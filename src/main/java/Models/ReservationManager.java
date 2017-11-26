package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author INT
 */

public class ReservationManager {

    ArrayList<ReservationInfo> reservationInfos = new ArrayList<ReservationInfo>();

    public void addReservation(ReservationInfo reservationInfo) {
        this.reservationInfos.add(reservationInfo);
    }

    public ReservationInfo getReservation(LocalDateTime date, int fieldNumber) {
        for (ReservationInfo info : reservationInfos) {
            if (date.equals(info.getDateTime()) && fieldNumber == info.getFieldNumber()) {
                return info;
            }
        }
        return null;
    }

    public ArrayList<ReservationInfo> getReservations() {
        return this.reservationInfos;
    }

    public void deleteReservation(int id) {
        for (ReservationInfo re : reservationInfos) {
            if (re.getId() == id) {
                this.reservationInfos.remove(re);
                break;
            }
        }
    }

}
