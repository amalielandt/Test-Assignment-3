package datalayer.employee;

import dto.Customer;
import dto.Employee;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class EmployeeStorageImpl implements EmployeeStorage {
    private String username, password;
    private String connectionString;
    private SimpleDateFormat dateFormatter;

    public EmployeeStorageImpl(String conStr, String user, String pass) {
        connectionString = conStr;
        username = user;
        password = pass;
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public Collection<Employee> getEmployees() throws SQLException {
        try (var con = getConnection();
             var stmt = con.createStatement()) {
            var results = new ArrayList<Employee>();

            try (ResultSet resultSet = stmt.executeQuery("select ID, firstname, lastname, birthdate from Employees")) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String firstname = resultSet.getString("firstname");
                    String lastname = resultSet.getString("lastname");
                    String birthdate = dateFormatter.format(resultSet.getDate("birthdate"));

                    Employee employee = new Employee(id, firstname, lastname, birthdate);
                    results.add(employee);
                }
            }

            return results;
        }
    }

    @Override
    public int createEmployee(EmployeeCreation employeeToCreate) throws SQLException, ParseException {
        var sql = "insert into Employees(firstname, lastname, birthdate) values (?, ?, ?)";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employeeToCreate.getFirstname());
            stmt.setString(2, employeeToCreate.getLastname());
            stmt.setDate(3, new Date(dateFormatter.parse(employeeToCreate.getBirthdate()).getTime()));

            stmt.executeUpdate();

            // get the newly created id
            try (var resultSet = stmt.getGeneratedKeys()) {
                resultSet.next();
                int newId = resultSet.getInt(1);
                return newId;
            }
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) throws SQLException {
        var sql = "select ID, firstname, lastname, birthdate from Employees where id = ?";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);

            try (var resultSet = stmt.executeQuery()) {
                if (resultSet.next()){
                    var id = resultSet.getInt("ID");
                    var firstname = resultSet.getString("firstname");
                    var lastname = resultSet.getString("lastname");
                    var birthdate = dateFormatter.format(resultSet.getDate("birthdate"));
                    return new Employee(id, firstname, lastname, birthdate);
                }
                return null;
            }
        }
    }
}
