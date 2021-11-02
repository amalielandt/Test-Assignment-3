package datalayer.booking;

import dto.Booking;
import dto.BookingCreation;
import dto.Customer;
import dto.Employee;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookingStorageImpl implements BookingStorage {

    private String connectionString;
    private String username, password;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    public BookingStorageImpl(String conStr, String user, String pass) {
        connectionString = conStr;
        username = user;
        password = pass;
        timeFormatter = new SimpleDateFormat("HH:mm:ss");
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public List<Booking> getBookings() throws SQLException {
        try (var con = getConnection();
             var stmt = con.createStatement()) {
            var results = new ArrayList<Booking>();

            try (ResultSet resultSet = stmt.executeQuery("select ID, customerId, employeeId, date, start, end from Bookings")) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    int customerId = resultSet.getInt("customerId");
                    int employeeId = resultSet.getInt("employeeId");
                    String date = dateFormatter.format(resultSet.getDate("date"));
                    String start = timeFormatter.format(resultSet.getTime("start"));
                    String end = timeFormatter.format(resultSet.getTime("end"));

                    Booking b = new Booking(id, customerId, employeeId, date, start, end);
                    results.add(b);
                }
            }

            return results;
        }
    }

    @Override
    public int createBooking(BookingCreation bookingToCreate) throws SQLException, ParseException {
        var sql = "insert into Bookings(customerId, employeeId, date, start, end) values (?,?,?,?,?)";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, bookingToCreate.getCustomerId());
            stmt.setInt(2, bookingToCreate.getEmployeeId());
            stmt.setDate(3, new Date(dateFormatter.parse(bookingToCreate.getDate()).getTime()));
            stmt.setTime(4, new Time(timeFormatter.parse(bookingToCreate.getStart()).getTime()));
            stmt.setTime(5, new Time(timeFormatter.parse(bookingToCreate.getEnd()).getTime()));

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
    public Collection<Booking> getBookingsForCustomer(int customerId) throws SQLException {
        var sql = "select * from Bookings where customerId = ?";
        Collection<Booking> bookings = new ArrayList<>();

        try (var con = getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            try (var resultSet = stmt.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    int customer = resultSet.getInt("customerId");
                    int employee = resultSet.getInt("employeeId");
                    String date = dateFormatter.format(resultSet.getDate("date"));
                    String start = timeFormatter.format(resultSet.getTime("start"));
                    String end = timeFormatter.format(resultSet.getTime("end"));

                    Booking b = new Booking(id, customer, employee, date, start, end);
                    bookings.add(b);
                }
                return bookings;
            }
        }
    }

    @Override
    public Collection<Booking> getBookingsForEmployee(int employeeId) throws SQLException {
        var sql = "select * from Bookings where employeeId = ?";
        Collection<Booking> bookings = new ArrayList<>();

        try (var con = getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);

            try (var resultSet = stmt.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    int customer = resultSet.getInt("customerId");
                    int employee = resultSet.getInt("employeeId");
                    String date = dateFormatter.format(resultSet.getDate("date"));
                    String start = timeFormatter.format(resultSet.getTime("start"));
                    String end = timeFormatter.format(resultSet.getTime("end"));

                    Booking b = new Booking(id, customer, employee, date, start, end);
                    bookings.add(b);
                }
                return bookings;
            }
        }
    }
}
