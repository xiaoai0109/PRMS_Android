package sg.edu.nus.iss.phoenix.schedule.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sg.edu.nus.iss.phoenix.schedule.android.controller.ReviewSelectScheduleController;
import sg.edu.nus.iss.phoenix.schedule.android.controller.ScheduleController;
import sg.edu.nus.iss.phoenix.schedule.entity.ProgramSlot;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_RADIO_PROGRAM;
import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_SCHEDULE;

/**
 * Created by mia on 2/9/18.
 */

public class RetrieveSchedulesDelegate extends AsyncTask<String, Void, String> {
    // Tag for logging
    private static final String TAG = RetrieveSchedulesDelegate.class.getName();

    private ScheduleController scheduleController = null;
    private ReviewSelectScheduleController reviewSelectScheduleController = null;

    public RetrieveSchedulesDelegate(ScheduleController scheduleController) {
        this.reviewSelectScheduleController = null;
        this.scheduleController = scheduleController;
    }

    public RetrieveSchedulesDelegate(ReviewSelectScheduleController reviewSelectScheduleController) {
        this.scheduleController = null;
        this.reviewSelectScheduleController = reviewSelectScheduleController;
    }

    @Override
    protected String doInBackground(String... params) {
        Uri builtUri1 = Uri.parse(PRMS_BASE_URL_SCHEDULE).buildUpon().build();
        Uri builtUri = Uri.withAppendedPath(builtUri1, params[0]).buildUpon().build();
        Log.v(TAG, builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.v("retrieve schedule", url.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return e.getMessage();
        }

        String jsonResp = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) jsonResp = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return jsonResp;
    }

    @Override
    protected void onPostExecute(String result) {
        List<ProgramSlot> programSlots = new ArrayList<>();

        if (result != null && !result.equals("")) {
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray psArray = reader.getJSONArray("psList");

                Log.e(TAG, "loading ps psArray " + psArray);
                for (int i = 0; i < psArray.length(); i++) {
                    JSONObject psJson = psArray.getJSONObject(i);
                    String id = psJson.getString("id");
                    String rpname = psJson.getString("rpname");
                    String date = psJson.getString("date");
                    String sttime = psJson.getString("sttime");
                    String duration = psJson.getString("duration");
                    String presenter = psJson.getString("presenter");
                    String producer = psJson.getString("producer");
                    Log.d(TAG, "loading ps jsonObject " + psJson);
                    programSlots.add(new ProgramSlot(id, rpname, date, sttime, duration, presenter, producer));
                }
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "JSON response error.");
        }

        if (scheduleController != null)
            scheduleController.schedulesRetrieved(programSlots);
//        else if (reviewSelectScheduleController != null)
//            reviewSelectScheduleController.programsRetrieved(programSlots);
    }
}

