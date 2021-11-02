package integration.datalayer.booking;

import com.github.javafaker.Faker;
import datalayer.booking.BookingStorage;
import datalayer.booking.BookingStorageImpl;
import datalayer.customer.CustomerStorage;
import datalayer.customer.CustomerStorageImpl;
import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import datalayer.employee.EmployeeStorageImpl;
import dto.Booking;
import dto.BookingCreation;
import dto.Customer;
import dto.CustomerCreation;
import integration.ContainerizedDbIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.shaded.org.apache.commons.lang.time.DateUtils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
public class CreateBookingTest extends ContainerizedDbIntegrationTest {
    private BookingStorage bookingStorage;
    private EmployeeStorage employeeStorage;
    private CustomerStorage customerStorage;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @BeforeAll
    public void Setup() throws SQLException, ParseException {
        runMigration(3);

        timeFormatter = new SimpleDateFormat("HH:mm:ss");
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

        employeeStorage = new EmployeeStorageImpl(getConnectionString(), "root", getDbPassword());

        var numEmployees = employeeStorage.getEmployees().size();
        if (numEmployees < 10) {
            addFakeEmployees(10 - numEmployees);
        }

        customerStorage = new CustomerStorageImpl(getConnectionString(), "root", getDbPassword());

        var numCustomers = customerStorage.getCustomers().size();
        if (numCustomers < 10) {
            addFakeCustomers(10 - numCustomers);
        }

        bookingStorage = new BookingStorageImpl(getConnectionString(), "root", getDbPassword());

        var numBookings = bookingStorage.getBookings().size();
        if (numBookings < 10) {
            addFakeBookings(10 - numBookings);
        }
    }

    private void addFakeCustomers(int numCustomers) throws SQLException {
        Faker faker = new Faker();
        for (int i = 0; i < numCustomers; i++) {
            CustomerCreation c = new CustomerCreation(faker.name().firstName(), faker.name().lastName());
            customerStorage.createCustomer(c);
        }

    }

    private void addFakeBookings(int numBookings) throws SQLException, ParseException {
        Faker faker = new Faker();
        for (int i = 0; i < numBookings; i++) {
            Date date = faker.date().between(new Date(), DateUtils.addYears(new Date(), 1));
            BookingCreation b = new BookingCreation(faker.number().numberBetween(1,10),
                    faker.number().numberBetween(1,10),
                    dateFormatter.format(date),
                    timeFormatter.format(date),
                    timeFormatter.format(DateUtils.addHours(date, 1)));
            bookingStorage.createBooking(b);
        }
    }

    private void addFakeEmployees(int numEmployees) throws SQLException, ParseException {
        Faker faker = new Faker();
        for (int i = 0; i < numEmployees; i++) {
            EmployeeCreation e = new EmployeeCreation(faker.name().firstName(), faker.name().lastName(), dateFormatter.format(faker.date().birthday()));
            employeeStorage.createEmployee(e);
        }
    }

    @Test
    public void mustSaveBookingInDatabaseWhenCallingCreateBooking() throws SQLException, ParseException {
        // Arrange
        Faker faker = new Faker();
        Date date = faker.date().between(new Date(), DateUtils.addYears(new Date(), 1));
        // Act
        bookingStorage.createBooking(new BookingCreation(2, 3,
                dateFormatter.format(date),
                timeFormatter.format(date),
                timeFormatter.format(DateUtils.addHours(date,2))));

        // Assert
        var bookings = bookingStorage.getBookings();

        assertTrue(
                bookings.stream().anyMatch(x ->
                        x.getCustomerId() == 2 &&
                                x.getEmployeeId() == 3 &&
                                x.getDate().equals(dateFormatter.format(date))));
    }

    @Test
    public void mustReturnLatestId() throws SQLException, ParseException {
        // Arrange
        Faker faker = new Faker();
        Date date1 = faker.date().between(new Date(), DateUtils.addYears(new Date(), 1));
        Date date2 = faker.date().between(new Date(), DateUtils.addYears(new Date(), 1));

        // Act
        var id1 = bookingStorage.createBooking(new BookingCreation(10, 4,
                dateFormatter.format(date1),
                timeFormatter.format(date1),
                timeFormatter.format(DateUtils.addHours(date1,2))));
        var id2 = bookingStorage.createBooking(new BookingCreation(9, 3,
                dateFormatter.format(date2),
                timeFormatter.format(date2),
                timeFormatter.format(DateUtils.addHours(date2,2))));

        // Assert
        assertEquals(1, id2 - id1);
    }

}
