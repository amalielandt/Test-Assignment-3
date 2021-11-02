package dto;

import java.util.Date;

public class Booking {

    public final int id, customerId, employeeId;
    public final String date, start, end;

    public Booking(int id, int customerId, int employeeId, String date, String start, String end) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getDate() {
        return date;
    }
}
