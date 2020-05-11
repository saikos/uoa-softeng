package gr.uoa.di.softeng.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.User;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class ClientHelper {

    public static String encode(Map<String, Object> data) {

        var builder = new StringBuilder();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }

        return builder.toString();
    }

    static String parseJsonStatus(Reader reader) {

        return parseJsonAndGetValueOfField(reader, "status");
    }

    static String parseJsonToken(Reader reader) {

        return parseJsonAndGetValueOfField(reader, "token");
    }

    static User parseJsonUser(Reader reader) {

        return new Gson().fromJson(reader, User.class);
    }

    static List<User> parseJsonUsers(Reader reader) {

        return new Gson().fromJson(reader, List.class);
    }

    static Incident parseJsonIncident(Reader reader) {

        // Set the date format to that of "new java.util.Date().toString()".
        return new GsonBuilder().setDateFormat("MMM d, yyyy, h:mm:ss a").create().fromJson(reader, Incident.class);
    }

    static List<Incident> parseJsonIncidents(Reader reader) {

        return new Gson().fromJson(reader, List.class);
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
