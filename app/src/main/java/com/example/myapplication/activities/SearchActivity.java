package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.SearchResultAdapter;
import com.example.myapplication.http.GoogleCustomSearchApi;
import com.example.myapplication.listeners.OnItemSearchResultClickListener;
import com.example.myapplication.model.SearchResult;
import com.example.myapplication.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.paperdb.Paper;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private TextView txtClean;
    private RecyclerView rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Paper.init(this);

        initView();
        initEvent();

        String search = getIntent().getStringExtra("search");
        if (search != null) {
            search(search);
            edtSearch.setText(search);
        }

    }

    private void search(String search) {
        GoogleCustomSearchApi.search(search, 10, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    List<SearchResult> list = new ArrayList<>();
                    Log.d("searchAPI",response.toString());
                    // Xử lý kết quả JSON
                    JSONArray items = response.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String title = item.getString("title");
                        String url = item.getString("link");
                        String snippet = item.getString("snippet");

                        // Trích xuất thông tin hình ảnh (nếu có)
                        JSONObject pagemap = item.optJSONObject("pagemap");
                        String imageUrl = "";
                        if (pagemap != null) {
                            JSONArray cseImages = pagemap.optJSONArray("cse_image");
                            if (cseImages != null && cseImages.length() > 0) {
                                JSONObject image = cseImages.getJSONObject(0);
                                imageUrl = image.optString("src", "N/A");
                            }
                        }

                        //domain
                        String domain = Utils.getDomain(url);

                        SearchResult searchResult = new SearchResult();
                        searchResult.setTitle(title);
                        searchResult.setUrl(url);
                        searchResult.setImgUrl(imageUrl);
                        searchResult.setSnippet(snippet);
                        if (domain != null) {
                            searchResult.setDomain(domain);
                        }
                        list.add(searchResult);
                    }
                    SearchResultAdapter adapter = new SearchResultAdapter(list, new OnItemSearchResultClickListener() {
                        @Override
                        public void onCLick(String title, String url) {
                            Intent intent = new Intent(SearchActivity.this, BrowserActivity.class);
                            intent.putExtra("success", "search");
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                    });

                    rc.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rc.setLayoutManager(linearLayoutManager);
                    rc.setAdapter(adapter);

                    setStatusSearchBar(edtSearch, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Xử lý khi gọi API thất bại
                Log.e("failure", "Status Code: " + statusCode, throwable);
            }
        }, "vi");
    }

    private void setStatusSearchBar(EditText editText, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            edtSearch.requestFocus();
            imm.showSoftInput(editText, 0);
        } else {
            edtSearch.clearFocus();
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private void initView() {
        edtSearch = findViewById(R.id.edt_search);
        txtClean = findViewById(R.id.txt_search_clean);
        rc = findViewById(R.id.rc_search_result);

        setStatusSearchBar(edtSearch, true);
    }

    private void initEvent() {
        txtClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                setStatusSearchBar(edtSearch, true);
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String searchString = edtSearch.getText().toString().trim();

                    if (!searchString.isEmpty()) {
                        boolean isLoad = searchString.contains(".com") || searchString.contains(".net") || searchString.contains(".in");
                        if (isLoad) {
                            if (searchString.contains("http") || searchString.contains("https")) {
                                Intent intent = new Intent(SearchActivity.this, BrowserActivity.class);
                                intent.putExtra("success", "load");
                                intent.putExtra("url", searchString);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SearchActivity.this, BrowserActivity.class);
                                intent.putExtra("success", "load");
                                intent.putExtra("url", "http://" + searchString);
                                startActivity(intent);
                            }
                        } else {
                            List<String> list = Paper.book().read("search");
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            boolean isExit = false;
                            if (list.contains(searchString)){
                                isExit=true;
                            }
                            if (list.size()>=7){
                                list.remove(list.size()-1);
                            }
                            if (!isExit){
                                list.add(searchString);
                                Paper.book().delete("search");
                                Paper.book().write("search",list);
                            }
                            search(searchString);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }
}