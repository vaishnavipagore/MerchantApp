package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VoidDeclineResponse extends AppCompatActivity {
    String mytoken,username,localtime1,localtime2,tid1,mid1,trandesc,pan,amounttran,rrn,authidrsp,trannr;
    TextView MID,TID,TIME1,TIME2,TRANSACTIONDESCRIPTION,PAN,AMOUNTTRAN,RRN,AUTHIDR,TRANNR;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void_response);



        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        localtime1 = intent.getStringExtra("LocalTranTime");
        localtime2 = intent.getStringExtra("LocalTranTime2");
        trandesc = intent.getStringExtra("TranDesc");
        pan = intent.getStringExtra("PAN");
        amounttran = intent.getStringExtra("AmountTran");
        rrn = intent.getStringExtra("RRN");
        authidrsp = intent.getStringExtra("AuthIDRsp");
        trannr = intent.getStringExtra("TranNR");


        tid1 = intent.getStringExtra("Terminal ID");
        mid1 = intent.getStringExtra("MID");
        Log.d("","MID is"+mid1+"TID is"+tid1+"Local1"+localtime1+"Local2"+localtime2);

        MID = (TextView) findViewById(R.id.mid);
        TID = (TextView) findViewById(R.id.terminalid);

        TRANSACTIONDESCRIPTION = (TextView) findViewById(R.id.transactiondescription);
        TRANSACTIONDESCRIPTION.setText("Tran Desc: "+trandesc);

        PAN = (TextView) findViewById(R.id.pan);
        PAN.setText("Card Number: "+pan);

        AMOUNTTRAN = (TextView) findViewById(R.id.amounttrans);
        AMOUNTTRAN.setText("Amount: "+amounttran);

        RRN = (TextView) findViewById(R.id.rrn);
        RRN.setText("RRN: "+rrn);

        AUTHIDR = (TextView) findViewById(R.id.authidresp);
        AUTHIDR.setText("AUTH RSP: "+authidrsp);

        TRANNR = (TextView) findViewById(R.id.trannr);
        TRANNR.setText("Tran Nr: "+trannr);



        MID.setText("MID: "+mid1);
        TID.setText("TID: "+tid1);
//        TIME1.setText("LocalTime: "+localtime1);
//        TIME2.setText("LocalTime2:"+localtime2);
        Log.d("token in void",mytoken);
        Log.d("username in void",username);

        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(VoidDeclineResponse.this,HomeActivity.class);
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
