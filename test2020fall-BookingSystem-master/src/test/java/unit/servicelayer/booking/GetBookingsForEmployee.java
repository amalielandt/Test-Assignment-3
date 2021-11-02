package unit.servicelayer.booking;

import datalayer.booking.BookingStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import servicelayer.booking.BookingService;
import servicelayer.booking.BookingServiceException;
import servicelayer.booking.BookingServiceImpl;

import java.sql.SQLException;
import java.text.ParseException;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("unit")
public class GetBookingsForEmployee {

    private BookingService bookingService;

    // DOC (Depended-on Component)
    private BookingStorage storageMock;


    @BeforeAll
    public void beforeAll(){
        storageMock = mock(BookingStorage.class);
        bookingService = new BookingServiceImpl(storageMock);
    }

    @Test
    public void mustCallStorageWhenRetrievingBookingsForEmployee() throws BookingServiceException, SQLException, ParseException {

        var employeeId = 2;

        bookingService.getBookingsForEmployee(employeeId);

        // Assert
        // Can be read like: verify that storageMock was called 1 time on the method
        //   'createCustomer' with an argument whose 'firstname' == firstName and
        //   whose 'lastname' == lastName
        verify(storageMock, times(1))
                .getBookingsForEmployee(eq(employeeId));
    }


}
