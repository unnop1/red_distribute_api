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

    public void ExportAlertAlarm(HttpServletResponse response) throws IOException{
        GafranaClient client = new GafranaClient(grafanaUrl, apiKey );

        // Step 1: Retrieve all alert IDs
        List<Integer> alertIds = client.getAllAlertIds();

        // Step 2: Retrieve alert history for each alert ID and write to CSV
        client.writeAlertHistoryToCsv(alertIds, response);
    }
}
