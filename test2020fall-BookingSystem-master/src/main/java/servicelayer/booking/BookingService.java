package servicelayer.booking;

import dto.Booking;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;

public interface BookingService {

    int createBooking(int customerId, int employeeId, String date, String start, String end) throws ParseException, BookingServiceException;

    Collection<Booking> getBookingsForCustomer(int customerId) throws BookingServiceException;

    Collection<Booking> getBookingsForEmployee(int employeeId) throws BookingServiceException;
}
