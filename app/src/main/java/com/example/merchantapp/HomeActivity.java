package com.example.merchantapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import helper.Config;

import static java.lang.Float.parseFloat;


public class HomeActivity extends AppCompatActivity {


    String parsed; //Defined Globally
    //private RequestQueue mQueue;
    TextView amounttext;
    TextView amount;
    TextView mid;
    String MyToken,UserId;
    String mobilenr;
    TextView opencount,successcount,declinecount;
    TextView TransType;
    TextView cardtype;
    TextView date,status;
    ImageView img;
    Button accept,reject;
    String mobile;
    String MerchantName;
    Button salebtn,logoutbtn,historybtn,voidbtn,preauthbtn,preauthcbtn,salewithcash,BQRbtn;
    String FCMToken;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String FCM = "FCM";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        try{
            Intent intent = getIntent();
            MyToken = intent.getStringExtra("token");
            UserId = intent.getStringExtra("username");
             MerchantName = intent.getStringExtra("ownername");
             FCMToken = intent.getStringExtra("fcmToken");
            Config.FCMToken = FCMToken;
            Log.d("token in home",MyToken);
            Log.d("userid",UserId);
            Log.d("FCM Token",FCMToken);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        jsonParse();
//        Toast.makeText(getApplicationContext(),UserId,Toast.LENGTH_LONG).show();
//        Log.d("UserId",UserId);

        amounttext = (TextView) findViewById(R.id.lasttranamount);

        TransType = (TextView) findViewById(R.id.transactiontype);

        cardtype = (TextView) findViewById(R.id.cardtype);
        date = (TextView) findViewById(R.id.date);
        logoutbtn = (Button) findViewById(R.id.logout);
        logoutbtn.setVisibility(View.INVISIBLE);

        img = (ImageView) findViewById(R.id.img);
        img.setVisibility(View.INVISIBLE);
        accept = (Button) findViewById(R.id.accept);
        reject = (Button) findViewById(R.id.reject);
        accept.setVisibility(View.INVISIBLE);
        reject.setVisibility(View.INVISIBLE);

        CardView cardView = (CardView) findViewById(R.id.cv_one_login);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, LastTransaction.class);
                i.putExtra("token1",MyToken);
                i.putExtra("userid1",UserId);
                startActivity(i);
            }
        });

        salebtn = (Button) findViewById(R.id.btn9);
        voidbtn = (Button) findViewById(R.id.btn13);
//        historybtn = (Button) findViewById(R.id.btn16);
        preauthbtn = (Button) findViewById(R.id.btn11);
        preauthcbtn = (Button) findViewById(R.id.btn14);
        salewithcash = (Button) findViewById(R.id.btn10);
        BQRbtn = (Button) findViewById(R.id.btnBharatQr);



//        historybtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(HomeActivity.this,TransactionCount.class);
//                i.putExtra("token2",MyToken);
//                i.putExtra("userid2",UserId);
//                startActivity(i);
//            }
//        });

        salebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, Amount.class);
                i.putExtra("token2",MyToken);
                i.putExtra("userid2",UserId);
                startActivity(i);
            }
        });
        salewithcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SaleWithCashAmount.class);
                i.putExtra("token2",MyToken);
                i.putExtra("userid2",UserId);
                startActivity(i);
            }
        });

        voidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,InvoiceNumber.class);
                i.putExtra("token2",MyToken);
                i.putExtra("userid2",UserId);
                startActivity(i);
            }
        });

        preauthbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AmountForPreAuth.class);
                i.putExtra("token2",MyToken);
                i.putExtra("userid2",UserId);
                startActivity(i);
            }
        });

        preauthcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, InvoiceNumberPreAuthC.class);
                i.putExtra("token2",MyToken);
                i.putExtra("userid2",UserId);
                startActivity(i);
            }
        });

        BQRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AmountForBQR.class);
                i.putExtra("token2",MyToken);
                i.putExtra("userid2",UserId);
                i.putExtra("MerchantName",MerchantName);
                i.putExtra("fcmToken",FCMToken);
                startActivity(i);
            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void jsonParse() {

        //  Log.d("Token",Item);
        String url = "https://europa-sandbox.perseuspay.com/mportal/v1/lasttransactionstatus";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray LTS = response.getJSONArray("ltsdata");


                    Log.d("response is ", String.valueOf(response));

                    String s =  response.getJSONArray("ltsdata").getString(0);
                    String amount=new JSONObject(s).getString("amount");
                    // Float amtinfloat =  Float.valueOf(amount);


                    String trantype = new JSONObject(s).getString("transactiontype");
                    String Cardtype = new JSONObject(s).getString("cardtype");
                    // Toast.makeText(getApplicationContext(),Cardtype,Toast.LENGTH_LONG).show();
                    cardtype.setText("Card: "+Cardtype);

                    String myDate = new JSONObject(s).getString("txndatetime");




                    myDate = myDate.substring(0,10);
                    date.setText(myDate);

                    String Status = new JSONObject(s).getString("status");
                    // status.setText("status: "+Status);

                    if(Status.equals("00")){
                        accept.setVisibility(View.VISIBLE);
                    }
                    else
                        reject.setVisibility(View.VISIBLE);



                    if(trantype.equals("32")){
                        TransType.setText("Token");
                    }
                    else if(trantype.equals("00")){
                        TransType.setText("Sale");
                    }
                    else if(trantype.equals("09")){
                        TransType.setText("Cashatpos");
                    }
                    else if(trantype.equals("51")){
                        TransType.setText("Service Creation");
                    }
                    else if(trantype.equals("63")){
                        TransType.setText("BQR");
                    }

                    else
                        TransType.setText("-");




                    String merchantname =  new JSONObject(s).getString("merchantname");
                    merchantname = merchantname.substring(0,merchantname.length() - 10);
                    merchantname = merchantname.replaceAll("\\s+","");
                    String midtext = new JSONObject(s).getString("mid");
                    String amountnew = new JSONObject(s).getString("amount");
                    System.out.print("amount is"+amount);
                    float amount1 = parseFloat(amountnew)/100;

                    //  Toast.makeText(getContext(),""+amount1,Toast.LENGTH_SHORT).show();
                    amounttext.setText("₹"+amount1);
                    //  amount.setText("₹"+amount1);
                    // mid.setText("MID: "+midtext);
                    //  Toast.makeText(getContext(),""+merchantname,Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this,HomeActivity.class);
//        intent.putExtra("token",MyToken);
//        intent.putExtra("username",UserId);
//        startActivity(intent);
        Toast.makeText(getApplicationContext(),"You are at Home Screen",Toast.LENGTH_LONG).show();
    }

}