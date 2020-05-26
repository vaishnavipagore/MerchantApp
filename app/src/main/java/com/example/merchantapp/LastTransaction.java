package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.parseFloat;

public class LastTransaction extends AppCompatActivity {

    private TextView mTextViewResults, tw, mTextView, Amount, Authcode, Cardtype, Batch, Card, MID, PAN, Product, RRN, TID, TransactionID;
    private Button button;
    private RequestQueue mQueue;
    String MyToken,UserId;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_transaction);


        Intent intent = getIntent();
        MyToken = intent.getStringExtra("token1");
        UserId = intent.getStringExtra("userid1");

        Log.d("Username is",UserId);
        Log.d("Token is ",MyToken);
        dialog = ProgressDialog.show(LastTransaction.this, "", "Loading...", true);
        mQueue = Volley.newRequestQueue(this);
        Amount = findViewById(R.id.amount);
//        authcode =findViewById(R.id.authcode);
        Cardtype = findViewById(R.id.cardtype);
        Authcode = findViewById(R.id.authcode);
        Batch = findViewById(R.id.batchno);
        Card = findViewById(R.id.card);
        MID = findViewById(R.id.mid);
        PAN = findViewById(R.id.pan);
        Product = findViewById(R.id.product);
        RRN = findViewById(R.id.rrn);
        TID = findViewById(R.id.tid);
        TransactionID = findViewById(R.id.transaction);
//        button = findViewById(R.id.btn);
//        button.setText("<");
        jsonParse();





    }


    private void jsonParse () {
        String url = "https://europa-sandbox.perseuspay.com/mportal/v1/lasttransactionstatus";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONArray LTS = response.getJSONArray("ltsdata");
                    Log.d("LTS", String.valueOf(LTS));

                    dialog.dismiss();
                    //  Log.d("Length is ", String.valueOf(n));

                    String s = response.getJSONArray("ltsdata").getString(0);
                    String auth = new JSONObject(s).getString("authcode");
                    String cardtype = new JSONObject(s).getString("cardtype");
                    Cardtype.append(cardtype);
                    String amount = new JSONObject(s).getString("amount");
                    Float amount1 = parseFloat(amount) /100;

                    Amount.append(amount1.toString());
                    
                    String authcode = new JSONObject(s).getString("authcode");
                    if(authcode.equals("")){
                        authcode = "-";
                    }
                    Authcode.append(authcode);
                    String batch = new JSONObject(s).getString("batchno");
                    Batch.append(batch);

                    Card.append(cardtype);
                    String mid = new JSONObject(s).getString("mid");
                    MID.append(mid);

                    String pan = new JSONObject(s).getString("pan");
                    PAN.append(pan);
                    String product = new JSONObject(s).getString("producttype");
                    Product.append(product);
                    String rrn = new JSONObject(s).getString("rrn");
                    RRN.append(rrn);
                    String tid = new JSONObject(s).getString("tid");
                    TID.append(tid);
                    String transaction = new JSONObject(s).getString("transactionid");
                    TransactionID.append(transaction);


                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
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
        mQueue.add(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
