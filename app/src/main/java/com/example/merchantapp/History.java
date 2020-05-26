package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {

    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private Adapter adapter;
    List<ModelClass> modelClassesList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    String MyToken,UserId;
    ProgressDialog dialog;
    TextView successText;
    Double d1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


       // Log.d("fewfwfe",successText.getText().toString());

        dialog = ProgressDialog.show(History.this, "", "Loading...", true);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        Intent intent = getIntent();
        MyToken = intent.getStringExtra("token");
        UserId = intent.getStringExtra("username");

        mQueue = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        modelClassesList = new ArrayList<>();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


       // modelClassesList.add(new ModelClass("Transaction ID: ", "TID:  ","RRN: " ,"₹"));
       // adapter.notifyDataSetChanged();
        jsonParse();

    }

    private void jsonParse() {
        String url = "https://europa-sandbox.perseuspay.com/mportal/v1/transactionhistory";
        Log.d("Inside","Request");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                   // JSONArray LTS = response.getJSONArray("historydata");


                    for(int i=0;i<10;i++){

                        Log.d("Inside","Loop");

                        String s =  response.getJSONArray("historydata").getString(i);

                        if(!s.equals("")){
                            dialog.dismiss();
                        }

                        String a=new JSONObject(s).getString("transactionid");
                          Log.d("transaction id is",a);

                        String b = new JSONObject(s).getString("status");
                        Log.d("Status is",b);
                        if(b.equals("00")){
                            b = "Approved";
//
                        }
                        else
                        {
                            b = "Declined";
                        }
                        String c = new JSONObject(s).getString("transactiontype");
                        if(c.equals("00")){
                            c = "Sale";
                        }
                        else if(c.equals("09")){
                            c = "Cash @ POS";
                        }
                        else if(c.equals("63")){
                            c = "BQR";
                        }
                        Log.d("rrn is",a);
                        String d = new JSONObject(s).getString("amount");


                        Log.d("Amount is",d);
//                        Float f1 = Float.parseFloat(d);
//                        f1 = f1 / 100;


                        d1 = Double.parseDouble(d)/100 ;
                        d = d1.toString();


                        //  double amount = 15;
                        NumberFormat formatter = new DecimalFormat("#0.00");
                        // System.out.println("The Decimal Value is:"+formatter.format(d));
                        d = formatter.format(d1);





                        modelClassesList.add(new ModelClass("Transaction ID: " +a, "Status:  "+b,"Transaction Type: " +c,"₹" +d));
                        Adapter adapter = new Adapter(modelClassesList);
                        recyclerView.setAdapter(adapter);
                    }


                }

                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No Data Found",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "No Data Found",Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", MyToken );
                headers.put("apikey", "perseus");
                headers.put("userid", UserId);
                return headers;
            }
        };

        mQueue.add(request);

    }



}