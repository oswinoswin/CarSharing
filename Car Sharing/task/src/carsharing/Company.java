package carsharing;

public class Company {
    String name;
    int id;

    public Company(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
