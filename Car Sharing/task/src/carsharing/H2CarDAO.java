package carsharing;

import carsharing.Car;
import carsharing.Company;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class H2CarDAO {
    static final String CREATE_TABLE_CAR =  "CREATE TABLE IF NOT EXISTS CAR " +
            "(id IDENTITY NOT NULL PRIMARY KEY, " +
            " name VARCHAR(255) UNIQUE NOT NULL, " +
            "company_id INTEGER NOT NULL, " +
            " CONSTRAINT fk_company " +
            "FOREIGN KEY (company_id) REFERENCES company(id)" +
            "ON DELETE CASCADE" +
            ");" ;
    static final String INSERT_CAR = "INSERT INTO car(name, company_id) VALUES( ? , ?);";
    static final String DROP_TABLE_CAR = "DROP TABLE IF EXISTS CAR";
    static final String DROP_FK = "ALTER TABLE CAR\n" +
            "DROP CONSTRAINT fk_company;";

    static final String SELECT_CARS_FOR_COMPANY = "SELECT * FROM CAR WHERE company_id = ? ;";
    static final String SELECT_ALL_CARS = "SELECT * FROM CAR;";
    static final String REMOVE_ALL = "REMOVE * FROM CAR";

    Connection connection;

    public H2CarDAO(String dbFilename) throws SQLException {
        String DB_URL = "jdbc:h2:./src/carsharing/db/" + dbFilename;
        JdbcDataSource dataSource = Objects.requireNonNull( new JdbcDataSource() );
        dataSource.setURL(DB_URL);
        connection = dataSource.getConnection();

        Statement statement = connection.createStatement();
        statement.execute(DROP_TABLE_CAR);

        statement.execute(CREATE_TABLE_CAR);
        statement.close();
    }

    public void add(String name, int company_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CAR, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, company_id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<Car> findAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_ALL_CARS);
        while (resultSet.next()){
            Car car = new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
            cars.add(car);
        }
        resultSet.close();
        statement.close();
        return cars;
    }

    public List<Car> companyCars(int companyID) throws SQLException {
        List<Car> cars = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CARS_FOR_COMPANY);
        preparedStatement.setInt(1, companyID);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Car car = new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
            cars.add(car);
        }
        resultSet.close();
        preparedStatement.close();
        return cars;
    }

    public void cleanup() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(DROP_FK);
        statement.execute(DROP_TABLE_CAR);
        statement.close();
        close();
    }

    public void close() throws SQLException {
        if (connection != null) connection.close();
    }
}
