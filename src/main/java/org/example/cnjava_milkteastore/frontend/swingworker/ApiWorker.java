package org.example.cnjava_milkteastore.frontend.swingworker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class ApiWorker<T> extends SwingWorker<T, Void> {

    private final String apiUrl;
    private final String method;
    private final String jsonBody;
    private final TypeReference<T> typeRef;
    private final Consumer<T> onSuccess;
    private final Runnable onStart;
    private final Runnable onFinish;
    private final Consumer<Exception> onError;



    public ApiWorker(String apiUrl,
                     String method,
                     String jsonBody,
                     TypeReference<T> typeRef,
                     Consumer<T> onSuccess,
                     Runnable onStart,
                     Runnable onFinish,
                     Consumer<Exception> onError) {
        this.apiUrl = apiUrl;
        this.method = method;
        this.jsonBody = jsonBody;
        this.typeRef = typeRef;
        this.onSuccess = onSuccess;
        this.onStart = onStart;
        this.onFinish = onFinish;
        this.onError = onError;
    }


    public static String convertObjectToJson(Object obj) {
        try {
            if (obj == null) return null;

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Để ISO 8601 thay vì timestamp

            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("❌ Không thể convert object thành JSON", e);
        }
    }


    @Override
    protected T doInBackground() throws Exception {
        if (onStart != null) SwingUtilities.invokeLater(onStart);

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        if (jsonBody != null && !jsonBody.isEmpty()) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes("UTF-8"));
            }
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) response.append(line);
        }

        if (status < 200 || status >= 300) {
            throw new RuntimeException("API Error (" + status + "): " + response);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper.readValue(response.toString(), typeRef);
    }


    @Override
    protected void done() {
        if (onFinish != null) SwingUtilities.invokeLater(onFinish);

        try {
            T result = get();
            if (onSuccess != null) onSuccess.accept(result);
        } catch (Exception e) {
            if (onError != null) onError.accept(e);
            else e.printStackTrace();
        }
    }
}
