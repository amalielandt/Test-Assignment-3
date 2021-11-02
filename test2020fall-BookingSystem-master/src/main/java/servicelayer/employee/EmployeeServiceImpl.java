package servicelayer.employee;

import datalayer.employee.EmployeeCreation;
import datalayer.employee.EmployeeStorage;
import dto.Employee;
import servicelayer.customer.CustomerServiceException;

import java.sql.SQLException;
import java.text.ParseException;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeStorage employeeStorage;
    public EmployeeServiceImpl(EmployeeStorage employeeStorage) {
        this.employeeStorage = employeeStorage;
    }

    @Override
    public int createEmployee(EmployeeCreation employee) throws EmployeeServiceException {
        try {
            return employeeStorage.createEmployee(employee);
        } catch (SQLException | ParseException throwables ) {
            throw new EmployeeServiceException(throwables.getMessage());
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) throws EmployeeServiceException {
        try {
            return employeeStorage.getEmployeeById(employeeId);
        } catch (SQLException throwables) {
            throw new EmployeeServiceException(throwables.getMessage());
        }
    }

}
