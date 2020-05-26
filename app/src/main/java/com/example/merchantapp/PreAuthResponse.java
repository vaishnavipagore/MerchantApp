package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PreAuthResponse extends AppCompatActivity {

    String mytoken,username,localtime1,localtime2,tid1,mid1,trandesc,pan,amounttran,rrn,authidrsp,trannr,responsecode;
    TextView MID,TID,TIME1,TIME2,TRANSACTIONDESCRIPTION,PAN,AMOUNTTRAN,RRN,AUTHIDR,TRANNR;
    Button done;
    TextView successText,declineText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_auth_response);

        successText = (TextView) findViewById(R.id.txtStatus);
        declineText = (TextView) findViewById(R.id.txtStatus2);

        successText.setVisibility(View.INVISIBLE);
        declineText.setVisibility(View.INVISIBLE);






        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        String retrieval = getIntent().getStringExtra("Retrieval");
        String AuthIdRsp = getIntent().getStringExtra("AuthIDRSP");
        String res = getIntent().getStringExtra("Response Code");
       // Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
        String date = getIntent().getStringExtra("Date");
        String time = getIntent().getStringExtra("Time");
        String terminalid = getIntent().getStringExtra("Terminal ID");
        String mid = getIntent().getStringExtra("MID");
        String stan = getIntent().getStringExtra("Stan");
        String cardnumber = getIntent().getStringExtra("CardNumber");
        String amt = getIntent().getStringExtra("Amt");

        responsecode = intent.getStringExtra("ResponseCode");
        String trandesc = intent.getStringExtra("TranDesc");
        String trannr = intent.getStringExtra("TranNr");
        //Increment  Toast.makeText(getApplicationContext(),responsecode.toString(),Toast.LENGTH_LONG).show();



        if(responsecode.equals("00")){
            successText.setVisibility(View.VISIBLE);
        }
        else if(responsecode.equals("94")){
            declineText.setText("Duplicate Transaction");
            declineText.setVisibility(View.VISIBLE);
        }
        else
            declineText.setVisibility(View.VISIBLE);

        tid1 = intent.getStringExtra("Terminal ID");
        mid1 = intent.getStringExtra("MID");
        Log.d("","MID is"+mid1+"TID is"+tid1+"Local1"+localtime1+"Local2"+localtime2);

        MID = (TextView) findViewById(R.id.midpreauth);
        TID = (TextView) findViewById(R.id.terminalidpreauth);

        TRANSACTIONDESCRIPTION = (TextView) findViewById(R.id.transactiondescriptionpreauth);
        TRANSACTIONDESCRIPTION.setText("Tran Desc: "+trandesc);

        PAN = (TextView) findViewById(R.id.panpreauth);
        PAN.setText("Card Number: "+cardnumber);

        AMOUNTTRAN = (TextView) findViewById(R.id.amounttranspreauth);
        if(amt.equals(0)){
            amt = "0";
        }
        Float f1 = Float.parseFloat(amt)/100;
        AMOUNTTRAN.setText("₹ "+f1);

        RRN = (TextView) findViewById(R.id.rrnpreauth);
        RRN.setText("RRN: "+retrieval);

        AUTHIDR = (TextView) findViewById(R.id.authidresppreauth);
        AUTHIDR.setText("AUTH RSP: "+AuthIdRsp);

        TRANNR = (TextView) findViewById(R.id.trannrpreauth);
        TRANNR.setText("Tran Nr: "+trannr);



        MID.setText("MID: "+mid);
        TID.setText("TID: "+terminalid);
//        TIME1.setText("LocalTime: "+localtime1);
//        TIME2.setText("LocalTime2:"+localtime2);
        Log.d("token in void",mytoken);
        Log.d("username in void",username);

        done = (Button) findViewById(R.id.donepreauth);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(PreAuthResponse.this,HomeActivity.class);
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
