package com.example.merchantapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.parseFloat;

public class TransactionCount extends AppCompatActivity {


    String MyToken,UserId;
    TextView count,amount,countyesterday,amountyesterday,todaydate,yesterday;
    ProgressDialog dialog;
    Button Top10;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_count);
         dialog = ProgressDialog.show(TransactionCount.this, "", "Loading...", true);

        Intent intent = getIntent();
        MyToken = intent.getStringExtra("token2");
        UserId = intent.getStringExtra("userid2");
        count = (TextView ) findViewById(R.id.count);
        amount = (TextView) findViewById(R.id.amount);

        countyesterday = (TextView) findViewById(R.id.countyes);
        amountyesterday =(TextView) findViewById(R.id.amountyes);
        todaydate = (TextView) findViewById(R.id.todaydate);
        yesterday = (TextView) findViewById(R.id.yesterdaydate);

        Log.d("tctoken",MyToken);
        Log.d("tcuserid",UserId);

        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Log.d("Date is",currentDateTimeString);

       // currentDateTimeString = currentDateTimeString.substring(0,10);
       // todaydate.setText(currentDateTimeString);
        Date currentDate = new Date(System.currentTimeMillis());
        String current = currentDate.toString();
        todaydate.setText(current.substring(0,10));

        Date yes = new Date(System.currentTimeMillis()-24*60*60*1000);
        String yesterdaydate = yes.toString().substring((int) 0.10);
        yesterday.setText(yesterdaydate.substring(0,10));
        Log.d("Yesterday", String.valueOf(yes).substring(0,10));


        CardView cardView = (CardView) findViewById(R.id.cv_one_login);
        Top10 = (Button) findViewById(R.id.top10);

        Top10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransactionCount.this, History.class);
                i.putExtra("token",MyToken);
                i.putExtra("username",UserId);
                startActivity(i);
            }
        });

//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(TransactionCount.this, History.class);
//                i.putExtra("token",MyToken);
//                i.putExtra("username",UserId);
//                startActivity(i);
//            }
//        });

        jsonParse();
        jsonParse2();
    }

    private void jsonParse() {


        String url = "https://europa-sandbox.perseuspay.com/mportal/v1/todaysummary";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String s =  response.getJSONArray("todaysummarydata").getString(1);
                    if(!s.equals("")){
                        dialog.dismiss();
                    }

                    String name=new JSONObject(s).getString("name");
                    String count1 = new JSONObject(s).getString("count");
                    String amount1 = new JSONObject(s).getString("amount");
                    if(!count1.equals("")){
                        count.setText("Count: "+count1);
                    }
                    else
                    {
                        count.setText("Count: -");
                    }


                    if(!amount1.equals("")){
                        amount.setText("Amount: "+amount1);
                    }
                    else
                    {
                        amount.setText("Amount: -");
                    }

                    Log.d("response is ", String.valueOf(name));

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", MyToken);
                headers.put("apikey", "perseus");
                headers.put("userid", UserId);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }


    private void jsonParse2() {


        String url = "https://europa-sandbox.perseuspay.com/mportal/v1/transactionsummary";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String s =  response.getJSONArray("transummarydata").getString(1);
                    String name=new JSONObject(s).getString("name");
                    String count1 = new JSONObject(s).getString("count");
                    String amount1 = new JSONObject(s).getString("amount");
                    if(!count1.equals("")){
                        countyesterday.setText("Count: "+count1);
                    }
                    else
                    {
                        countyesterday.setText("Count: -");
                    }


                    if(!amount1.equals("")){
                        amountyesterday.setText("Amount: "+amount1);
                    }
                    else
                    {
                        amountyesterday.setText("Amount: -");
                    }

                    Log.d("response is ", String.valueOf(name));

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", MyToken);
                headers.put("apikey", "perseus");
                headers.put("userid", UserId);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }


}
