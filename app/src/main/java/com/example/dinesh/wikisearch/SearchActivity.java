package com.example.dinesh.wikisearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;



import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText txtSearch, txtResult;
    private Button btnSearch;
    private ListView listView;
    private String[] imageURLArray = {};
    private String apiURL = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50&pilimit=50&generator=prefixsearch&gpssearch=";
    private static String getSearchString = "getSearchString";
    private static String jsonResult = "query";
    private Activity act;
    ArrayList<ImageStore> pgList;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtResult = (EditText) findViewById(R.id.JSONResult);
        txtResult.setText("abc.....");
        listView = (ListView)this.findViewById(R.id.listView);
        pgList = new ArrayList<ImageStore>();
        adapter = new ImageAdapter(getApplicationContext(), R.layout.listview, pgList);
        addListenerOnButton();
        //imageAdapter = new ImageAdapter(this,  imageUrls);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), pgList.get(position).getPgTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy(){
        listView.setAdapter(null);
        super.onDestroy();
    }

    public void addListenerOnButton() {

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String value1 = txtSearch.getText().toString().trim();
                Toast.makeText(getApplicationContext(), String.valueOf( apiURL + value1), Toast.LENGTH_LONG).show();
                // apiURL += value1;
                AsyncExtender jp = new AsyncExtender();
                jp.execute( apiURL + value1 );


            }
        });

    }


    private class AsyncExtender extends AsyncTask<String, Void, Boolean> {
        InputStream inStream = null;
        JSONObject json = null;
        String outPut = "";
        HttpURLConnection urlConnection = null;
        URL url = null;
        Exception mException = null;
        ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SearchActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        protected Boolean doInBackground(String... apiUrl) {

            String response = "";
            Boolean output = false;
            try {
                url = new URL(apiUrl[0].toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.d("JSON", stringBuilder.toString());
                    output = JSONParser(stringBuilder.toString());
                    return output;
                } finally {
                    urlConnection.disconnect();

                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
            }
            return output;
        }

        protected void onPostExecute(Boolean response) {
            dialog.cancel();
            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            if(response == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();


        }

        public Boolean JSONParser(String response) {
            String output = "";
            try {
                JSONObject query = (new JSONObject(response)).getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");

                imageURLArray = new String[pages.length()];
                int i = 0;
                Iterator pgids = pages.keys();
                while (pgids.hasNext()) {
                    ImageStore pgStore = new ImageStore();
                    String key = (String) pgids.next();
                    JSONObject singlePage = pages.getJSONObject(key);
                    String pageid = singlePage.getString("pageid");
                    String title = singlePage.getString("title");
                    JSONObject thumbnail = singlePage.getJSONObject("thumbnail");
                    String imageURL = thumbnail.getString("source");
                    String width = thumbnail.getString("width");
                    String height = thumbnail.getString("height");
                    String pagedata = " \npageid : " + pageid + " \ntitle : " + title + " \nImageURL : " + imageURL + " \nwidth : " + width + "\nheight : " + height;
                    output += pagedata;
                    pgStore.setPgId(singlePage.getInt("pageid"));
                    pgStore.setPgTitle(singlePage.getString("title"));
                    pgStore.setImgSrc(thumbnail.getString("source"));
                    pgStore.setImgWidth(thumbnail.getInt("width"));
                    pgStore.setImgHeight(thumbnail.getInt("height"));
                    pgList.add(pgStore);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                output = "Failed to retrieve data";
            }
            return false;
        }
    }
}



