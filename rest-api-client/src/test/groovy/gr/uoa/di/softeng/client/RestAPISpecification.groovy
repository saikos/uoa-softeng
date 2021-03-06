package gr.uoa.di.softeng.client

import gr.uoa.di.softeng.data.model.DateFormat
import gr.uoa.di.softeng.data.model.Incident
import gr.uoa.di.softeng.data.model.Limits
import gr.uoa.di.softeng.data.model.User
import java.text.SimpleDateFormat
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import static gr.uoa.di.softeng.client.RestAPI.BASE_URL
import static com.github.tomakehurst.wiremock.client.WireMock.*

/**
 *
 */
@Stepwise
class RestAPISpecification extends Specification {

    private static final String TOKEN1 = "token1"
    private static final String TOKEN2 = "token2"
    private static final int MOCK_SERVER_PORT = 9001

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DateFormat.CUSTOM)

    @Shared WireMockServer wms
    @Shared RestAPI caller1 = new RestAPI("localhost", MOCK_SERVER_PORT, null)
    @Shared RestAPI caller2 = new RestAPI("localhost", MOCK_SERVER_PORT, null)

    def setupSpec() {

        wms = new WireMockServer(WireMockConfiguration.options().httpsPort(MOCK_SERVER_PORT))
        wms.start()
    }

    def cleanupSpec() {

        wms.stop()
    }

    def "T01. Health check status is OK"() {

        given:
        wms.givenThat(
            get(
                urlEqualTo("$BASE_URL/health-check")
            ).willReturn(
                okJson(new Gson().toJson([ status: "OK" ]))
            )
        )

        when:
        String status = caller1.healthCheck()

        then:
        status == "OK"
    }

    def "T02. The database is reset successfully"() {

        given:
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/reset")
            ).willReturn(
                okJson(new Gson().toJson([ status: "OK" ]))
            )
        )

        when:
        String status = caller1.resetDatabase()

        then:
        status == "OK"
    }

    private static final adminUserLoginInfo = [ username: "admin", password: "pass123!" ]

    def "T03. Admin logs in successfully"() {

        given:
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/login")
            ).withRequestBody(
                equalTo(ClientHelper.encode(adminUserLoginInfo))
            ).willReturn(
                okJson(new Gson().toJson([ token: TOKEN1 ]))
            )
        )

        when:
        caller1.login(adminUserLoginInfo.username, adminUserLoginInfo.password)

        then:
        caller1.isLoggedIn()
    }

    private static final tempUserInfo = [
        username:   "temporary_user",
        password:   "a_password",
        firstName:  "a_name",
        lastName:   "a_surname",
        role:       "a_role",
        agency:     "an_agency",
    ]

    def "T04. Admin creates a temp user"() {

        given:
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/admin/users")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN1)
            ).withRequestBody(
                equalTo(ClientHelper.encode(tempUserInfo))
            ).willReturn(
                okJson(new Gson().toJson(tempUserInfo))
            )
        )

        when:
        User createdUser = caller1.addUser(tempUserInfo.username, tempUserInfo.password, tempUserInfo.firstName,
            tempUserInfo.lastName, tempUserInfo.role, tempUserInfo.agency)

        then:
        createdUser                != null &&
        createdUser.getUsername()  == tempUserInfo.username  &&
        createdUser.getPassword()  == tempUserInfo.password  &&
        createdUser.getFirstName() == tempUserInfo.firstName &&
        createdUser.getLastName()  == tempUserInfo.lastName  &&
        createdUser.getRole()      == tempUserInfo.role      &&
        createdUser.getAgency()    == tempUserInfo.agency
    }

    private static final tempUserNewInfo = tempUserInfo + [ agency: "updated_agency" ]

    def "T05. Admin updates the temp user"() {

        given:
        wms.givenThat(
            put(
                urlEqualTo("$BASE_URL/admin/users/${tempUserInfo.username}")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN1)
            ).withRequestBody(
                equalTo(ClientHelper.encode(tempUserNewInfo))
            ).willReturn(
                okJson(new Gson().toJson(tempUserNewInfo))
            )
        )

        when:
        User updatedUser = caller1.updateUser(new User(tempUserNewInfo.username, tempUserNewInfo.password,
            tempUserNewInfo.firstName, tempUserNewInfo.lastName, tempUserNewInfo.role, tempUserNewInfo.agency))

        then:
        updatedUser                != null &&
        updatedUser.getUsername()  == tempUserNewInfo.username  &&
        updatedUser.getPassword()  == tempUserNewInfo.password  &&
        updatedUser.getFirstName() == tempUserNewInfo.firstName &&
        updatedUser.getLastName()  == tempUserNewInfo.lastName  &&
        updatedUser.getRole()      == tempUserNewInfo.role      &&
        updatedUser.getAgency()    == tempUserNewInfo.agency
    }

    def "T06. Temp user logs in"() {

        given:
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/login")
            ).withRequestBody(
                equalTo(ClientHelper.encode([ username: tempUserInfo.username, password: tempUserInfo.password ]))
            ).willReturn(
                okJson(new Gson().toJson([ token: TOKEN2 ]))
            )
        )

        when:
        caller2.login(tempUserInfo.username, tempUserInfo.password)

        then:
        caller2.isLoggedIn()
    }

    private static tempIncidentInfo = [
        title:       "a_title",
        x:           123.0,
        y:           456.0,
        startDate:   dateFormatter.format(new Date()),
    ]

    def "T07. Temp user creates a new incident"() {

        given:
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/incidents")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).withRequestBody(
                equalTo(ClientHelper.encode(tempIncidentInfo))
            ).willReturn(
                okJson(new GsonBuilder().serializeNulls().create().toJson(tempIncidentInfo + [
                    id: "a_unique_incident_identifier",
                    description: null,
                    endDate: null,
                ]))
            )
        )

        when:
        Incident createdIncident = caller2.addIncident(tempIncidentInfo.title.toString(), null,
            tempIncidentInfo.x.toString(), tempIncidentInfo.y.toString(), tempIncidentInfo.startDate.toString(), null)

        then:
        createdIncident                    != null &&
        createdIncident.getId()            != null &&
        createdIncident.getTitle()         == tempIncidentInfo.title &&
        createdIncident.getDescription()   == null &&
        createdIncident.getX()             == tempIncidentInfo.x &&
        createdIncident.getY()             == tempIncidentInfo.y &&
        createdIncident.getStartDate()     == dateFormatter.parse(tempIncidentInfo.startDate.toString()) &&
        createdIncident.getEndDate()       == null
    }

    def "T08. Temp user retrieves a list of incidents"() {

        given:
        wms.givenThat(
            get(
                urlEqualTo("$BASE_URL/incidents?start=0&count=1")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).willReturn(
                okJson(new Gson().toJson([ [ id: "1234" ] + tempIncidentInfo ]))
            )
        )

        when:
        List<Incident> incidents = caller2.getIncidents(new Limits(0, 1))

        then:
        incidents.size() == 1
    }

    private static final tempIncidentNewInfo = [
        id:          "1234",
        title:       tempIncidentInfo.title,
        description: "updated_description",
        x:           tempIncidentInfo.x,
        y:           tempIncidentInfo.y,
        startDate:   tempIncidentInfo.startDate,
    ]

    def "T09. Temp user updates an incident"() {

        given:
        wms.givenThat(
            put(
                urlEqualTo("$BASE_URL/incidents/${tempIncidentNewInfo.id}")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).withRequestBody(
                equalTo(ClientHelper.encode(tempIncidentNewInfo))
            ).willReturn(
                okJson(new Gson().toJson(tempIncidentNewInfo))
            )
        )

        when:
        Incident updatedIncident = caller2.updateIncident(new Incident(tempIncidentNewInfo.id.toString(),
            tempIncidentNewInfo.title.toString(), tempIncidentNewInfo.description.toString(),
            (Double)tempIncidentNewInfo.x, (Double)tempIncidentNewInfo.y,
            dateFormatter.parse(tempIncidentNewInfo.startDate.toString()), null))

        then:
        updatedIncident                     != null &&
        updatedIncident.getId()             == tempIncidentNewInfo.id &&
        updatedIncident.getTitle()          == tempIncidentNewInfo.title &&
        updatedIncident.getDescription()    == tempIncidentNewInfo.description &&
        updatedIncident.getX()              == tempIncidentNewInfo.x &&
        updatedIncident.getY()              == tempIncidentNewInfo.y &&
        updatedIncident.getStartDate()      == dateFormatter.parse(tempIncidentInfo.startDate.toString()) &&
        updatedIncident.getEndDate()        == null
    }

    def "T10. Temp user retrieves an incident"() {

        given:
        wms.givenThat(
            get(
                urlEqualTo("$BASE_URL/incidents/${tempIncidentNewInfo.id}")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).willReturn(
                okJson(new Gson().toJson(tempIncidentNewInfo))
            )
        )

        when:
        Incident incident = caller2.getIncident(tempIncidentNewInfo.id.toString())

        then:
        incident != null &&
        incident.getId()             == tempIncidentNewInfo.id &&
        incident.getTitle()          == tempIncidentNewInfo.title &&
        incident.getDescription()    == tempIncidentNewInfo.description &&
        incident.getX()              == tempIncidentNewInfo.x &&
        incident.getY()              == tempIncidentNewInfo.y &&
        incident.getStartDate()      == dateFormatter.parse(tempIncidentInfo.startDate.toString()) &&
        incident.getEndDate()        == null
    }

    def "T11. Temp user deletes an incident"() {

        given:
        wms.givenThat(
            delete(
                urlEqualTo("$BASE_URL/incidents/${tempIncidentNewInfo.id}")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).willReturn(
                okJson(new Gson().toJson([ status: "OK" ]))
            )
        )

        when:
        String status = caller2.deleteIncident(tempIncidentNewInfo.id.toString())

        then:
        status == "OK"
    }

    def "T12. Temp user logs out"() {

        given:
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/logout")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            )
        )

        when:
        caller2.logout()

        then:
        !caller2.isLoggedIn()
    }

    def "T13. Admin deletes the temp user"() {

        given:
        wms.givenThat(
            delete(
                urlEqualTo("$BASE_URL/admin/users/${tempUserInfo.username}")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN1)
            ).willReturn(
                okJson(new Gson().toJson([ status: "OK" ]))
            )
        )

        when:
        String status = caller1.deleteUser(tempUserInfo.username)

        then:
        status == "OK"
    }

    def "T14. Admin logs out"() {

        given:
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/logout")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN1)
            )
        )

        when:
        caller1.logout()

        then:
        !caller1.isLoggedIn()
    }

}