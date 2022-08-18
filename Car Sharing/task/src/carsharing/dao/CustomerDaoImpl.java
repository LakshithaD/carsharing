package carsharing.dao;

import carsharing.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:file:/home/lakshitha/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/%s";

    final String dbUrl;
    final String driver;


    public CustomerDaoImpl(String dbUrl, String driver) {
        try {
            this.driver = driver;
            this.dbUrl = dbUrl;
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCustomer(String name) {

        Connection connection = null;
        PreparedStatement statement = null;
        try {

            connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(true);
            statement = connection.prepareStatement("INSERT INTO CUSTOMER (name) VALUES (?)");
            statement.setString(1, name);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public List<Customer> getAllCustomer() {

        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER ORDER BY ID ASC");
            List<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setRentCarId(resultSet.getInt("RENTED_CAR_ID") == 0 ? null : resultSet.getInt("RENTED_CAR_ID"));
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRentedCarId(Integer customerId, Integer rentedCarId) {
        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ? where id = ?")){
            connection.setAutoCommit(true);
            if (rentedCarId == null) {
                statement.setNull(1, JDBCType.INTEGER.getVendorTypeNumber());
            } else {
                statement.setInt(1, rentedCarId);
            }
            statement.setInt(2, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
