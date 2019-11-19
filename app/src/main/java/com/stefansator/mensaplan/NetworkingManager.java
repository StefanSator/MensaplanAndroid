package com.stefansator.mensaplan;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetworkingManager {
    private static NetworkingManager shared;
    private RequestQueue requestQueue;
    private static Context context;
    private String backendURL = "https://www.stwno.de/infomax/daten-extern/csv/UNI-R/";

    private NetworkingManager(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkingManager getInstance(Context context) {
        if (shared == null) {
            shared = new NetworkingManager(context);
        }
        return shared;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToStringRequestQueue(StringRequest request) {
        getRequestQueue().add(request);
    }

    public String getBackenURL() {
        return backendURL;
    }
}
