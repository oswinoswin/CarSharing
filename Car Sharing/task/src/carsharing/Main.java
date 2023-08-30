package carsharing;

import java.sql.SQLException;

public class Main {


    public static void main(String[] args) throws SQLException {
        String filename = "someDefaultNameLOL6";

        if (args.length >= 2){
            for (int i = 0; i < args.length - 1; i++){
                if (args[i].equals("-databaseFileName")){
                    filename =  args[i + 1];
                    break;
                }
            }
        }



        H2CompanyDAO companyDAO = new H2CompanyDAO(filename);
        H2CarDAO carDAO = new H2CarDAO(filename);
// git test
        Menu menu = new Menu(companyDAO, carDAO);
        menu.show();



    }
}