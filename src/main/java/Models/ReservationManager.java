package Models;

import java.util.ArrayList;

public class ReservationManager {

    ArrayList<ReservationInfo> reservationInfos = new ArrayList<ReservationInfo>();

    public void addReservation(ReservationInfo reservationInfo) {
        this.reservationInfos.add(reservationInfo);
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
