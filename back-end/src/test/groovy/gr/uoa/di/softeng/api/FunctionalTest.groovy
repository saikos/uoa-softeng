package gr.uoa.di.softeng.api

import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Shared

import gr.uoa.di.softeng.client.RestAPI

/**
 *
 */
@Stepwise
class FunctionalTest extends Specification {

    @Shared RestAPI caller1 = new RestAPI()

    def "T01. Health check status is OK"() {
        given:
        String status = caller1.healthCheck()

        expect:
        status == "OK"

    }


    def "T02. The database is reset successfully"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T03. Admin logs in successfully"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }


    def "T04. Admin creates a temp user"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T05. Admin updates the temp user"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T06. Temp user logs in"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T07. Temp user creates a new incident"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T08. Temp user retrieves a list of incidents"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T09. Temp user updates an incident"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T10. Temp user retrieves an incident"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T11. Temp user deletes an incident"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T12. Temp user logs out"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T13. Admin deletes the temp user"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

    def "T14. Admin logs out"() {
        // TODO: Implement this
        expect:
        caller1 != null
    }

}