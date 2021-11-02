package integration.datalayer.employee;

import com.github.javafaker.Faker;
import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import datalayer.employee.EmployeeStorageImpl;
import dto.Employee;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
public class GetEmployeeByIdTest extends ContainerizedDbIntegrationTest {

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
    public void mustRetrieveEmployeeFromDatabaseWhenCallingGetEmployeeById() throws SQLException, ParseException {
        // Arrange
        Faker faker = new Faker();
        Date date = faker.date().between(new Date(), DateUtils.addYears(new Date(), 1));
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        // Act
        int id = employeeStorage.createEmployee(new EmployeeCreation(firstname,
                lastname, dateFormatter.format(date)));

        // Assert

        Employee employee = employeeStorage.getEmployeeById(id);

        assertTrue(employee.getFirstname().equals(firstname) &&
                                employee.getLastname().equals(lastname));
    }
}
