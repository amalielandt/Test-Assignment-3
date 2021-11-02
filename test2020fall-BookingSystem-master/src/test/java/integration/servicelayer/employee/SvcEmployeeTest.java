package integration.servicelayer.employee;

import datalayer.customer.CustomerStorageImpl;
import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import datalayer.employee.EmployeeStorageImpl;
import integration.ContainerizedDbIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import servicelayer.customer.CustomerServiceException;
import servicelayer.customer.CustomerServiceImpl;
import servicelayer.employee.EmployeeService;
import servicelayer.employee.EmployeeServiceException;
import servicelayer.employee.EmployeeServiceImpl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SvcEmployeeTest extends ContainerizedDbIntegrationTest {
    private EmployeeService svc;
    private EmployeeStorage storage;

    @BeforeAll
    public void setup() {
        runMigration(3);
        storage = new EmployeeStorageImpl(getConnectionString(),"root", getDbPassword());
        svc = new EmployeeServiceImpl(storage);
    }

    @Test
    public void mustSaveEmployeeToDatabaseWhenCallingCreateEmployee() throws EmployeeServiceException, SQLException {
        // Arrange
        var firstName = "John";
        var lastName = "Johnson";
        var bday = "03.04.1996";
        EmployeeCreation employee = new EmployeeCreation(firstName, lastName, bday);

        int id = svc.createEmployee(employee);

        // Act
        var createdEmployee = storage.getEmployeeById(id);

        // Assert
        assertEquals(firstName, createdEmployee.getFirstname());
        assertEquals(lastName, createdEmployee.getLastname());
    }
}
