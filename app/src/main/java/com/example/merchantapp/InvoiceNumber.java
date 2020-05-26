package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import helper.Config;
import messagebus.PostMsg2;
import utils.LogUtil;
import utils.Translate;
import utils.Utility;

public class InvoiceNumber extends AppCompatActivity {

    String mytoken;
    String username;
    EditText invoicenumber;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_number);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token2");
        username = intent.getStringExtra("userid2");
//        Log.d("token is",mytoken);
//        Log.d("username is",username);

        invoicenumber = (EditText) findViewById(R.id.invoicenumber);


        invoicenumber.setFocusableInTouchMode(true);
        invoicenumber.requestFocus();

        if(invoicenumber.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        pay = (Button) findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invoiceno = invoicenumber.getText().toString();
                doVoidTransaction(invoiceno);
            }
        });


    }

    private void doVoidTransaction(String invoiceno) {
        String url="https://europa-sandbox.perseuspay.com/pos/processtransaction";


        System.out.println("Invoice no"+invoiceno);

        PostMsg2 msg = new PostMsg2();

        msg.setMsgType("0420");
        msg.setField(41,"10002685");
        msg.setField(42,"113103000029178");
        msg.setField(62,invoiceno);

//        msg.setField(55,emv_data);

        String payload = msg.getMsg();
        int len = payload.length();

        payload = Utility.resize(Integer.toString(len),4,"0", false)+payload;

        HashMap<String, String> hdrs = new HashMap<>();
        hdrs.put("Content-Type","text/plain");
        hdrs.put("apikey","B0D69595E51048E29AF85E7B9A75EBA8");

        LogUtil.d("Req", payload);

        String rsp = Utility.sendAndRxHTTPMsg(url, 40000, payload, hdrs);
        LogUtil.d("Rsp", rsp);

        PostMsg2 msg2 = new PostMsg2();
        msg2.parseMsgFromRemote(rsp);


        String f012 = "0";
        String f013 = "0";
        String f039 = "0";
        String f041 = "0";
        String f059 = "0";
        String f104 = "0";
        String f042 = "0";
        String f002 = "0";
        String f004 = "0";
        String f037 = "0";
        String f038 = "0";



        if(msg2.isFieldSet(PostMsg2.Field._012_TIME_LOCAL))
        {
            f012 = msg2.getField(PostMsg2.Field._012_TIME_LOCAL);
        }
        if(msg2.isFieldSet(PostMsg2.Field._013_DATE_LOCAL)){
            f013 = msg2.getField(PostMsg2.Field._013_DATE_LOCAL);
        }

        if(msg2.isFieldSet(PostMsg2.Field._041_CARD_ACCEPTOR_TERM_ID)){
            f041 = msg2.getField(PostMsg2.Field._041_CARD_ACCEPTOR_TERM_ID);
        }

        if(msg2.isFieldSet(PostMsg2.Field._104_TRAN_DESCRIPTION)){
            f104 = msg2.getField(PostMsg2.Field._104_TRAN_DESCRIPTION);

        }

        if(msg2.isFieldSet(PostMsg2.Field._042_CARD_ACCEPTOR_ID_CODE)){
            f042 = msg2.getField(PostMsg2.Field._042_CARD_ACCEPTOR_ID_CODE);

        }
        if(msg2.isFieldSet(PostMsg2.Field._002_PAN)){
            f002 = msg2.getField(PostMsg2.Field._002_PAN);

        }

        if(msg2.isFieldSet(PostMsg2.Field._004_AMOUNT_TRANSACTION)){
            f004 = msg2.getField(PostMsg2.Field._004_AMOUNT_TRANSACTION);

        }
        if(msg2.isFieldSet(PostMsg2.Field._037_RETRIEVAL_REF_NR)){
            f037 = msg2.getField(PostMsg2.Field._037_RETRIEVAL_REF_NR);

        }
        if(msg2.isFieldSet(PostMsg2.Field._038_AUTH_ID_RSP)){
            f038 = msg2.getField(PostMsg2.Field._038_AUTH_ID_RSP);

        }
        if(msg2.isFieldSet(PostMsg2.Field._039_RSP_CODE)){
            f039 = msg2.getField(PostMsg2.Field._039_RSP_CODE);

        }
        if(msg2.isFieldSet(PostMsg2.Field._059_TRAN_NR)){
            f059 = msg2.getField(PostMsg2.Field._059_TRAN_NR);

        }




        System.out.println(f039+" f039");



            Intent intent = new Intent(InvoiceNumber.this,VoidResponse.class);
            intent.putExtra("LocalTranTime",f012);
            intent.putExtra("LocalTranTime2",f013);
            intent.putExtra("Terminal ID",f041);
            intent.putExtra("MID","113103000029178");

            intent.putExtra("TranDesc",f041);
            intent.putExtra("PAN",f002);
            intent.putExtra("AmountTran",f004);
            intent.putExtra("RRN",f037);
            intent.putExtra("AuthIDRsp",f038);
            intent.putExtra("TranNR",f059);
            intent.putExtra("ResponseCode",f039);
            intent.putExtra("token",mytoken);
            intent.putExtra("username",username);

//            Log.d("Response is","Retrieval"+f037+"AuthId"+f038+"Responsecode"+f039+"Date"+date+"Time"+hhmmss+"terminalid"+f041+"mid"+f042+"stan"+f011+"cardnumber"+f002+"amt"+amt);
            startActivity(intent);



    }
}
