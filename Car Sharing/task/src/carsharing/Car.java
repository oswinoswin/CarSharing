package carsharing;

public class Car {
    int id;
    String name;
    int companyId;

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public Car(int id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }
}
