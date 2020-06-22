package gr.uoa.di.softeng.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.uoa.di.softeng.data.model.DateFormat;
import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.User;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        List<Map<String,Object>> parsedJsonUsers = new Gson().fromJson(reader, List.class);
        List<User> users = new ArrayList<>();

        parsedJsonUsers.forEach(parsedJsonUser -> {
            users.add(new User(
                parsedJsonUser.get("username").toString(),
                null, // API does not return user password.
                parsedJsonUser.get("firstName").toString(),
                parsedJsonUser.get("lastName").toString(),
                parsedJsonUser.get("role").toString(),
                parsedJsonUser.get("agency").toString()
            ));
        });

        return users;
    }

    static Incident parseJsonIncident(Reader reader) {

        return new GsonBuilder().setDateFormat(DateFormat.CUSTOM).create().fromJson(reader, Incident.class);
    }

    static List<Incident> parseJsonIncidents(Reader reader) {

        List<Map<String,Object>> parsedJsonIncidents = new Gson().fromJson(reader, List.class);
        List<Incident> incidents = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormat.CUSTOM);

        parsedJsonIncidents.forEach(parsedJsonIncident -> {
            String description = null;
            Date startDate = null, endDate = null;

            try { description = parsedJsonIncident.get("description").toString(); }
            catch(Exception ex) {}
            try { startDate = formatter.parse(parsedJsonIncident.get("startDate").toString()); }
            catch(Exception ex) {}
            try { endDate = formatter.parse(parsedJsonIncident.get("endDate").toString()); }
            catch(Exception ex) {}

            incidents.add(new Incident(
                parsedJsonIncident.get("id").toString(),
                parsedJsonIncident.get("title").toString(),
                description,
                Double.parseDouble(parsedJsonIncident.get("x").toString()),
                Double.parseDouble(parsedJsonIncident.get("y").toString()),
                startDate,
                endDate
            ));
        });

        return incidents;
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
