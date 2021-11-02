package unit.servicelayer.employee;

import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import servicelayer.employee.EmployeeService;
import servicelayer.employee.EmployeeServiceException;
import servicelayer.employee.EmployeeServiceImpl;

import java.sql.SQLException;
import java.util.Date;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("unit")
public class GetEmployeeByIdTest {

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
    public void mustCallStorageWhenRetrievingEmployeeById() throws EmployeeServiceException, SQLException {
        // Arrange
        // Act
        var employeeId = 1;

        employeeService.getEmployeeById(employeeId);

        // Assert
        // Can be read like: verify that storageMock was called 1 time on the method
        //   'createCustomer' with an argument whose 'firstname' == firstName and
        //   whose 'lastname' == lastName
        verify(storageMock, times(1))
                .getEmployeeById(eq(employeeId));
    }
}
