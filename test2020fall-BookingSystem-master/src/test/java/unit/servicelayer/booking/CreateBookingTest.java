package unit.servicelayer.booking;

import datalayer.booking.BookingStorage;
import datalayer.customer.CustomerStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import servicelayer.booking.BookingService;
import servicelayer.booking.BookingServiceException;
import servicelayer.booking.BookingServiceImpl;
import servicelayer.customer.CustomerService;
import servicelayer.customer.CustomerServiceException;
import servicelayer.customer.CustomerServiceImpl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("unit")
public class CreateBookingTest {

    private BookingService bookingService;

    // DOC (Depended-on Component)
    private BookingStorage storageMock;


    @BeforeAll
    public void beforeAll(){
        storageMock = mock(BookingStorage.class);
        bookingService = new BookingServiceImpl(storageMock);
    }

    @Test
    public void mustCallStorageWhenCreatingBooking() throws BookingServiceException, SQLException, ParseException {
        // Arrange
        // Act
        var customerId = 1;
        var employeeId = 2;
        var date = "10.10.2020";
        var start = "13:00:00";
        var end = "14:00:00";

        bookingService.createBooking(customerId, employeeId, date, start, end);

        // Assert
        // Can be read like: verify that storageMock was called 1 time on the method
        //   'createCustomer' with an argument whose 'firstname' == firstName and
        //   whose 'lastname' == lastName
        verify(storageMock, times(1))
                .createBooking(
                        argThat(x -> x.customerId == customerId &&
                                x.employeeId == employeeId));
    }
}
