package dto;

import java.sql.Time;
import java.util.Date;

public class BookingCreation {

    public final int customerId, employeeId;
    public final String date, start, end;

    public BookingCreation(int customerId, int employeeId, String date, String start, String end) {
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.date = date;
        this.start = start;
        this.end = end;

    }

    public int getCustomerId() { return customerId; }

    public int getEmployeeId() { return employeeId; }

    public String getDate() { return date; }

    public String getStart() { return start; }

    public String getEnd() { return end; }
}

