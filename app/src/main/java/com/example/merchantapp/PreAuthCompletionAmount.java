package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import helper.Config;
import messagebus.PostMsg2;
import utils.LogUtil;
import utils.Utility;

public class PreAuthCompletionAmount extends AppCompatActivity {

    String mytoken;
    String username;
    String invoicenumber;
    Button pay;
    EditText amountforpreathc;
    String strDate,strTime;
    Float f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_auth_completion_amount);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Log.d("PreAuthCompletion", "PAC");
        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token2");
        username = intent.getStringExtra("userid2");
        invoicenumber = intent.getStringExtra("InvoiceNumberPreAuthCompletion");
//        Log.d("token PAC", mytoken);
//        Log.d("username PAC", username);
//        Log.d("" + "Invoice " + "PAC", invoicenumber);


        Format f = new SimpleDateFormat("MMdd");
        strDate = f.format(new Date());

        f = new SimpleDateFormat("HHmmss");
        strTime = f.format(new Date());
       // Log.d("Hours min sec",strTime);



        amountforpreathc = (EditText) findViewById(R.id.amountpreauthc);

        amountforpreathc.setFocusableInTouchMode(true);
        amountforpreathc.requestFocus();

        if (amountforpreathc.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }


        amountforpreathc.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString();
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                    amountforpreathc.setText(text.substring(0, text.length() - 1));
                    amountforpreathc.setSelection(amountforpreathc.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
            }
        });


        pay = (Button) findViewById(R.id.paypreauthc);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),amountforpreathc.getText().toString()+"",Toast.LENGTH_LONG).show();
                f1 = Float.valueOf(amountforpreathc.getText().toString());
                f1 = f1 * 10;
                String amount = f1 + "";
                amount = amount.replace(".","");
                doPreAuthCompletionTransaction(amountforpreathc.getText().toString(),invoicenumber);
            }

            //Pre Auth Completion Transaction
            private void doPreAuthCompletionTransaction(String amount,String invoiceno) {
                String url="https://europa-sandbox.perseuspay.com/pos/processtransaction";


                System.out.println("Invoice no"+invoiceno);

                String stan = Utility.resize(Utility.getTime(), 6, "0", false);


                PostMsg2 msg = new PostMsg2();

                msg.setMsgType("0220");
                msg.setField(11,stan);
                msg.setField(13,strDate);
                msg.setField(12,strTime);
                msg.setField(41,"10002685"); //TID
                msg.setField(42,"113103000029178"); //MID
                msg.setField(62,invoiceno); //Invoice Number or Transaction ID
                msg.setField(4, Config.tran_amount); //Amount


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
//
//
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
                String f011 = "0";
                String f062 = "0";


//
//
                if(msg2.isFieldSet(PostMsg2.Field._012_TIME_LOCAL))
                {
                    f012 = msg2.getField(PostMsg2.Field._012_TIME_LOCAL);
                }
                if(msg2.isFieldSet(PostMsg2.Field._013_DATE_LOCAL)){
                    f013 = msg2.getField(PostMsg2.Field._013_DATE_LOCAL);
                }
//
                if(msg2.isFieldSet(PostMsg2.Field._041_CARD_ACCEPTOR_TERM_ID)){
                    f041 = msg2.getField(PostMsg2.Field._041_CARD_ACCEPTOR_TERM_ID);
                }
//
                if(msg2.isFieldSet(PostMsg2.Field._104_TRAN_DESCRIPTION)){
                    f104 = msg2.getField(PostMsg2.Field._104_TRAN_DESCRIPTION);

                }
//
                if(msg2.isFieldSet(PostMsg2.Field._042_CARD_ACCEPTOR_ID_CODE)){
                    f042 = msg2.getField(PostMsg2.Field._042_CARD_ACCEPTOR_ID_CODE);

                }
                if(msg2.isFieldSet(PostMsg2.Field._002_PAN)){
                    f002 = msg2.getField(PostMsg2.Field._002_PAN);

                }
//
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
                if(msg2.isFieldSet(PostMsg2.Field._011_SYSTEMS_TRACE_AUDIT_NR)){
                    f011 = msg2.getField(PostMsg2.Field._011_SYSTEMS_TRACE_AUDIT_NR);

                }
                if(msg2.isFieldSet(PostMsg2.Field._062_TRANS_ID)){
                    f062 = msg2.getField(PostMsg2.Field._062_TRANS_ID);
                }

                System.out.println("CardNumber is "+f002+" RRN is "+f037 + " Auth ID Rsp "+f038);

                Intent intent = new Intent(PreAuthCompletionAmount.this,PreAuthCompletionResponse.class);
                intent.putExtra("LocalTranTime",f012);
                intent.putExtra("LocalTranTime2",f013);
                intent.putExtra("Terminal ID",f041);
                intent.putExtra("MID","113103000029178");
                intent.putExtra("Stan",f011);
                intent.putExtra("TranDesc",f041);
                intent.putExtra("PAN",f002);
                intent.putExtra("AmountTran",amount);
                intent.putExtra("RRN",f037);
                intent.putExtra("AuthIDRsp",f038);
                intent.putExtra("TranNR",f059);
                intent.putExtra("ResponseCode",f039);
                intent.putExtra("token",mytoken);
                intent.putExtra("username",username);

//            Log.d("Response is","Retrieval"+f037+"AuthId"+f038+"Responsecode"+f039+"Date"+date+"Time"+hhmmss+"terminalid"+f041+"mid"+f042+"stan"+f011+"cardnumber"+f002+"amt"+amt);
                startActivity(intent);

            }
        });
    }
}
