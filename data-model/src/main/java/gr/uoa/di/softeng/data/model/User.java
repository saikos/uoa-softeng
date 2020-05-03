package gr.uoa.di.softeng.data.model;

/**
 *
 */
public class User {

    private String username;
    private String plain_password_not_safe;
    private String firstName;
    private String lastName;
    private String role;
    private String agency;

    public User() {

        // Keep this for json encoding/decoding.
    }

    public User(String username, String password, String firstName, String lastName, String role, String agency) {

        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
        setAgency(agency);
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public void setPassword(String password) {

        this.plain_password_not_safe = password;
    }

    public String getPassword() {

        return plain_password_not_safe;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setRole(String role) {

        this.role = role;
    }

    public String getRole() {

        return role;
    }

    public void setAgency(String agency) {

        this.agency = agency;
    }

    public String getAgency() {

        return agency;
    }

}
