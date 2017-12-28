package com.apps.gluonix.googleinapppurchase;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    BillingProcessor bp;
    public TextView textView;
    public Button pay_premium, pay_activity, view_activity, view_premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String PLAY_LICENSE = "your_license";
        bp = new BillingProcessor(this, PLAY_LICENSE, this);

//      If you want to test use
//      bp = new BillingProcessor(this, null, this);
        // textView = (TextView)findViewById(R.id.textView);

        pay_premium = (Button)findViewById(R.id.pay_premium);
        pay_activity = (Button)findViewById(R.id.pay_activity);
        view_premium = (Button)findViewById(R.id.view_premium);
        view_activity = (Button)findViewById(R.id.view_activity);

        view_premium.setVisibility(View.INVISIBLE);
        view_activity.setVisibility(View.INVISIBLE);

        pay_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.subscribe(MainActivity.this,"android.test.purchased");
            }
        });

        pay_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.purchase(MainActivity.this,"android.test.purchased");
            }
        });

        view_premium.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, PremiumActivity.class);
                startActivity(intent);
            }
        });

        view_activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, PremiumActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, "Your purchase was successful", Toast.LENGTH_SHORT).show();
        // send to api purchasetoken, productid, date, orderid, developerpayload, purchasestate
        // new setPurchaseAPI(details).execute();

        //https://developers.google.com/android-publisher/api-ref/purchases/products
        //https://developers.google.com/android-publisher/api-ref/purchases/subscriptions
        view_premium.setVisibility(View.VISIBLE);
        view_activity.setVisibility(View.VISIBLE);
        pay_premium.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        if (errorCode == 101){
            Toast.makeText(this, "You dont have a Google Account", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Something were wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!bp.handleActivityResult(requestCode,resultCode,data)){
            super.onActivityResult(requestCode, resultCode,data);
        }
    }

    @Override
    public void onDestroy(){
        if (bp != null){
            bp.release();
        }
        super.onDestroy();
    }
}

class setPurchaseAPI extends AsyncTask<String, TransactionDetails, String> {

    private static final String API_URL = "http://example.com/api/v3/purchase?";
    private static final String API_KEY = "api_key";
    private TransactionDetails details;

    public setPurchaseAPI(TransactionDetails d){
        this.details = d;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            TransactionDetails d = details;

            HttpClient client = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost(API_URL + "order_id=" + details.orderId);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.addHeader("Authorization", "Token token=\"" + API_KEY + "\"");

//            try {
//                HttpResponse response = client.execute(httpGet);
//                InputStream inputStream = response.getEntity().getContent();
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//                }
//                bufferedReader.close();
//                return stringBuilder.toString();
//            }
//            finally{
//            }
            return null;
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

    }

}

