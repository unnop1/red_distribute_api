package com.nt.red_distribute_api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.client.GafranaClient;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class GafranaService {

    @Value("${grafana.api.key}")
    private String apiKey;

    @Value("${grafana.url}")
    private String grafanaUrl;

    public void ExportAlertAlarm(HttpServletResponse response, String exportType) throws IOException{
        GafranaClient client = new GafranaClient(grafanaUrl, apiKey );

        // Step 1: Retrieve all alert IDs
        List<Integer> alertIds = client.getAllAlertIds();

        // Step 2: Retrieve alert history for each alert ID and write to type file
        switch (exportType) {
            case "csv":
                client.writeAlertHistoryToCsv(alertIds, response);
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=\"alert_history.csv\"");
                break;
            case "text":
                client.writeAlertHistoryToCsv(alertIds, response);
                response.setContentType("text/plain");
                response.setHeader("Content-Disposition", "attachment;filename=alert_history.txt");
                break;
            default:
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=\"alert_history.csv\"");
                break;
        }
        
        
    }
}
