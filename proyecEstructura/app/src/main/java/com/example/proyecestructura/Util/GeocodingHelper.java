package com.example.ED_P1_Grupo02.Util;

import android.app.DownloadManager;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeocodingHelper {
    public interface GeocodingCallback {
        void onSuccess(double lat, double lng);
        void onFailure(String error);
    }

    public static void obtenerCoordenadas(String direccion, String apiKey, GeocodingCallback callback) {
        new AsyncTask<String, Void, Void>() {
            double lat = 0, lng = 0;
            String error = null;

            @Override
            protected Void doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        direccion.replace(" ", "+") + "&key=" + apiKey;
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        error = "HTTP error " + response.code();
                        return null;
                    }
                    String body = response.body().string();
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    if (results.length() > 0) {
                        JSONObject location = results.getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location");
                        lat = location.getDouble("lat");
                        lng = location.getDouble("lng");
                    } else {
                        error = "No se encontraron resultados";
                    }
                } catch (Exception e) {
                    error = e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (error != null) {
                    callback.onFailure(error);
                } else {
                    callback.onSuccess(lat, lng);
                }
            }
        }.execute(direccion);
    }
}
