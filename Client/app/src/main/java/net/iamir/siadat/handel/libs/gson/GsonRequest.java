package net.iamir.siadat.handel.libs.gson;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private static final String TAG = "GsonRequest";
    private Gson gson = new Gson();
    private Response.Listener<T> responseListener;
    private String requestBody;
    private T result;
    private Type type;
    private Boolean single;

    public GsonRequest(int method, String url, String requestBody, Response.Listener<T> responseListener, Response.ErrorListener listener, Type type, Boolean single) {
        super(method, url, listener);
        this.responseListener = responseListener;
        this.requestBody = requestBody;
        this.type = type;
        this.single = single;
        setRetryPolicy(new DefaultRetryPolicy(16000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
            result = gson.fromJson(single ? jsonObject.getJSONObject("data").toString() : jsonObject.getJSONArray("data").toString(), type);
            return Response.success(result, null);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "parseNetworkResponse: " + e.toString());
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            Log.e(TAG, "parseNetworkResponse: " + e.toString());
        }
        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        responseListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (requestBody == null) {
            return super.getBody();
        } else {
            return requestBody.getBytes();
        }

    }
}
