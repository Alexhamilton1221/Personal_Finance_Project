package com.example.personal_finance_project;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Inside ApiCallTask.java
public class ApiCallTask extends AsyncTask<String, Void, String> {

    private ApiCallback callback;

    public ApiCallTask(ApiCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String upcCode = params[0];

        try {
            String apiUrl = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + upcCode;
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                Log.d("InsertShoppingItem Using UPC", "API Response: " + response.toString());

                // Parse the response and extract the product information (product name, etc.)
                String title = extractTitleFromResponse(response.toString());
                return title;

            } else {
                Log.e("InsertShoppingItem Using UPC", "HTTP Error: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            Log.e("InsertShoppingItem Using UPC", "API ERROR: ", e);
            return null;
        }
    }

    private String extractTitleFromResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray itemsArray = jsonResponse.getJSONArray("items");
            JSONObject firstItem = itemsArray.getJSONObject(0);
            return firstItem.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String title) {
        super.onPostExecute(title);

        if (callback != null) {
            callback.onApiCallComplete(title);
        }
    }

    public interface ApiCallback {
        void onApiCallComplete(String title);
    }
}


