package ru.mail.jira.plugins;

import java.net.URL;
import java.util.List;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class ScriptExecutor {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Не указаны все необходимые аргументы: <URL> <TOKEN> <FILE_PATH>.");
            return;
        }

        String urlString = args[0];
        String token = args[1];
        String filePath = args[2];
        try {
            StringBuilder result = new StringBuilder();
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                result.append(shieldText(line));
                result.append("\\r\\n");
            }
            sendPostRequest(urlString, token, result.toString());
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    public static String shieldText(String str) {
        StringBuilder lineResult = new StringBuilder();
        char[] arrayFromInput = str.toCharArray();
        for (char c : arrayFromInput) {
            switch (c) {
                case '"':
                    lineResult.append("\\\"");
                    break;
                case '\\':
                    lineResult.append("\\\\");
                    break;
                default:
                    lineResult.append(c);
            }
        }
        return lineResult.toString();
    }

    public static void sendPostRequest(String urlString, String token, String scriptPath) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            String jsonInputString = String.format("{\"script\": \"%s\"}", scriptPath);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String output = jsonResponse.getString("output");
                    System.out.println(output);
                }
            } else if (responseCode >= 400 && responseCode <= 499) { // client error
                System.out.println("POST request did not work, client error");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Error response from server: " + response.toString());
                }
            } else {
                System.out.println("POST request did not work, response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
