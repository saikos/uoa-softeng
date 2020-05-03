package gr.uoa.di.softeng.client

import gr.uoa.di.softeng.data.model.Incident
import gr.uoa.di.softeng.data.model.Limits
import gr.uoa.di.softeng.data.model.User
import static gr.uoa.di.softeng.client.RestAPI.BASE_URL

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder
import com.google.gson.Gson
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import static com.github.tomakehurst.wiremock.client.WireMock.*

/**
 *
 */
@Stepwise
class RestAPISpecification extends Specification {

    private static final String TOKEN1 = "token1"
    private static final String TOKEN2 = "token2"
    private static final int MOCK_SERVER_PORT = 9001

    @Shared WireMockServer wms
    @Shared RestAPI caller1 = new RestAPI("localhost", MOCK_SERVER_PORT)
    @Shared RestAPI caller2 = new RestAPI("localhost", MOCK_SERVER_PORT)

    def setupSpec() {
        wms = new WireMockServer(WireMockConfiguration.options().httpsPort(MOCK_SERVER_PORT))
        wms.start()
    }

    def cleanupSpec() {
        wms.stop()
    }

    def "T. Health check status is OK"() {
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

    def "T. The database is reset successfully"() {
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

    def "T. Admin logs in successfully"() {
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

    def "T. Admin creates a temp user"() {
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
        User user = caller1.addUser(tempUserInfo.username, tempUserInfo.password, tempUserInfo.firstName,
            tempUserInfo.lastName, tempUserInfo.role, tempUserInfo.agency)
        println(user)

        then:
        user.getUsername()  == tempUserInfo.username  &&
        user.getPassword()  == tempUserInfo.password  &&
        user.getFirstName() == tempUserInfo.firstName &&
        user.getLastName()  == tempUserInfo.lastName  &&
        user.getRole()      == tempUserInfo.role      &&
        user.getAgency()    == tempUserInfo.agency
    }

    private static final tempUserNewInfo = tempUserInfo + [ agency: "an_updated_agency" ]

    def "T. Admin updates the temp user"() {
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
        User user = caller1.updateUser(new User(tempUserNewInfo.username, tempUserNewInfo.password,
            tempUserNewInfo.firstName, tempUserNewInfo.lastName, tempUserNewInfo.role, tempUserNewInfo.agency))

        then:
        user.getUsername()  == tempUserNewInfo.username  &&
        user.getPassword()  == tempUserNewInfo.password  &&
        user.getFirstName() == tempUserNewInfo.firstName &&
        user.getLastName()  == tempUserNewInfo.lastName  &&
        user.getRole()      == tempUserNewInfo.role      &&
        user.getAgency()    == tempUserNewInfo.agency
    }

    def "T. Temp user logs in"() {
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

    def start = 0, count = 1
    def mockIncidentInfo = [
        id: "",
    ]

    def "T. Temp user retrieves a list of incidents"() {
        given:
        wms.givenThat(
            get(
                urlEqualTo("$BASE_URL/incidents?start=$start&count=$count")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).willReturn(
                okJson(new Gson().toJson([ mockIncidentInfo ]))
            )
        )

        when:
        List<Incident> incidents = caller2.getIncidents(new Limits(start, count))

        then:
        incidents.size() == 1
    }

    def "T. Temp user retrieves an incident"() {
        given:
        wms.givenThat(
            get(
                urlEqualTo("$BASE_URL/incidents/${mockIncidentInfo.id}")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).willReturn(
                okJson('{"username":"user", "email":"email"}')
            )
        )

        when:
        Incident incident = caller2.getIncident(mockIncidentInfo.id)

        then:
        incident != null
    }

    def "T. Temp user creates a new incident report"() {
        given:
        wms.givenThat(
            get(
                urlEqualTo("$BASE_URL/...")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).willReturn(
                aResponse().withStatus(402)
            )
        )

        when:
        true

        then:
        ServerResponseException exception = thrown()
        exception.getStatusCode() == 402
    }

    def "T. Temp user retrieves an incident report"() {
        given:
        wms.givenThat(
            put(
                urlEqualTo("$BASE_URL/admin/users/user")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN1)
            ).withRequestBody(
                equalTo(ClientHelper.encode([email:"email"]))
            ).willReturn(
                okJson('{"username":"user", "email":"email"}')
            )
        )

        when:
        true

        then:
        true
    }


    def "T. Temp user updates an incident report"() {
        given:
        String csv = Paths.get(getClass().getResource("/test.csv").toURI()).toString()
        wms.givenThat(
            post(
                urlEqualTo("$BASE_URL/...")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).withMultipartRequestBody(
                new MultipartValuePatternBuilder().
                    withName("file").
                    withBody(
                        binaryEqualTo(
                            Base64.mimeEncoder.encode(new File(csv).getBytes())
                        )
                    )
            ).willReturn(
                okJson('')
            )
        )

        when:
        true

        then:
        true
    }

    def "T. Temp user deletes an incident report"() {
        given:
        wms.givenThat(
            delete(
                urlEqualTo("$BASE_URL/incidents/${ID}/reports/${ID}")
            ).withHeader(
                RestAPI.CUSTOM_HEADER, equalTo(TOKEN2)
            ).willReturn(
                okJson(new Gson().toJson([ status: "OK" ]))
            )
        )

        when:
        String status = caller2.deleteReport(incidentId, reportId)

        then:
        status == "OK"
    }

    def "file/{id}"() {
        given:
        wms.givenThat(
            get(
                urlEqualTo("$BASE_URL/...")
            ).willReturn(
                aResponse().withStatus(401)
            )
        )

        when:
        true

        then:
        ServerResponseException exception = thrown()
        exception.getStatusCode() == 401
    }

    def "T. Temp user logs out"() {
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

    def "T. Admin deletes the temp user"() {
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

    def "T. Admin logs out"() {
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