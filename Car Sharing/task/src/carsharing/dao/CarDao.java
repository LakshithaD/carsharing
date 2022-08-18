package carsharing.dao;

import carsharing.Car;

import java.util.List;

public interface CarDao {

    List<Car> getCarsByCompanyId(Integer companyId);

    void createCar(Car car);

    Car getCarById(Integer id);
}
