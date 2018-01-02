package com.apps.gluonix.googleinapppurchase;

import android.app.Activity;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static com.apps.gluonix.googleinapppurchase.R.id.activityText;
import static com.apps.gluonix.googleinapppurchase.R.id.parallax;
import static com.apps.gluonix.googleinapppurchase.R.id.textView;

public class PremiumActivity extends AppCompatActivity {

    private TextView textView;
    String activity_text;
    TextView activityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        textView = (TextView)findViewById(R.id.textView);
        Button b = (Button)findViewById(R.id.queryButton);

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                activityText = (TextView)findViewById(R.id.activityText);
                activity_text = activityText.getText().toString();

//              getActivitiesAPI mytask = new getActivitiesAPI(this);
                new getActivitiesAPI().execute(activity_text);
            }
        });

    }

}

class getActivitiesAPI extends AsyncTask<String, String, String> {

    private static final String API_URL = "http://example.com/api/v3/activities?";
    private static final String API_KEY = "";
    //    public Activity activity;
    //    private String activity_text;
    //
    //    public getActivitiesAPI(){
    //        this.activity_text = params[0];
    //    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String activity_text = params[0];

            HttpClient client = new DefaultHttpClient(new BasicHttpParams());
            HttpGet httpGet = new HttpGet(API_URL + "per_page=10&page=1&locale=en&q=" + activity_text);
            httpGet.setHeader("Content-type", "application/json");
            httpGet.addHeader("Authorization", "Token token=\"" + API_KEY + "\"");

            try {
                HttpResponse response = client.execute(httpGet);
                InputStream inputStream = response.getEntity().getContent();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);
//        TextView responseView = (TextView) activity.findViewById(R.id.responseView);
//        responseView.setText(response);
    }

}
