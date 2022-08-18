package carsharing.dao;

import carsharing.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {

    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:file:/home/lakshitha/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/%s";

    String databaseFileName;

    public CompanyDaoImpl(String databaseFileName) {

        try {
            Class.forName(JDBC_DRIVER);
            this.databaseFileName = databaseFileName;
            createTables();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Company> getAllCompanies() {

        Connection connection;
        try {
            connection = DriverManager.getConnection(String.format(DB_URL, databaseFileName));
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM company");
            List<Company> companies = new ArrayList<>();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Company company = new Company(id, name);
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTables() {

        try (Connection  connection = DriverManager.getConnection(String.format(DB_URL, databaseFileName));
             Statement statement= connection.createStatement()){
            connection.setAutoCommit(true);
            String sql =  "CREATE TABLE IF NOT EXISTS COMPANY (\n" +
                    "    ID INTEGER NOT NULL AUTO_INCREMENT primary key,\n" +
                    "    NAME VARCHAR(255) NOT NULL UNIQUE \n" +
                    ")";
            statement.executeUpdate(sql);

            String createCarSql = "CREATE TABLE IF NOT EXISTS CAR (\n" +
                    "    ID INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    NAME VARCHAR(50) UNIQUE NOT NULL,\n" +
                    "    COMPANY_ID INT NOT NULL,\n" +
                    "    CONSTRAINT FK_CAR_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)\n" +
                    ")";
            statement.executeUpdate(createCarSql);

            String createCustomerSql = "CREATE TABLE IF NOT EXISTS CUSTOMER (\n" +
                    "    id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    name VARCHAR(50) UNIQUE NOT NULL,\n" +
                    "    RENTED_CAR_ID INT,\n" +
                    "    CONSTRAINT fk_customer_car FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)\n" +
                    ")";
            statement.executeUpdate(createCustomerSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCompany(String companyName) {
        try (Connection connection = DriverManager.getConnection(String.format(DB_URL, databaseFileName));
             PreparedStatement statement = connection.prepareStatement("insert into company (name) values(?)");) {
            connection.setAutoCommit(true);
            statement.setString(1, companyName);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException();
        }
    }

    @Override
    public Company getCompanyById(Integer companyId) {

        try (Connection connection = DriverManager.getConnection(String.format(DB_URL, databaseFileName));
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM COMPANY WHERE ID = ?")) {
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer id = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                Company company = new Company(id, name);
                return company;
            }
            return new Company();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException();
        }
    }
}
