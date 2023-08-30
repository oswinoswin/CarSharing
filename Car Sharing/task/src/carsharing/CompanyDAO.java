package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDAO {
    List<Company> findAll() throws SQLException;
//    void add(Company company);

    void add(String companyName) throws SQLException;
    void close() throws  SQLException;
}
