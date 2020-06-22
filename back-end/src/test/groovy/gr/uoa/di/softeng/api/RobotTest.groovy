package gr.uoa.di.softeng.api

import gr.uoa.di.softeng.data.model.DateFormat
import gr.uoa.di.softeng.data.model.Limits
import gr.uoa.di.softeng.data.model.User
import gr.uoa.di.softeng.client.RestAPI
import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Shared
import spock.lang.IgnoreIf
import java.text.SimpleDateFormat

/**
 * Invoke this test using 
  
  ./gradlew :test --tests "*RobotTest" -DtestJson=/path/to/json

 * The test json file located at src/test/resources/RobotTestData.json will be used
 * during the project's evaluation. An additional (not known) test json file will be also used.
 */
@IgnoreIf({ TEST_JSON == null })
@Stepwise
class RobotTest extends Specification {

    private static final String TEST_JSON = System.getProperty("testJson")

    @Shared RestAPI caller1 = new RestAPI()
    @Shared RestAPI caller2 = new RestAPI()
    @Shared Object json;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DateFormat.CUSTOM);

    def setup() {

        try {
            def robotTestData = new File(TEST_JSON)
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
        int OFFSET = (int) json.users_limits.offset
        int LIMIT = (int) json.users_limits.limit

        when:
        usersData.each { userData ->
            caller1.addUser(
                (String) userData.username,
                (String) userData.password,
                (String) userData.firstName,
                (String) userData.lastName,
                (String) userData.role,
                (String) userData.agency
            )
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
        def createdUsersSome = caller1.getUsers(new Limits(OFFSET, LIMIT))

        then:
        createdUsersSome.size() == Math.min(Math.max(usersData.size() - OFFSET, 0), LIMIT)
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
        deletionResponses.every { it == "OK" }

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

    def "RT07. User manages a list of incidents"() {

        given:
        List incidentsData = json.incidents
        int OFFSET = (int) json.incidents_limits.offset
        int LIMIT = (int) json.incidents_limits.limit

        when:
        incidentsData.each { incidentData ->
            caller2.addIncident(
                (String) incidentData.title,
                (String) incidentData.description,
                (String) incidentData.x,
                (String) incidentData.y,
                (String) incidentData.startDate,
                (String) incidentData.endDate
            )
        }
        and:
        def createdIncidentsAll = caller2.getIncidents()

        then:
        createdIncidentsAll.size() == incidentsData.size()
        createdIncidentsAll.indexed().every { index, createdIncident ->
            def incident = incidentsData[index]
            createdIncident.getTitle()       == (String) incident.title &&
            createdIncident.getDescription() == (String) incident.description &&
            createdIncident.getX()           == (Double) incident.x &&
            createdIncident.getY()           == (Double) incident.y &&
            createdIncident.getStartDate()   == dateFormatter.parse((String) incident.startDate) &&
            createdIncident.getEndDate()     == (incident.endDate ? dateFormatter.parse((String) incident.endDate) : null)
        }

        when:
        def incidentsSome = caller2.getIncidents(new Limits(OFFSET, LIMIT))
        and:
        def incidentsNone = caller2.getIncidents(new Limits(incidentsData.size(), 10))

        then:
        incidentsSome.size() == Math.min(Math.max(incidentsData.size() - OFFSET, 0), LIMIT)
        incidentsNone.size() == 0

        when:
        def lastIncidentData = incidentsData[incidentsData.size() - 1]
        def lastIncident = caller2.getIncidents(new Limits(incidentsData.size() - 1, 1))[0]
        and:
        def endDate = dateFormatter.parse("2020-01-01 01:01:01.000")
        lastIncident.setEndDate(endDate)
        caller2.updateIncident(lastIncident)
        and:
        lastIncident = caller2.getIncident(lastIncident.getId())

        then:
        lastIncident                   != null
        lastIncident.getTitle()        == (String) lastIncidentData.title
        lastIncident.getDescription()  == (String) lastIncidentData.description
        lastIncident.getX()            == (Double) lastIncidentData.x
        lastIncident.getY()            == (Double) lastIncidentData.y
        lastIncident.getStartDate()    == dateFormatter.parse((String) lastIncidentData.startDate)
        lastIncident.getEndDate()      == endDate

        when:
        def deletionResponses = createdIncidentsAll.collect { incident ->
            caller1.deleteIncident(incident.getId())
        }
        and:
        def incidentsAll = caller2.getIncidents()

        then:
        deletionResponses.every { it == "OK" }
        incidentsAll.size() == 7
    }

    def "RT08. User logs out"() {
        when:
        caller2.logout()

        then:
        !caller2.isLoggedIn()
    }

    def "RT09. Admin deletes the remaining user"() {

        when:
        def users = caller1.getUsers()
        and:
        def deletionResponses = users.collect {
            caller1.deleteUser((String) it.username)
        }

        then:
        deletionResponses.every { it == "OK" }
    }

    def "RT10. Admin logs out"() {
        when:
        caller1.logout()

        then:
        !caller1.isLoggedIn()
    }

}