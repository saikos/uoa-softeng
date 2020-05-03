package gr.uoa.di.softeng.data;

import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.Limits;
import gr.uoa.di.softeng.data.model.Report;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Incident> fetchIncidents(Limits limits) throws DataAccessException {

        return fetchIncidents(null, limits);
    }

    public List<Incident> fetchIncidents(String title, Limits limits) throws DataAccessException {

        String sqlQuery;
        Object[] sqlParams;

        if (title != null) {
            sqlQuery = "SELECT * FROM `incidents` WHERE `name` = ? LIMIT ? OFFSET ?;";
            sqlParams = new Object[] { title, limits.getCount(), limits.getStart() };
        }
        else {
            sqlQuery = "SELECT * FROM `incidents`";
            sqlParams = new Object[] {};
        }

        try {
            // TODO: Update database table `incidents`:
            // TODO: - rename column `name` to `title`.
            // TODO: - rename column `creation_date` to `start_date`
            // TODO: - create column `end_date`
            // TODO: - create column `x`
            // TODO: - create column `y`

            // TODO: Create table `reports` to store Report records.

            // TODO: for each incident record in result set, fetch its records or its record ids.

            return jdbcTemplate.query(sqlQuery, sqlParams, (ResultSet rs, int rowNum) -> new Incident(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                0d, // Pass fixed value for "x" since there is no such column in the example database schema.
                0d, // Pass fixed value for "y" since there is no such column in the example database schema.
                new ArrayList<Report>()
            ));
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

}
