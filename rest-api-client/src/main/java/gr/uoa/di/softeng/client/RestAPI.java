package gr.uoa.di.softeng.client;

import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.Limits;
import gr.uoa.di.softeng.data.model.User;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 */
public class RestAPI {

    public static final String BASE_URL = "/control-center/api";
    public static final String CUSTOM_HEADER = "X-CONTROL-CENTER-AUTH";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String URL_ENCODED = "application/x-www-form-urlencoded";

    private static final TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
        }
    };

    static {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
    }

    private final String urlPrefix;
    private final HttpClient client;

    private String token = null; // User is not logged in.

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public RestAPI() throws RuntimeException {

        this("localhost", 9000);
    }

    public RestAPI(String host, int port) throws RuntimeException {

        try {
            this.client = newHttpClient();
        }
        catch(NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e.getMessage());
        }

        this.urlPrefix = "http://" + host + ":" + port + BASE_URL;
    }

    public boolean isLoggedIn() {

        return token != null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String healthCheck() {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newGetRequest(getHealthCheckResourceUrl()),
            ClientHelper::parseJsonStatus
        );
    }

    public String resetDatabase() {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newPostRequest(getResetResourceUrl(), URL_ENCODED, HttpRequest.BodyPublishers.noBody()),
            ClientHelper::parseJsonStatus
        );
    }

    public void login(String username, String password) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("username", username);
        formData.put("password", password);

        token = sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newPostRequest(getLoginResourceUrl(), URL_ENCODED, ofUrlEncodedFormData(formData)),
            ClientHelper::parseJsonToken
        );
    }

    public void logout() {

        sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newPostRequest(getLogoutResourceUrl(), URL_ENCODED, HttpRequest.BodyPublishers.noBody()),
            null
        );

        token = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User(s) CRUD

    public User addUser(String username, String password, String firstName, String lastName, String role, String agency) {

        Map<String, Object> formData = new LinkedHashMap<>();
        // All user fields must be explicitly initialized.
        formData.put("username", username);
        formData.put("password", password);
        formData.put("firstName", firstName);
        formData.put("lastName", lastName);
        formData.put("role", role);
        formData.put("agency", agency);

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newPostRequest(getUsersResourceUrl(), URL_ENCODED, ofUrlEncodedFormData(formData)),
            ClientHelper::parseJsonUser
        );
    }

    public List<User> getUsers() {

        return getUsers(new Limits());
    }

    public List<User> getUsers(Limits limits) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newGetRequest(getUsersResourceUrl(limits)),
            ClientHelper::parseJsonUsers
        );
    }

    public User getUser(String username) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newGetRequest(getUserResourceUrl(username)),
            ClientHelper::parseJsonUser
        );
    }

    public User updateUser(User user) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("username",    user.getUsername());
        formData.put("password",    user.getPassword());
        formData.put("firstName",   user.getFirstName());
        formData.put("lastName",    user.getLastName());
        formData.put("role",        user.getRole());
        formData.put("agency",      user.getAgency());

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newPutRequest(getUserResourceUrl(user.getUsername()), URL_ENCODED, ofUrlEncodedFormData(formData)),
            ClientHelper::parseJsonUser
        );
    }

    public String deleteUser(String username) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newDeleteRequest(getUserResourceUrl(username)),
            ClientHelper::parseJsonStatus
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Incident(s) CRUD

    public Incident addIncident(String title, String description, String x, String y, String startDate, String endDate) {

        Map<String, Object> formData = new LinkedHashMap<>();
        // Incident's fields that must be explicitly initialized.
        formData.put("title", title);
        formData.put("x", x);
        formData.put("y", y);
        // All user fields must be explicitly initialized.
        if(startDate != null)   formData.put("startDate", startDate);
        if(endDate != null)     formData.put("endDate", endDate);
        if(description != null) formData.put("description", description);

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newPostRequest(getIncidentsResourceUrl(), URL_ENCODED, ofUrlEncodedFormData(formData)),
            ClientHelper::parseJsonIncident
        );
    }

    public List<Incident> getIncidents() {

        return getIncidents(new Limits());
    }

    public List<Incident> getIncidents(Limits limits) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newGetRequest(getIncidentsResourceUrl(limits)),
            ClientHelper::parseJsonIncidents
        );
    }

    public Incident getIncident(String incidentId) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newGetRequest(getIncidentResourceUrl(incidentId)),
            ClientHelper::parseJsonIncident
        );
    }

    public Incident updateIncident(Incident incident) {

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("id",          incident.getId());
        formData.put("title",       incident.getTitle());
        formData.put("x",           incident.getX());
        formData.put("y",           incident.getY());
        formData.put("startDate",   incident.getStartDate());
        formData.put("endDate",     incident.getEndDate());
        formData.put("description", incident.getDescription());

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newPutRequest(getIncidentResourceUrl(incident.getId()), URL_ENCODED, ofUrlEncodedFormData(formData)),
            ClientHelper::parseJsonIncident
        );
    }

    public String deleteIncident(String incidentId) {

        return sendRequestAndParseResponseBodyAsUTF8Text(
            () -> newDeleteRequest(getIncidentResourceUrl(incidentId)),
            ClientHelper::parseJsonStatus
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    String getHealthCheckResourceUrl() {

        return urlPrefix + "/health-check";
    }

    String getResetResourceUrl() {

        return urlPrefix + "/reset";
    }

    String getLoginResourceUrl() {

        return urlPrefix + "/login";
    }

    String getLogoutResourceUrl() {

        return urlPrefix + "/logout";
    }

    String getUsersResourceUrl() {

        return getUsersResourceUrl(null);
    }

    String getUsersResourceUrl(Limits limits) {

        return urlPrefix + "/admin/users"
            + (limits == null ? "" : "?start=" + limits.getStart() + "&count=" + limits.getCount());
    }

    String getUserResourceUrl(String username) {

        return urlPrefix + "/admin/users/" + URLEncoder.encode(username, StandardCharsets.UTF_8);
    }

    String getIncidentsResourceUrl() {

        return getIncidentsResourceUrl(null);
    }

    String getIncidentsResourceUrl(Limits limits) {

        return urlPrefix + "/incidents"
            + (limits == null ? "" : "?start=" + limits.getStart() + "&count=" + limits.getCount());
    }

    String getIncidentResourceUrl(String incidentId) {

        return urlPrefix + "/incidents/" + URLEncoder.encode(incidentId, StandardCharsets.UTF_8);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private HttpRequest newPostRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher) {

        return newRequest("POST", url, contentType, bodyPublisher);
    }

    private HttpRequest newGetRequest(String url) {

        return newRequest("GET", url, URL_ENCODED, HttpRequest.BodyPublishers.noBody());
    }

    private HttpRequest newPutRequest(String url, String contentType, HttpRequest.BodyPublisher bodyPublisher) {

        return newRequest("PUT", url, contentType, bodyPublisher);
    }

    private HttpRequest newDeleteRequest(String url) {

        return newRequest("DELETE", url, URL_ENCODED, HttpRequest.BodyPublishers.noBody());
    }

    private HttpRequest newRequest(String method, String url, String contentType,
                                   HttpRequest.BodyPublisher bodyPublisher) {

        HttpRequest.Builder builder = HttpRequest.newBuilder();

        if (token != null) {
            builder.header(CUSTOM_HEADER, token);
        }

        return builder
            .method(method, bodyPublisher)
            .header(CONTENT_TYPE_HEADER, contentType)
            .uri(URI.create(url))
            .build();
    }

    private <T> T sendRequestAndParseResponseBodyAsUTF8Text(Supplier<HttpRequest> requestSupplier,
                                                            Function<Reader, T> bodyProcessor) {

        HttpRequest request = requestSupplier.get();

        try {
            System.out.println("Sending " + request.method() + " to " + request.uri());
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            int statusCode = response.statusCode();
            System.out.println(">>> " + statusCode);
            if (statusCode == 200) {
                try {
                    if (bodyProcessor != null) {
                        return bodyProcessor.apply(new InputStreamReader(response.body(), StandardCharsets.UTF_8));
                    }
                    else {
                        return null;
                    }
                }
                catch(Exception e) {
                    throw new ResponseProcessingException(e.getMessage(), e);
                }
            }
            else {
                throw new ServerResponseException(statusCode, ClientHelper.readContents(response.body()));
            }
        }
        catch(IOException | InterruptedException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    /**
     * Helper method to create a new http client that can tolerate self-signed or improper ssl certificates.
     */
    private static HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        return HttpClient.newBuilder().sslContext(sslContext).build();
    }

    private static HttpRequest.BodyPublisher ofUrlEncodedFormData(Map<String, Object> data) {

        return HttpRequest.BodyPublishers.ofString(ClientHelper.encode(data));
    }

}
