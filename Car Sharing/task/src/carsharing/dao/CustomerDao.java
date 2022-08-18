package carsharing.dao;

import carsharing.Customer;

import java.util.List;

public interface CustomerDao {

    void createCustomer(String customer);
    List<Customer> getAllCustomer();
    void updateRentedCarId(Integer customerId, Integer rentedCarId);
}
