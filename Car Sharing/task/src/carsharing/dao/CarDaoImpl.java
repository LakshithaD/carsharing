package carsharing.dao;

import carsharing.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:file:/home/lakshitha/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/%s";
    String databaseFileName;

    public CarDaoImpl(String databaseFileName) {
        try {
            Class.forName(JDBC_DRIVER);
            this.databaseFileName = databaseFileName;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getCarsByCompanyId(Integer companyId) {

        List<Car> cars = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL.formatted(databaseFileName));
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");){
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int compId = resultSet.getInt("COMPANY_ID");
                Car car = new Car(id, name, compId);
                cars.add(car);
            }

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return cars;
    }

    @Override
    public void createCar(Car car) {
        try (Connection connection = DriverManager.getConnection(DB_URL.formatted(databaseFileName));
             PreparedStatement statement = connection.prepareStatement("INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)");) {
            connection.setAutoCommit(true);
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompanyId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public Car getCarById(Integer id) {

        try (Connection connection = DriverManager.getConnection(DB_URL.formatted(databaseFileName));
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR WHERE ID = ?;")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setName(resultSet.getString("name"));
                car.setCompanyId(resultSet.getInt("COMPANY_ID"));
                return car;
            }
            return new Car();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
