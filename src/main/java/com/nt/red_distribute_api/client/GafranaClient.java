package com.nt.red_distribute_api.client;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.HttpServletResponse;

public class GafranaClient {
    private String grafanaUrl;
    private String apiKey;

    public GafranaClient(String grafanaUrl, String apiKey){
        this.grafanaUrl = grafanaUrl;
        this.apiKey = apiKey;
    }

    public List<Integer> getAllAlertIds() throws IOException {
            String url = grafanaUrl + "/api/v1/provisioning/alert-rules";
            String jsonResponse = makeApiRequest(url);

            JSONArray alertRules = new JSONArray(jsonResponse);
            List<Integer> alertIds = new ArrayList<>();

            for (int i = 0; i < alertRules.length(); i++) {
                JSONObject alertRule = alertRules.getJSONObject(i);
                alertIds.add(alertRule.getInt("id"));
            }

            return alertIds;
        }

    public void writeAlertHistoryToCsv(List<Integer> alertIds,String from, String to, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"alert_history.csv\"");
        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(response.getOutputStream()), CSVFormat.DEFAULT.withHeader(
                "id", "alertId", "dashboardId", "panelId", "userId", "newState", "prevState", "created", "updated"))) {

            for (int alertId : alertIds) {
                String url = grafanaUrl + "/api/annotations?alertId=" + alertId +"&from=" + from + "&to=" + to;
                String jsonResponse = makeApiRequest(url);

                JSONArray alertHistory = new JSONArray(jsonResponse);
                for (int i = 0; i < alertHistory.length(); i++) {
                    JSONObject alert = alertHistory.getJSONObject(i);
                    printer.printRecord(
                            alert.getInt("id"),
                            alert.getInt("alertId"),
                            alert.getInt("dashboardId"),
                            alert.getInt("panelId"),
                            alert.getInt("userId"),
                            alert.getString("text"),
                            alert.getString("newState"),
                            alert.getString("prevState"),
                            alert.getLong("created"),
                            alert.getLong("updated"));
                }
            }
        }
    }

    public void writeAlertHistoryToTextFile(List<Integer> alertIds,String from, String to, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=alert_history.txt");
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream()))) {
            // Write the header
            writer.println("id\talertId\tdashboardId\tpanelId\tuserId\tnewState\tprevState\tcreated\tupdated");

            for (int alertId : alertIds) {
                String url = grafanaUrl + "/api/annotations?alertId=" + alertId +"&from=" + from + "&to=" + to;
                String jsonResponse = makeApiRequest(url);

                JSONArray alertHistory = new JSONArray(jsonResponse);
                for (int i = 0; i < alertHistory.length(); i++) {
                    JSONObject alert = alertHistory.getJSONObject(i);
                    writer.printf(
                        "%d\t%d\t%d\t%d\t%d\t%s\t%s\t%d\t%d%n",
                        alert.getInt("id"),
                        alert.getInt("alertId"),
                        alert.getInt("dashboardId"),
                        alert.getInt("panelId"),
                        alert.getInt("userId"),
                        alert.getString("newState"),
                        alert.getString("prevState"),
                        alert.getLong("created"),
                        alert.getLong("updated")
                    );
                }
            }
        }
    }

    private String makeApiRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }
}
