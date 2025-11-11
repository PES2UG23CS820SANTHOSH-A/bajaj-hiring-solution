package com.example.demo;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class BajajService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void startProcess() {
        try {
            System.out.println("Sending request to generate webhook...");

            String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            Map<String, String> body = new HashMap<>();
            body.put("name", "John Doe");
            body.put("regNo", "REG12347");
            body.put("email", "john@example.com");

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(generateUrl, body, Map.class);
            Map<String, Object> response = responseEntity.getBody();

            if (response == null) {
                System.out.println("Failed to receive a valid response from webhook generation.");
                return;
            }

            String webhookUrl = response.get("webhook").toString();
            String accessToken = response.get("accessToken").toString();

            System.out.println("Webhook generated successfully!");
            System.out.println("Webhook URL: " + webhookUrl);
            System.out.println("Access Token: " + accessToken);

            String finalQuery =
                "SELECT p.amount AS SALARY, " +
                "CONCAT(e.first_name, ' ', e.last_name) AS NAME, " +
                "TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS AGE, " +
                "d.department_name AS DEPARTMENT_NAME " +
                "FROM payments p " +
                "JOIN employee e ON p.emp_id = e.emp_id " +
                "JOIN department d ON e.department = d.department_id " +
                "WHERE DAY(p.payment_time) <> 1 " +
                "ORDER BY p.amount DESC " +
                "LIMIT 1;";

            System.out.println("Solved SQL Query Prepared:\n" + finalQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            Map<String, String> finalBody = new HashMap<>();
            finalBody.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(finalBody, headers);

            System.out.println("Sending SQL query to webhook...");
            restTemplate.postForEntity(webhookUrl, request, String.class);

            System.out.println("Successfully sent SQL query to webhook!");

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
