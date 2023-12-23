package com.example.myapplication.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GoogleCustomSearchApi {
    private static final String API_KEY = "AIzaSyCZz5Ax1zkWHGCrtiKLzEOpVzOvk0_NePg";
    private static final String SEARCH_ENGINE_ID = "b68176ff6a4de4b18";

    private static final String BASE_URL = "https://www.googleapis.com/customsearch/v1";

    public static void search(String query, int numberOfResults, JsonHttpResponseHandler responseHandler, String languageCode) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", API_KEY);
        params.put("cx", SEARCH_ENGINE_ID);
        params.put("q", query);
        params.put("num", numberOfResults);

        if (languageCode != null && !languageCode.isEmpty()) {
            params.put("lr", "lang_"+languageCode);
        }

        client.get(BASE_URL, params, responseHandler);
    }
}
