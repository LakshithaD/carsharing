package carsharing;

public class Customer {

    private Integer id;
    private String name;
    private Integer rentCarId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRentCarId() {
        return rentCarId;
    }

    public void setRentCarId(Integer rentCarId) {
        this.rentCarId = rentCarId;
    }
}
