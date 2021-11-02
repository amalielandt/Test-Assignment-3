package datalayer.employee;

import dto.Employee;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public interface EmployeeStorage {
    public Collection<Employee> getEmployees() throws SQLException;
    public int createEmployee(EmployeeCreation employeeToCreate) throws SQLException, ParseException;
    public Employee getEmployeeById(int employeeId) throws SQLException;
}
