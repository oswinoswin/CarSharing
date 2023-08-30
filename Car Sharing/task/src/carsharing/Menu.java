package carsharing;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    H2CompanyDAO companyDAO;
    H2CarDAO carDAO;

    public Menu(H2CompanyDAO companyDAO, H2CarDAO carDAO) {
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
    }

    public void show() throws SQLException {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while (!exit){
            System.out.println("1. Log in as a manager\n" +
                    "0. Exit\n");
            int input = scanner.nextInt();
            switch (input) {
                case 1 ->
                    managerMenu();
                case 0 ->
                    exit = true;
            }
        }
        carDAO.cleanup();
        companyDAO.cleanup();
    }

    private void managerMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean back = false;
        while (!back) {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back\n");
            int input = scanner.nextInt();
            switch (input){
                case 1 -> companyList();
                case 2 -> createCompany();
                case 0 -> back = true;
            }
        }

    }

    private void createCompany() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        try {
            companyDAO.add(companyName);
            System.out.println("The company was created!\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void companyList() {
        try {
            List<Company> companyList = companyDAO.findAll();
            if (companyList.isEmpty()) {
                System.out.println("The company list is empty!\n");
            } else {
                Scanner scanner = new Scanner(System.in);
                while (true){
                    System.out.println("Choose a company:");
                    companyList.forEach(System.out::println);
                    System.out.println("0. Back\n");
                    int input = scanner.nextInt();
                    if (input == 0 ) break;
                    else {
                        Company company = companyList.get(input - 1);

                        companyMenu(company);
                    }
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void companyMenu(Company company) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean back = false;
        System.out.println("'" + company.name + "' company:");
        while (!back) {

            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back\n");

            int input = scanner.nextInt();
            switch (input) {
                case 1 -> {
                    List<Car> cars = carDAO.companyCars(company.id);
                    if (cars.isEmpty()) System.out.println("The car list is empty!\n");
                    else {
                        for (int i = 0; i < cars.size(); i++){
                            System.out.println((i + 1) + ". " + cars.get(i).name);
                        }
                        System.out.println();
                    }
                }
                case 2 -> {
                    addCar(company);
//                    System.out.println("Enter the car name:");
//                    scanner.nextLine();
//                    String carName = scanner.nextLine();
//                    carDAO.add(carName, company.id);
//                    System.out.println("The car was added!\n");
                }
                case 0 -> back = true;
            }
        }
        managerMenu();
    }

    private void addCar(Company company) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carDAO.add(carName, company.id);
        System.out.println("The car was added!\n");

    }
}
