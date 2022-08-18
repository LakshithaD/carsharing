package carsharing;

import carsharing.dao.*;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    final static String JDBC_DRIVER = "org.h2.Driver";

    final static String DB_URL = "jdbc:h2:file:/home/lakshitha/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/%s";
    private static CompanyDao companyDao;
    private static CarDao carDao;
    private static CustomerDao customerDao;
    private static Scanner scanner;

    public static void main(String[] args) {
        deleteFile();
        scanner = new Scanner(System.in);
        String fileName = databaseFileName(args);
        companyDao = new CompanyDaoImpl(fileName);
        carDao = new CarDaoImpl(fileName);
        customerDao = new CustomerDaoImpl(DB_URL.formatted(fileName), JDBC_DRIVER);
        displayMenu();
    }

    private static String databaseFileName(String[] args) {

        if (args.length == 2) {
            return args[1];
        } else {
            return "db.txt";
        }
    }

    private static void deleteFile() {

        File file = new File("/home/lakshitha/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db");
        for (File tmp: file.listFiles()) {
            boolean deleted = tmp.delete();
        }
    }

    private static void displayMenu () {

        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            String option = scanner.nextLine();
            System.out.println();
            if ("1".equals(option)) {

                while (true) {
                    System.out.println("1. Company list");
                    System.out.println("2. Create a company");
                    System.out.println("0. Back");
                    String subOption = scanner.nextLine();
                    if ("1".equals(subOption)) {
                        showCompanyList();
                    } else if ("2".equals(subOption)) {
                        createCompany();
                    } else if ("0".equals(subOption)) {
                        break;
                    }
                    System.out.println();
                }
            } else if ("2".equals(option)) {
                handleCustomerMenu();
            } else if ("3".equals(option)) {
                createCustomer();
            } else if ("0".equals(option)) {
                break;
            }
            System.out.println();
        }
    }

    private static void showCompanyList() {

        List<Company> companyList = companyDao.getAllCompanies();
        System.out.println();
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose a company:");
            for (int i = 0; i < companyList.size(); i++) {
                System.out.println((i + 1) + ". " + companyList.get(i).getName());
            }
            System.out.println("0. Back");
            String companyId = scanner.nextLine();
            if (Integer.parseInt(companyId) > companyList.size() || Integer.parseInt(companyId) == 0) {
                return;
            }
            Company selectedCompany = companyList.get(Integer.parseInt(companyId) - 1);
            System.out.println();
            while (true) {
                System.out.println("%s company:".formatted(selectedCompany.getName()));
                System.out.println("1. Car list");
                System.out.println("2. Create a car");
                System.out.println("0. Back");
                String option = scanner.nextLine();
                if ("1".equals(option)) {
                    List<Car> cars = carDao.getCarsByCompanyId(selectedCompany.getId());
                    showCompanyCarList(selectedCompany.getName(),cars);
                } else if ("2".equals(option)) {
                    createCar(selectedCompany.getId());
                } else  if ("0".equals(option)) {
                    break;
                }
                System.out.println();
            }
        }
    }

    private static void createCompany() {

        System.out.println();
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        companyDao.createCompany(companyName);
        System.out.println("The company was created!");
    }

    private static void showCompanyCarList(String company, List<Car> cars) {
        System.out.println("%s cars".formatted(company));
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            for (int i = 0; i <cars.size(); i++) {
                Car car = cars.get(i);
                System.out.println("%d. %s".formatted(i + 1, car.getName()));
            }
            System.out.println("0. Back");
        }
    }

    private static void createCar(Integer companyId) {

        System.out.println();
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        Car car = new Car();
        car.setCompanyId(companyId);
        car.setName(carName);
        carDao.createCar(car);
        System.out.println("The car was added!");
    }

    private static void createCustomer() {
        System.out.println();
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerDao.createCustomer(name);
        System.out.println("The customer was added!");
    }

    private static void handleCustomerMenu() {
        System.out.println("Choose a customer:");
        List<Customer> customers = customerDao.getAllCustomer();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            for (int i = 0; i < customers.size(); i++) {
                System.out.println("%d. %s".formatted(i + 1, customers.get(i).getName()));
            }
            System.out.println("0. Back");
            String input = scanner.nextLine();
            if ("0".equals(input)) {
                return;
            } else {
                while (true) {
                    System.out.println("1. Rent a car");
                    System.out.println("2. Return a rented car");
                    System.out.println("3. My rented car");
                    System.out.println("0. Back");
                    String optionTwo = scanner.nextLine();
                    Customer selectedCustomer = customers.get(Integer.parseInt(input) - 1);
                    if ("0".equals(optionTwo)) {
                        break;
                    } else if ("1".equals(optionTwo)) {
                        rentCar(selectedCustomer);
                    } else if ("2".equals(optionTwo)) {
                        returnRentedCar(selectedCustomer);
                    } else if ("3".equals(optionTwo)) {
                        printRentedCars(selectedCustomer);
                    }
                }
            }
        }
    }

    private static void printRentedCars(Customer customer) {

        if (customer.getRentCarId() == null) {
            System.out.println("You didn't rent a car!");
        } else {
            Car car = carDao.getCarById(customer.getRentCarId());
            System.out.println("Your rented car:");
            System.out.println(car.getName());
            System.out.println("Company:");
            Company company = companyDao.getCompanyById(car.getCompanyId());
            System.out.println(company.getName());
        }
    }

    private static void returnRentedCar(Customer customer) {
        if (customer.getRentCarId() == null) {
            System.out.println("You didn't rent a car!");
        } else if (customer.getRentCarId() == -1) {
            System.out.println("You've returned a rented car!");
        } else {
            customerDao.updateRentedCarId(customer.getId(), null);
            customer.setRentCarId(-1);
            System.out.println("You've returned a rented car!");
        }
    }

    private static void rentCar(Customer customer) {

        if (customer.getRentCarId() != null) {
            System.out.println("You've already rented a car!");
            return;
        }
        System.out.println("Choose a company:");
        List<Company> companies = companyDao.getAllCompanies();
        for (int i = 0; i < companies.size(); i++) {
            System.out.println("%d. %s".formatted(i + 1, companies.get(i).getName()));
        }
        System.out.println("0. Back");
        String input = scanner.nextLine();
        if ("0".equals(input)) {
            return;
        }
        Company selected = companies.get(Integer.parseInt(input) - 1);
        List<Car> cars = carDao.getCarsByCompanyId(selected.getId());
        if (cars.isEmpty()) {
            System.out.println("No available cars in the %s company".formatted(selected.getName()));
        } else {
            if (customer.getRentCarId() != null) {
                System.out.println("You've already rented a car!");
            } else {
                System.out.println("Choose a car:");
                List<Integer> rentedCarIds = customerDao.getAllCustomer().stream()
                        .map(Customer::getRentCarId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                List<Car> availableCars = cars.stream()
                        .filter(car -> !rentedCarIds.contains(car.getId()))
                        .collect(Collectors.toList());
                for (int i = 0; i < availableCars.size(); i++) {
                    System.out.println("%d. %s".formatted(i + 1, availableCars.get(i).getName()));
                }
                System.out.println("0. Back");
                String selection = scanner.nextLine();
                if ("0".equals(selection)) {
                    return;
                } else {
                    Car selectedCar = availableCars.get(Integer.parseInt(selection) - 1);
                    customer.setRentCarId(selectedCar.getId());
                    customerDao.updateRentedCarId(customer.getId(), selectedCar.getId());
                    System.out.println("You rented '%s'".formatted(selectedCar.getName()));
                }
            }
        }
        System.out.println();
    }
 }