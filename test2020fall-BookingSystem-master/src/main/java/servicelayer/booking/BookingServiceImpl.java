package servicelayer.booking;

import datalayer.booking.BookingStorage;
import datalayer.customer.CustomerStorage;
import dto.Booking;
import dto.BookingCreation;
import dto.CustomerCreation;
import servicelayer.customer.CustomerServiceException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;

public class BookingServiceImpl implements BookingService {

    private BookingStorage bookingStorage;

    public BookingServiceImpl(BookingStorage bookingStorage) {

        this.bookingStorage = bookingStorage;
    }

    @Override
    public int createBooking(int customerId, int employeeId, String date, String start, String end) throws ParseException, BookingServiceException {
        try {
            return bookingStorage.createBooking(new BookingCreation(customerId, employeeId, date, start, end));
        } catch (SQLException throwables) {
            throw new BookingServiceException(throwables.getMessage());
        }
    }

    @Override
    public Collection<Booking> getBookingsForCustomer(int customerId) throws BookingServiceException {
        try {
            return bookingStorage.getBookingsForCustomer(customerId);
        } catch (SQLException throwables) {
            throw new BookingServiceException(throwables.getMessage());
        }
    }

    @Override
    public Collection<Booking> getBookingsForEmployee(int employeeId) throws BookingServiceException {
        try {
            return bookingStorage.getBookingsForEmployee(employeeId);
        }catch (SQLException throwables) {
            throw new BookingServiceException(throwables.getMessage());
        }
    }
}
