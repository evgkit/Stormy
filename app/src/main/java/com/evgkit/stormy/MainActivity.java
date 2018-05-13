package com.evgkit.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private final OkHttpClient client = new OkHttpClient();

    private CurrentWeather currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isNetworkAvailable()) {
            alertUserAboutError();
            return;
        }

        String apiKey = "111b4406c7757474f9e4ba2bee689f93";
        Double latitude = 59.9059;
        Double longitude = 30.5130;

        String forecastURL = String.format(
                Locale.US,
                "https://api.darksky.net/forecast/%s/%f,%f",
                apiKey, latitude, longitude
        );

        Request request = new Request.Builder()
                .url(forecastURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                alertUserAboutError();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    if (responseBody != null) {
                        currentWeather = getCurrentDetails(responseBody.string());
                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    alertUserAboutError();
                }
            }
        });
    }

    private CurrentWeather getCurrentDetails(String jsonAsString) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(jsonAsString);
            CurrentWeather currentWeather = new CurrentWeather();
            currentWeather.setHumidity(jsonObject.getJSONObject("currently").getDouble("humidity"));
            currentWeather.setIcon(jsonObject.getJSONObject("currently").getString("icon"));
            currentWeather.setLocationLabel("CouldRaw Country");
            currentWeather.setPrecipChance(jsonObject.getJSONObject("currently").getDouble("precipProbability"));
            currentWeather.setSummary(jsonObject.getJSONObject("currently").getString("summary"));
            currentWeather.setTime(jsonObject.getJSONObject("currently").getLong("time"));
            currentWeather.setTemperature(jsonObject.getJSONObject("currently").getDouble("temperature"));
            return currentWeather;
        } catch (JSONException e) {
            throw new IOException(e.getMessage());
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    private void alertUserAboutError() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getFragmentManager(), "error_dialog");
    }
}