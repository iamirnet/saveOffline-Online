package net.iamir.siadat.handel.services.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import net.iamir.siadat.handel.libs.gson.GsonRequest;
import net.iamir.siadat.handel.libs.gson.RequestQueueContainer;
import net.iamir.siadat.handel.models.Record;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class ApiService {
    private static final String TAG = "ApiService";
    public static final String Domain = "school.raya.pw";
    public static final String BASE_URL = "https://" + Domain + "/api/v1/";
    private static final String ENDPOINT_RECORDS = "siadat/records";
    private Context context;

    public ApiService(Context context) {
        this.context = context;
    }


    public void getRecords(int page,String query, Response.Listener<List<Record>> listener, Response.ErrorListener errorListener) {
        GsonRequest<List<Record>> gsonRequest;
        gsonRequest = new GsonRequest<>(Request.Method.GET,
                BASE_URL + ENDPOINT_RECORDS + "?page="+ page+ "&q="+ query,
                null,
                listener,
                errorListener,
                new TypeToken<List<Record>>() {
                }.getType(), false);
        RequestQueueContainer.getRequestQueue(context).add(gsonRequest);
    }

    public void  saveRecord(Record record,  Response.Listener<Record> listener, Response.ErrorListener errorListener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", record.getRecordName());
            requestBody.put("family", record.getRecordFamily());
            requestBody.put("level", record.getRecordLevel());
            if (record.getRecordId() != null) {
                requestBody.put("_method", "put");
            }
        } catch (JSONException e) {
            Log.e(TAG, "saveRecord" + e.toString());
        }
        String url = BASE_URL + ENDPOINT_RECORDS;
        if (record.getRecordId() != null) {
            url += "/" + record.getRecordId();
        }
        GsonRequest<Record> gsonRequest;
        gsonRequest = new GsonRequest<>(Request.Method.POST,
                url,
                requestBody.toString(),
                listener,
                errorListener,
                new TypeToken<Record>() {
                }.getType(), true);
        RequestQueueContainer.getRequestQueue(context).add(gsonRequest);
    }

    public void delRecord(Record record,  Response.Listener<Record> listener, Response.ErrorListener errorListener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("_method", "delete");
        } catch (JSONException e) {
            Log.e(TAG, "deleteRecord" + e.toString());
        }
        String url = BASE_URL + ENDPOINT_RECORDS;
        if (record.getRecordId() != null) {
            url += "/" + record.getRecordId();
        }
        GsonRequest<Record> gsonRequest;
        gsonRequest = new GsonRequest<>(Request.Method.POST,
                url,
                requestBody.toString(),
                listener,
                errorListener,
                new TypeToken<Record>() {
                }.getType(), true);
        RequestQueueContainer.getRequestQueue(context).add(gsonRequest);
    }

}
