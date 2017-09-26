package Models;

import java.time.LocalDateTime;

public class ReservationInfo {

    private int id;

    private LocalDateTime dateTime;
    private int fieldNumber;
    private int fieldPrice;
    private String customerName;
    private String customerTel;

    public ReservationInfo(int id, LocalDateTime dateTime,
                           int fieldNumber, int fieldPrice,
                           String customerName, String customerTel) {
        this.id = id;
        this.dateTime = dateTime;
        this.fieldNumber = fieldNumber;
        this.fieldPrice = fieldPrice;
        this.customerName = customerName;
        this.customerTel = customerTel;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public int getFieldPrice() {
        return fieldPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerTel() {
        return customerTel;
    }

}
