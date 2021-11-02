package datalayer.booking;

import com.github.javafaker.Dog;
import dto.Booking;
import dto.BookingCreation;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public interface BookingStorage {
    List<Booking> getBookings() throws SQLException;
    int createBooking(BookingCreation b) throws SQLException, ParseException;
    Collection<Booking> getBookingsForCustomer(int customerId) throws SQLException;
    Collection<Booking> getBookingsForEmployee(int employeeId) throws SQLException;
}
