package gr.uoa.di.softeng.client;

import com.google.gson.Gson;
import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.Report;
import gr.uoa.di.softeng.data.model.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class ClientHelper {

    public static String encode(Map<String, String> data) {

        var builder = new StringBuilder();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }

        return builder.toString();
    }

    static String parseJsonStatus(Reader reader) {

        return parseJsonAndGetValueOfField(reader, "status");
    }

    static String parseJsonToken(Reader reader) {

        return parseJsonAndGetValueOfField(reader, "token");
    }

    static List<User> parseJsonUsers(Reader reader) {

        return new Gson().fromJson(reader, List.class);
    }

    static User parseJsonUser(Reader reader) {

        return new Gson().fromJson(reader, User.class);
    }

    static List<Incident> parseJsonIncidents(Reader reader) {

        return new Gson().fromJson(reader, List.class);
    }

    static Incident parseJsonIncident(Reader reader) {

        return new Gson().fromJson(reader, Incident.class);
    }

    static List<Report> parseJsonReports(Reader reader) {

        return new Gson().fromJson(reader, List.class);
    }

    static Report parseJsonReport(Reader reader) {

        return new Gson().fromJson(reader, Report.class);
    }

    static ImportResult parseJsonImportResult(Reader reader) {

        return new Gson().fromJson(reader, ImportResult.class);
    }

    static String readContents(InputStream inputStream) {

        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).
            lines().collect(Collectors.joining("\n"));
    }

    private static String parseJsonAndGetValueOfField(Reader reader, String field) {

        Gson gson = new Gson();
        Map map = gson.fromJson(reader, Map.class);

        return (String) map.get(field);
    }

}
