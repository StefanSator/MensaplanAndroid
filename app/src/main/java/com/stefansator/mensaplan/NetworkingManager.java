package com.stefansator.mensaplan;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Class which is responsible for the Networking Communication with the Backend of the App.
 * @author stefansator
 * @version 1.0
 */
public class NetworkingManager {
    /** Singleton instance of a NetworkingManager. */
    private static NetworkingManager shared;
    /** The RequestQueue to which the backend requests get added. */
    private RequestQueue requestQueue;
    /** The current application context. */
    private static Context context;
    //private String backendURL = "https://www.stwno.de/infomax/daten-extern/csv/UNI-R/";
    /** The URL of the backend service. */
    private String backendURL = "https://young-beyond-20476.herokuapp.com";

    /**
     * Constructor.
     * @param context The current application context.
     */
    private NetworkingManager(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Get the Singleton Instance of the NetworkingManager or create a new one, with the current
     * application context, if the instance is null.
     * @param context The current application context.
     * @return NetworkingManager Singleton instance of a NetworkingManager.
     */
    public static synchronized NetworkingManager getInstance(Context context) {
        if (shared == null) {
            shared = new NetworkingManager(context);
        }
        return shared;
    }

    /**
     * Get the RequestQueue or create a new one if the instance is null.
     * @return RequestQueue The Queue holding all requests.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Add Backend Requests to the Request Queue.
     * @param request The request to add to the queue.
     * @param <T> Type of the request.
     */
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    /**
     * Get the URL of the Backend Service.
     * @return String URL.
     */
    public String getBackendURL() {
        return backendURL;
    }
}
