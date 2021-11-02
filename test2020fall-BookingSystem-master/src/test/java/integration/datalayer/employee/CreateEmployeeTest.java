package integration.datalayer.employee;


import com.github.javafaker.Faker;
import datalayer.booking.BookingStorageImpl;
import datalayer.customer.CustomerStorage;
import datalayer.customer.CustomerStorageImpl;
import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import datalayer.employee.EmployeeStorageImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
public class CreateEmployeeTest extends ContainerizedDbIntegrationTest {

    private EmployeeStorage employeeStorage;
    private SimpleDateFormat dateFormatter;

    @BeforeAll
    public void Setup() throws SQLException, ParseException {
        runMigration(3);

        dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

        employeeStorage = new EmployeeStorageImpl(getConnectionString(), "root", getDbPassword());

        var numEmployees = employeeStorage.getEmployees().size();
        if (numEmployees < 10) {
            addFakeEmployees(10 - numEmployees);
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
    public void mustSaveEmployeeInDatabaseWhenCallingCreateEmployee() throws SQLException, ParseException {
        // Arrange
        Faker faker = new Faker();
        Date date = faker.date().between(new Date(), DateUtils.addYears(new Date(), 1));
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        // Act
        employeeStorage.createEmployee(new EmployeeCreation(firstname,
                lastname, dateFormatter.format(date)));

        // Assert
        var employees = employeeStorage.getEmployees();
        assertTrue(
                employees.stream().anyMatch(x ->
                        x.getFirstname().equals(firstname) &&
                                x.getLastname().equals(lastname)));
    }

    @Test
    public void mustReturnLatestId() throws SQLException, ParseException {
        // Arrange
        Faker faker = new Faker();
        Date date = faker.date().between(new Date(), DateUtils.addYears(new Date(), 1));
        // Act
        var id1 = employeeStorage.createEmployee(new EmployeeCreation("a", "b",dateFormatter.format(date)));
        var id2 = employeeStorage.createEmployee(new EmployeeCreation("c", "d",dateFormatter.format(date)));

        // Assert
        assertEquals(1, id2 - id1);
    }
}
