package integration.servicelayer.booking;

import datalayer.booking.BookingStorage;
import datalayer.booking.BookingStorageImpl;
import datalayer.customer.CustomerStorage;
import datalayer.customer.CustomerStorageImpl;
import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import datalayer.employee.EmployeeStorageImpl;
import integration.ContainerizedDbIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import servicelayer.booking.BookingService;
import servicelayer.booking.BookingServiceException;
import servicelayer.booking.BookingServiceImpl;
import servicelayer.customer.CustomerService;
import servicelayer.customer.CustomerServiceException;
import servicelayer.customer.CustomerServiceImpl;
import servicelayer.employee.EmployeeService;
import servicelayer.employee.EmployeeServiceException;
import servicelayer.employee.EmployeeServiceImpl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SvcBookingTest extends ContainerizedDbIntegrationTest {

    private BookingService svc;
    private BookingStorage storage;

    private EmployeeService employeeSvc;
    private EmployeeStorage employeeStorage;

    private CustomerService customerSvc;
    private CustomerStorage customerStorage;

    @BeforeAll
    public void setup() {
        runMigration(3);
        storage = new BookingStorageImpl(getConnectionString(),"root", getDbPassword());
        svc = new BookingServiceImpl(storage);

        employeeStorage = new EmployeeStorageImpl(getConnectionString(),"root", getDbPassword());
        employeeSvc = new EmployeeServiceImpl(employeeStorage);

        customerStorage = new CustomerStorageImpl(getConnectionString(),"root", getDbPassword());
        customerSvc = new CustomerServiceImpl(customerStorage);
    }

    @Test
    public void mustSaveBookingToDatabaseWhenCallingCreateBooking() throws BookingServiceException, SQLException, EmployeeServiceException, CustomerServiceException, ParseException {
        // Arrange
        var firstName = "John";
        var lastName = "Johnson";
        var bday = "03.04.1996";

        int employeeId = employeeSvc.createEmployee(new EmployeeCreation(firstName, lastName, bday));

        var customerBday = new Date(1239821l);
        int customerId = customerSvc.createCustomer(firstName, lastName, customerBday);

        var date = "10.10.2020";
        var start = "13:00:00";
        var end = "14:00:00";

        int id = svc.createBooking(customerId, employeeId, date, start, end);
        var createdBookings = storage.getBookingsForCustomer(customerId);

        // Assert
        assertTrue(
                createdBookings.stream().anyMatch(x ->
                        x.getCustomerId() == customerId &&
                                x.getEmployeeId() == employeeId &&
                                x.getId() == id &&
                                x.getDate().equals(date)));
    }

}
