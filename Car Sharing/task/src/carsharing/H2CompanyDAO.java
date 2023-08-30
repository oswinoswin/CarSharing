package carsharing;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class H2CompanyDAO implements CompanyDAO {
    static final String CREATE_TABLE_COMPANY =  "CREATE TABLE IF NOT EXISTS COMPANY " +
            "(id IDENTITY NOT NULL PRIMARY KEY , " +
            " name VARCHAR(255) UNIQUE NOT NULL);" ;
    static final String CREATE_TABLE_CAR =  "CREATE TABLE IF NOT EXISTS CAR " +
            "(id IDENTITY NOT NULL PRIMARY KEY, " +
            " name VARCHAR(255) UNIQUE NOT NULL, " +
            "company_id INTEGER NOT NULL, " +
            "FOREIGN KEY (company_id) REFERENCES COMPANY(id)" +
            ");" ;

    static final String SELECT_ALL_COMPANIES = "SELECT * FROM COMPANY;";
    static final String DROP_TABLE_COMPANY = "DROP TABLE IF EXISTS COMPANY;";
    static final String INSERT_COMPANY = "INSERT INTO company(name) VALUES( ? );";

    static final String RESTART_ID = "ALTER TABLE company ALTER COLUMN id RESTART WITH 1;";
    Connection connection;
    public H2CompanyDAO(String dbFilename) throws  SQLException {
        String DB_URL = "jdbc:h2:./src/carsharing/db/" + dbFilename;
        JdbcDataSource dataSource = Objects.requireNonNull( new JdbcDataSource() );
        dataSource.setURL(DB_URL);
        connection = dataSource.getConnection();

        Statement statement = connection.createStatement();
//        statement.execute(DROP_TABLE_COMPANY);
        statement.execute(CREATE_TABLE_COMPANY);
//        statement.execute(RESTART_ID);
        statement.close();
    }

    @Override
    public List<Company> findAll() throws SQLException {
        List<Company> companies = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_ALL_COMPANIES);
        while (resultSet.next()){
            Company company = new Company(resultSet.getString("name"), resultSet.getInt("id"));
            companies.add(company);
        }
        resultSet.close();
        statement.close();
        return companies;
    }

    @Override
    public void add(String companyName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COMPANY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, companyName);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void cleanup() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(DROP_TABLE_COMPANY);
        close();
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) connection.close();
    }
}
