package com.vijayjaidewan01vivekrai.jsonparsing_github;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> arrayList;
    ListAdapter adapter;

    String url = "http://bydegreestest.agnitioworld.com/test/slider.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       listView = findViewById(R.id.list_view);
       arrayList = new ArrayList<>();

        new GetData().execute();


    }

    private class GetData extends AsyncTask<Void, Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String jsonString = handler.makeServiceRequest(url);

            if (jsonString != null){


                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("contacts");

                    for (int i=0 ; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String name = jsonObject1.getString("name");
                        String email = jsonObject1.getString("email");

                        hashMap = new HashMap<>();
                        hashMap.put("name", name);
                        hashMap.put("email", email);
                        arrayList.add(hashMap);
                    }
                } catch (final Exception e) {
                    Log.e("Erro", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();


                        }
                    });

                }


            }else {
                Log.e("Error", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check Logcat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            adapter = new SimpleAdapter(MainActivity.this, arrayList, R.layout.list_items, new String[]{"name", "email"}, new int[]{R.id.name, R.id.email});
            listView.setAdapter(adapter);
            super.onPostExecute(aVoid);
        }
    }
}
