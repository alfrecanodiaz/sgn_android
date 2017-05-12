package zentcode02.parks.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class CustomObjectRequest {

    private Context mContext;

    public CustomObjectRequest(Context context) {
        mContext = context;
    }

    public interface ServerCallback {
        void onResponse(JSONObject response);
        void onErrorResponse();
    }

    public void SendRequest(int method, String url, final String body, final ServerCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try  {
                    callback.onResponse(response);
                } catch (Exception ex)  {
                    Log.d(Config.REQUEST_EXCEPTION_TAG, ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = error == null ? "error null" : error.getMessage();
                message = message == null ? error.toString() : message;
                Log.d(Config.REQUEST_EXCEPTION_TAG, message );
                callback.onErrorResponse();
            }
        })
        {
            @Override
            public byte[] getBody() {
                Log.d("body response ", body);
                return body.getBytes();
            }
        };
        jsonObjectRequest.setShouldCache(false);
        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        VolleyRequestQueue.getInstance(mContext).getRequestQueue().add(jsonObjectRequest);
    }
}
