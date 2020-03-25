package gr.uoa.di.softeng.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class DataAccess {

    private static final int MAX_TOTAL_CONNECTIONS = 16;
    private static final int MAX_IDLE_CONNECTIONS = 8;
    private static final String SQL_VALIDATION_QUERY = "SELECT 1";
    private JdbcTemplate jdbcTemplate;

    public void setup(String driverClass, String url, String user, String pass) throws SQLException {

        // Initialize the data source
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(driverClass);
        bds.setUrl(url);
        bds.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        bds.setMaxIdle(MAX_IDLE_CONNECTIONS);
        bds.setUsername(user);
        bds.setPassword(pass);
        bds.setValidationQuery(SQL_VALIDATION_QUERY);
        bds.setTestOnBorrow(true);
        bds.setDefaultAutoCommit(true);

        // Check that everything works ok.
        bds.getConnection().close();

        // Initialize the jdbc template utility.
        jdbcTemplate = new JdbcTemplate(bds);
    }

    public void accessDataCheck() throws DataAccessException {

        try {
            jdbcTemplate.query(SQL_VALIDATION_QUERY, ResultSet::next);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public List<Incident> fetchIncidents() throws DataAccessException {

        return fetchIncidents(null);
    }

    public List<Incident> fetchIncidents(String name) throws DataAccessException {

        String sqlQuery;
        Object[] sqlParams;

        if (name != null) {
            sqlQuery = "SELECT * FROM `incidents` WHERE `name` = ?;";
            sqlParams = new Object[] { name };
        }
        else {
            sqlQuery = "SELECT * FROM `incidents`";
            sqlParams = new Object[] {};
        }

        try {
            return jdbcTemplate.query(sqlQuery, sqlParams, (ResultSet rs, int rowNum) -> new Incident(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("creation_date")
            ));
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

}
