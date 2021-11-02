package unit.servicelayer.employee;

import datalayer.customer.CustomerStorage;
import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import dto.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import servicelayer.customer.CustomerService;
import servicelayer.customer.CustomerServiceException;
import servicelayer.customer.CustomerServiceImpl;
import servicelayer.employee.EmployeeService;
import servicelayer.employee.EmployeeServiceException;
import servicelayer.employee.EmployeeServiceImpl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("unit")
public class CreateEmployeeTest {

    // SUT (System Under Test)
    private EmployeeService employeeService;

    // DOC (Depended-on Component)
    private EmployeeStorage storageMock;


    @BeforeAll
    public void beforeAll(){
        storageMock = mock(EmployeeStorage.class);
        employeeService = new EmployeeServiceImpl(storageMock);
    }

    @Test
    public void mustCallStorageWhenCreatingEmployee() throws EmployeeServiceException, SQLException, ParseException {
        // Arrange
        // Act
        var firstName = "a";
        var lastName = "b";
        var birthdate = "08.04.1995";
        EmployeeCreation employee = new EmployeeCreation(firstName, lastName, birthdate);

        employeeService.createEmployee(employee);

        // Assert
        // Can be read like: verify that storageMock was called 1 time on the method
        //   'createCustomer' with an argument whose 'firstname' == firstName and
        //   whose 'lastname' == lastName
        verify(storageMock, times(1))
                .createEmployee(eq(employee));
    }
}
