package com.example.merchantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResponseActivity extends AppCompatActivity {
    private TextView Retrieval,AuthIDRSP,Date,Time,TerminalID,MerchantId,STAN,CardNo,Amount;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_response);
        Intent intent = getIntent();
        String mytoken = intent.getStringExtra("token");
        String username = intent.getStringExtra("username");
        Log.d("token in res",mytoken);
        Log.d("username in res",username);


        String retrieval = getIntent().getStringExtra("Retrieval");
        String AuthIdRsp = getIntent().getStringExtra("AuthIDRSP");
        String res = getIntent().getStringExtra("Response Code");
        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
        String date = getIntent().getStringExtra("Date");
        String time = getIntent().getStringExtra("Time");
        String terminalid = getIntent().getStringExtra("Terminal ID");
        String mid = getIntent().getStringExtra("MID");
        String stan = getIntent().getStringExtra("Stan");
        String cardnumber = getIntent().getStringExtra("CardNumber");
        String amt = getIntent().getStringExtra("Amt");
//        StringBuilder sb = new StringBuilder(amt);
//        sb.insert(2,".");
//        Log.d("sb ",sb.toString());
//        Float f = Float.parseFloat(amt);
//        f= f / 100;

        Retrieval = (TextView) findViewById(R.id.retrieval);
        Retrieval.setText("Retrieval: "+retrieval);


        AuthIDRSP = (TextView) findViewById(R.id.authIdRsp);
        AuthIDRSP.setText("Auth ID RSP: " +AuthIdRsp);


        Date = (TextView) findViewById(R.id.date);
        Date.setText("Date: " +date);


        Time = (TextView) findViewById(R.id.time);
        Time.setText("Time: "+time);


        TerminalID = (TextView) findViewById(R.id.terminalid);
        TerminalID.setText("Terminal: "+terminalid);

        MerchantId = (TextView)findViewById(R.id.mid);
        MerchantId.setText("MID: "+mid);

        STAN = (TextView)findViewById(R.id.stan);
        STAN.setText("STAN: "+stan);

        CardNo = (TextView)findViewById(R.id.CardNumber);
        CardNo.setText("Card Number: "+cardnumber);


        Amount = (TextView)findViewById(R.id.TransactionAmount);
        Amount.setText("₹ "+amt);




        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ResponseActivity.this,HomeActivity.class);
                intent.putExtra("token",mytoken);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

//       // Log.d("Response activity: ",res.toString());
//        try {
//            JSONObject obj = new JSONObject(getIntent().getStringExtra("Response"));
//            String aqbank = obj.getString("acqbankname");
//            String TransactionAmount = obj.getString("amount_tran");
//            float sucessamount = Float.parseFloat(TransactionAmount);
//
//            String BinOwner = obj.getString("binowner");
//            String Date = obj.getString("date");
//            int successdate = Integer.parseInt(Date);
//
//            StringBuilder str = new StringBuilder(Date);
//            str.insert(2,'/');
//
//
//
//            String pan = obj.getString("pan");
//            String rrn = obj.getString("rrn");
//            String rsp_code = obj.getString("rsp_code");
//            String stan = obj.getString("stan");
//            String tid = obj.getString("tid");
//            String time = obj.getString("time");
//            StringBuilder str2 = new StringBuilder(time);
//            str2.insert(2,':');
//            str2.insert(5,':');
//            String transactionid = obj.getString("transactionid");
//
//
//            AcBank = findViewById(R.id.aqbank);
//            TranAmount = findViewById(R.id.TransactionAmount);
//            BinOwneR = findViewById(R.id.binowner);
//            DATE= findViewById(R.id.date);
//            PAN = findViewById(R.id.pan);
//            RRN = findViewById(R.id.rrn);
//            Stan = findViewById(R.id.stan);
//            TID= findViewById(R.id.tid);
//            Time= findViewById(R.id.time);
//            TransactionID = findViewById(R.id.transaction);
//            TranAmount = findViewById(R.id.TransactionAmount);
//
//            TranAmount.setText("Rs "+sucessamount/100);
//
//
//            AcBank.setText("Acquiring Bank: "+aqbank);
//            BinOwneR.setText("BinOwner: "+BinOwner);
//            DATE.setText("Date: "+str+" [MM/DD]");
//            PAN.setText("Pan: "+pan);
//            RRN.setText("RRN: "+rrn);
//            Stan.setText("Stan: "+stan);
//            TID.setText("Tid: "+tid);
//            Time.setText("Time: "+str2+" [HH:MM:SS]");
//            TransactionID.setText("Transaction ID: "+transactionid);
//
//            done = (Button) findViewById(R.id.done);
//            done.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent  i = new Intent(ResponseActivity.this,MainActivity.class);
//                    startActivity(i);
//                }
//            });
         //   TranAmount = findViewById(R.id.)


//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Press Done Button To Go Home",Toast.LENGTH_LONG).show();
    }
}
