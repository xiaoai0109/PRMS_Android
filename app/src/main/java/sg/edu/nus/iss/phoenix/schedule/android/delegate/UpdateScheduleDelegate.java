package sg.edu.nus.iss.phoenix.schedule.android.delegate;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sg.edu.nus.iss.phoenix.radioprogram.android.delegate.UpdateProgramDelegate;
import sg.edu.nus.iss.phoenix.schedule.android.controller.ScheduleController;
import sg.edu.nus.iss.phoenix.schedule.entity.ProgramSlot;

import static sg.edu.nus.iss.phoenix.core.android.delegate.DelegateHelper.PRMS_BASE_URL_SCHEDULE;

/**
 * Created by mia on 2/9/18.
 */

public class UpdateScheduleDelegate extends AsyncTask<ProgramSlot, Void, Boolean> {
    // Tag for logging
    private static final String TAG = UpdateProgramDelegate.class.getName();

    private final ScheduleController scheduleController;

    public UpdateScheduleDelegate(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
    }

    @Override
    protected Boolean doInBackground(ProgramSlot... params) {
        Uri builtUri = Uri.parse(PRMS_BASE_URL_SCHEDULE).buildUpon().build();
        builtUri = Uri.withAppendedPath(builtUri,"update").buildUpon().build();
        Log.v(TAG, builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
            return false;
        }

        JSONObject jsonNewPs = new JSONObject();
        JSONObject jsonOldPs = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
        try {
            jsonNewPs.put("id", params[0].getId());
            jsonNewPs.put("rpname", params[0].getRadioProgramName());
            jsonNewPs.put("date", params[0].getProgramSlotDate());
            jsonNewPs.put("sttime", params[0].getProgramSlotSttime());
            jsonNewPs.put("duration", params[0].getProgramSlotDuration());
            jsonNewPs.put("presenter", params[0].getProgramSlotPresenter());
            jsonNewPs.put("producer", params[0].getProgramSlotProducer());
//            jsonOldPs.put("rpname", params[1].getRadioProgramName());
//            jsonOldPs.put("date", params[1].getProgramSlotDate());
//            jsonOldPs.put("sttime", params[1].getProgramSlotSttime());
            Log.d(TAG, "Updating new ps json " + jsonNewPs);
//            Log.d(TAG, "Updating old ps json " + jsonOldPs);
//            jsonArray.put(0, jsonNewPs);
//            jsonArray.put(1, jsonOldPs);
//            Log.d(TAG, "Updating json array " + jsonArray);
        } catch (JSONException e) {
            Log.v(TAG, e.getMessage());
        }

        boolean success = false;
        HttpURLConnection httpURLConnection = null;
        DataOutputStream dos = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setDoOutput(true);
            dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeUTF(jsonNewPs.toString());
//            dos.writeUTF(jsonArray.toString());
            dos.write(512);
            Log.v(TAG, "Http POST response " + httpURLConnection.getResponseCode());
            success = true;
        } catch (IOException exception) {
            Log.v(TAG, exception.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.flush();
                    dos.close();
                } catch (IOException exception) {
                    Log.v(TAG, exception.getMessage());
                }
            }
            if (httpURLConnection != null) httpURLConnection.disconnect();
        }
        return new Boolean(success);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        scheduleController.scheduleUpdated(result.booleanValue());
    }
}
