package library;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author INT
 */

public class Library {

    public static final DateFormat dateTimeFormat;
    public static final HashMap<String, String> reservationStatus;
    static {
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);
        reservationStatus = new HashMap<>();
        reservationStatus.put("Paid", "PAID");
        reservationStatus.put("Not Paid", "NOTPAID");
        reservationStatus.put("Canceled", "CANCEL");
    }

}
