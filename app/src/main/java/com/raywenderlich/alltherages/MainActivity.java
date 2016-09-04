/*
 * Copyright (c) 2015 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.alltherages;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity implements RageComicListFragment.OnRageComicSelected {

    byte[] b=null;
    String strJson;
    ArrayList<String> names=new ArrayList<>();
    ArrayList<String> country=new ArrayList<>();
    ArrayList<String> house=new ArrayList<>();
    ArrayList<String> years=new ArrayList<>();
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start=(Button) findViewById(R.id.button);
        setContentView(R.layout.activity_main);
        GetURLTask getURLTask = new GetURLTask(this, "http://mysafeinfo.com/api/data?list=englishmonarchs&format=json");
        try {
            strJson=getURLTask.execute().get(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        byte[] bytes=strJson.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        JsonReader jsonReader=new JsonReader(new InputStreamReader(stream));
        try {
            jsonReader.beginArray();
            while(jsonReader.hasNext()){
                jsonReader.beginObject();
                jsonReader.nextName();
                names.add(jsonReader.nextString());
                jsonReader.nextName();
                country.add(jsonReader.nextString());
                jsonReader.nextName();
                house.add(jsonReader.nextString());
                jsonReader.nextName();
                years.add(jsonReader.nextString());
                jsonReader.endObject();
            }
            jsonReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount()==0){
            start=(Button) findViewById(R.id.button);
            start.setVisibility(View.VISIBLE);
        }
    }

    public void getExercise(View view) throws MalformedURLException {
        start=(Button) findViewById(R.id.button);
        start.setVisibility(View.INVISIBLE);
        Bundle bundle=new Bundle();
        bundle.putSerializable("country",country);
        bundle.putSerializable("house",house);
        bundle.putSerializable("years",years);
        bundle.putSerializable("names",names);
        RageComicListFragment rageComicListFragment=RageComicListFragment.newInstance();
        rageComicListFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.root_layout,rageComicListFragment, "rageComicList")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onRageComicSelected(String name, String year, String city,String house) {
        final RageComicDetailsFragment detailsFragment =
                RageComicDetailsFragment.newInstance(name, year, city,house);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, detailsFragment, "rageComicDetails")
                .addToBackStack(null)
                .commit();
    }


    public class GetURLTask extends AsyncTask<Void, Void, String> {
        private Context mContext;
        private String mUrl;

        public GetURLTask(Context context, String url) {
            mContext = context;
            mUrl = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            String resultString = null;
            resultString = getJSON(mUrl);
            strJson=resultString;

            return resultString;
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            strJson=strings;
        }

        public String getJSON(String url) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int status = c.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        //disconnect error
                    }
                }
            }
            return null;
        }
    }
}
