package com.example.merchantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResponseSuccessActivity extends AppCompatActivity {
    private TextView Retrieval,AuthIDRSP,Date,Time,TerminalID,MerchantId,STAN,CardNo,Amount;
    String MID;
    Button done;
    Float f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_success);

        Intent intent = getIntent();
        String mytoken = intent.getStringExtra("token");
        String username = intent.getStringExtra("username");
        Log.d("token in res",mytoken);
        Log.d("username in res",username);

        String retrieval = getIntent().getStringExtra("Retrieval");
        String AuthIdRsp = getIntent().getStringExtra("AuthIDRSP");
        String res = getIntent().getStringExtra("Response Code");
        String date = getIntent().getStringExtra("Date");
        String time = getIntent().getStringExtra("Time");
        String terminalid = getIntent().getStringExtra("Terminal ID");
        try{
            MID  = getIntent().getStringExtra("MID");
        }
        catch(Exception e){
            e.printStackTrace();
            MID = "0";
        }

       // System.out.print(mid+"Mid in response");
        String stan = getIntent().getStringExtra("Stan");
        String cardnumber = getIntent().getStringExtra("CardNumber");
        String amt = getIntent().getStringExtra("Amt");

       // String intentAmount = getIntent().getStringExtra("")
        if(amt.equals(null)){
            amt = "";
        }
        else
        {

//            f = Float.parseFloat(amt);
//            f = f  / 100;

        }

//        StringBuilder sb = new StringBuilder(amt);
//        sb.insert(2,".");
//        Log.d("sb ",sb.toString());




        Retrieval = (TextView) findViewById(R.id.retrieval);
        Retrieval.setText("Retrieval: "+retrieval);


        AuthIDRSP = (TextView) findViewById(R.id.authIdRsp);
        AuthIDRSP.setText("Auth ID RSP: " +AuthIdRsp);


        Date = (TextView) findViewById(R.id.date);
        Date.setText("Date: " +date);


        Time = (TextView) findViewById(R.id.time);
        Time.setText("Time: "+time);


        TerminalID = (TextView) findViewById(R.id.terminalid);
        TerminalID.setText("Terminal ID: "+terminalid);

        MerchantId = (TextView)findViewById(R.id.mid);
        MerchantId.setText("MID: "+MID);

        STAN = (TextView)findViewById(R.id.stan);
        STAN.setText("STAN: "+stan);

        CardNo = (TextView)findViewById(R.id.CardNumber);
        CardNo.setText("Card Number: "+cardnumber);

        Amount = (TextView)findViewById(R.id.TransactionAmount);
        Float f1 = Float.parseFloat(amt)/100;
        Amount.setText("₹ "+f1);


        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ResponseSuccessActivity.this,HomeActivity.class);
                intent.putExtra("token",mytoken);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });





    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Press Done Button To Go Home",Toast.LENGTH_LONG).show();
    }
}
