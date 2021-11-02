package servicelayer.employee;

import datalayer.employee.EmployeeCreation;
import dto.Employee;

import java.sql.SQLException;
import java.text.ParseException;

public interface EmployeeService {
    int createEmployee(EmployeeCreation employee) throws EmployeeServiceException;

    Employee getEmployeeById(int employeeId) throws EmployeeServiceException;
}
