package gr.uoa.di.softeng.api

import gr.uoa.di.softeng.data.model.Limits
import gr.uoa.di.softeng.data.model.User
import gr.uoa.di.softeng.client.RestAPI
import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Shared

/**
 *
 */
@Stepwise
class RobotTest extends Specification {

    @Shared RestAPI caller1 = new RestAPI()
    @Shared RestAPI caller2 = new RestAPI()
    @Shared Object json;

    def setup() {

        try {
            def robotTestData = new File("src/test/resources/RobotTestData.json")
            json = new JsonSlurper().parseText(robotTestData.text)
        }
        catch(ex) {
            println ex
        }
    }

    def "RT01. Health check status is OK"() {

        when:
        String status = caller1.healthCheck()

        then:
        status == "OK"
    }

    def "RT02. The database is reset successfully"() {

        when:
        String status = caller1.resetDatabase()

        then:
        status == "OK"
    }

    def "RT03. Admin logs in successfully"() {

        given:
        String username = "admin"
        String password = "admin123"

        when:
        caller1.login(username, password)

        then:
        caller1.isLoggedIn()
    }

    def "RT04. Admin creates multiple users"() {

        given:
        List usersData = json.users

        when:
        usersData.collect { userData ->
            String username  = userData.username
            String password  = userData.password
            String firstName = userData.firstName
            String lastName  = userData.lastName
            String role      = userData.role
            String agency    = userData.agency
            caller1.addUser(username, password, firstName, lastName, role, agency)
        }
        and:
        def createdUsersAll = caller1.getUsers()

        then:
        createdUsersAll.size() == usersData.size()
        createdUsersAll.indexed().every { index, createdUser ->
            def user = usersData[index]
            createdUser.getUsername()  == (String) user.username  &&
            createdUser.getFirstName() == (String) user.firstName &&
            createdUser.getLastName()  == (String) user.lastName  &&
            createdUser.getRole()      == (String) user.role      &&
            createdUser.getAgency()    == (String) user.agency
        }

        when:
        def createdUsersSome = caller1.getUsers(new Limits(1, 5))

        then:
        createdUsersSome.size() == Math.min(Math.max(usersData.size() - 1, 0), 5)
    }

    def "RT05. Admin updates one user and deletes the rest of the users"() {

        given:
        List usersData   = json.users
        String username  = usersData[0].username
        String password  = usersData[0].password
        String firstName = usersData[0].firstName
        String lastName  = usersData[0].lastName
        String role      = usersData[0].role
        String agency    = "new_updated_agency"

        when:
        caller1.updateUser(new User(username, password, firstName, lastName, role, agency))
        and:
        def deletionResponses = usersData.subList(1, usersData.size()).collect { userData ->
            caller1.deleteUser((String) userData.username)
        }

        then:
        deletionResponses.every { it == "ok" }

        when:
        def fetchedUsers = caller1.getUsers()
        def updatedUser = fetchedUsers[0]

        then:
        fetchedUsers.size() == 1
        updatedUser                != null
        updatedUser.getUsername()  == username
        updatedUser.getFirstName() == firstName
        updatedUser.getLastName()  == lastName
        updatedUser.getRole()      == role
        updatedUser.getAgency()    == agency
    }

    def "RT06. User logs in"() {

        given:
        List usersData  = json.users
        Object userData = usersData[0]
        String username = userData.username
        String password = userData.password

        when:
        caller2.login(username, password)

        then:
        caller2.isLoggedIn()
    }

    def "RT07. User logs out"() {
        when:
        caller2.logout()

        then:
        !caller2.isLoggedIn()
    }

    def "RT08. Admin deletes the remaining user"() {

        when:
        def users = caller1.getUsers()
        and:
        def deletionResponses = users.collect {
            caller1.deleteUser((String) it.username)
        }

        then:
        deletionResponses.every { it == "ok" }
    }

    def "RT09. Admin logs out"() {
        when:
        caller1.logout()

        then:
        !caller1.isLoggedIn()
    }

}