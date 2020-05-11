package gr.uoa.di.softeng.data;

import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.Limits;
import gr.uoa.di.softeng.data.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
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
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User related CRUD methods

    public User createUser(String username, String password, String firstName, String lastName, String role,
                           String agency) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    public List<User> fetchUsers(Limits limits) throws DataAccessException {

        String sqlQuery = "SELECT * FROM `users` LIMIT ? OFFSET ?;";
        Object[] sqlParams = new Object[] { limits.getCount(), limits.getStart() };

        try {
            return jdbcTemplate.query(sqlQuery, sqlParams, (ResultSet rs, int rowNum) -> new User(
                rs.getString("username"),
                null,
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("role"),
                rs.getString("agency")
            ));
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public User fetchUser(String userId) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    public User updateUser(String userId, String password, String firstName, String lastName, String role,
                           String agency) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    public void deleteUser(String userId) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Incident related CRUD methods

    public Incident createIncident(String title, String description, String x, String y, String startDate,
                                   String endDate) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    public List<Incident> fetchIncidents(String title, Limits limits) throws DataAccessException {

        String sqlQuery;
        Object[] sqlParams;

        if (title != null) {
            sqlQuery = "SELECT * FROM `incidents` WHERE `title` = ? LIMIT ? OFFSET ?;";
            sqlParams = new Object[] { title, limits.getCount(), limits.getStart() };
        }
        else {
            sqlQuery = "SELECT * FROM `incidents` LIMIT ? OFFSET ?;";
            sqlParams = new Object[] { limits.getCount(), limits.getStart() };
        }

        try {
            return jdbcTemplate.query(sqlQuery, sqlParams, (ResultSet rs, int rowNum) -> new Incident(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDouble("x"),
                rs.getDouble("y"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
            ));
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Incident fetchIncident(String incidentId) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    public Incident updateIncident(String id, String title, String description, String x, String y, String startDate,
                                   String endDate) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    public void deleteIncident(String incidentId) throws ResourceException {

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
