package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PreAuthCompletionResponse extends AppCompatActivity {

    String mytoken,username,responsecode,tid1,mid1,trandesc,pan,amounttran,rrn,authidrsp;
    TextView MID,TID,TIME1,TIME2,TRANSACTIONDESCRIPTION,PAN,AMOUNTTRAN,RRN,AUTHIDR,TRANNR;
    Button done;
    TextView successText,declineText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_auth_completion_response);


        successText = (TextView) findViewById(R.id.txtStatus6);
        declineText = (TextView) findViewById(R.id.txtStatus7);
        successText.setVisibility(View.INVISIBLE);
        declineText.setVisibility(View.INVISIBLE);


        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        String retrieval = getIntent().getStringExtra("RRN");
        String AuthIdRsp = getIntent().getStringExtra("AuthIDRsp");
        String res = getIntent().getStringExtra("ResponseCode");
        // Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
        String date = getIntent().getStringExtra("LocalTranTime2");
        String time = getIntent().getStringExtra("LocalTranTime");
        String terminalid = getIntent().getStringExtra("Terminal ID");
        String mid = getIntent().getStringExtra("MID");
        String stan = getIntent().getStringExtra("Stan");
        String cardnumber = getIntent().getStringExtra("PAN");
        String amt = getIntent().getStringExtra("AmountTran");

        responsecode = intent.getStringExtra("ResponseCode");

        String trandesc = intent.getStringExtra("TranDesc");
        String trannr = intent.getStringExtra("TranNR");


        if(responsecode.equals("00")){
            successText.setVisibility(View.VISIBLE);
        }
        else
            declineText.setVisibility(View.VISIBLE);


        MID = (TextView) findViewById(R.id.midpreauthc);
        TID = (TextView) findViewById(R.id.terminalidpreauthc);

        tid1 = intent.getStringExtra("Terminal ID");
        mid1 = intent.getStringExtra("MID");
       // Log.d("","MID is"+mid1+"TID is"+tid1+"Local1"+localtime1+"Local2"+localtime2);



        TRANSACTIONDESCRIPTION = (TextView) findViewById(R.id.transactiondescriptionpreauthc);
        try{
            TRANSACTIONDESCRIPTION.setText("Tran Desc: "+trandesc);
        }
        catch(Exception e){
            TRANSACTIONDESCRIPTION.setText("0");
        }


        PAN = (TextView) findViewById(R.id.panpreauthc);
        PAN.setText("Card Number: "+cardnumber);

        AMOUNTTRAN = (TextView) findViewById(R.id.amounttranspreauthc);
        AMOUNTTRAN.setText("₹ "+amt);

        RRN = (TextView) findViewById(R.id.rrnpreauthc);
        RRN.setText("RRN: "+retrieval);

        AUTHIDR = (TextView) findViewById(R.id.authidresppreauthc);
        AUTHIDR.setText("AUTH RSP: "+AuthIdRsp);

        TRANNR = (TextView) findViewById(R.id.trannrpreauthc);
        TRANNR.setText("Tran Nr: "+trannr);



        MID.setText("MID: "+mid);
        TID.setText("TID: "+terminalid);
//        TIME1.setText("LocalTime: "+localtime1);
//        TIME2.setText("LocalTime2:"+localtime2);
        Log.d("token in void",mytoken);
        Log.d("username in void",username);

        done = (Button) findViewById(R.id.donepreauthc);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(PreAuthCompletionResponse.this,HomeActivity.class);
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
