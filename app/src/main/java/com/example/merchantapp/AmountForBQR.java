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
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import emv.Tag;
import helper.Config;
import messagebus.PostMsg2;
import utils.LogUtil;
import utils.Utility;

public class AmountForBQR extends AppCompatActivity {

    String token,username,MerchantName;
    EditText BqrAmount;
    Button BqrButton;
    Float f;
    public static TextView resultTextView;
    String FCMToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_for_bqr);

        token = getIntent().getStringExtra("token2");
        username = getIntent().getStringExtra("userid2");
        MerchantName = getIntent().getStringExtra("MerchantName");
//        Log.d("MerchantName",MerchantName);

      //  FCMToken = getIntent().getStringExtra("fcmToken");
        FCMToken = Config.FCMToken;
//        Log.d("FCM in BQR",Config.FCMToken);
//        Log.d("FCM Token",FCMToken);

//        Log.d("token in bqr",token);
//        Log.d("username in bqr",username);

        resultTextView = (TextView) findViewById(R.id.result);

        BqrAmount = (EditText) findViewById(R.id.amountbqr);
        BqrButton = (Button) findViewById(R.id.paybqr);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        BqrAmount.setFocusableInTouchMode(true);
        BqrAmount.requestFocus();

        if(BqrAmount.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }



        BqrAmount.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString();
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                    BqrAmount.setText(text.substring(0, text.length() - 1));
                    BqrAmount.setSelection(BqrAmount.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
            }
        });

        BqrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(BqrAmount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Amount",Toast.LENGTH_LONG).show();
                }else
                {
                    f = Float.valueOf(BqrAmount.getText().toString());
                    f = f * 10;
                    String amount = f + "";
                    amount = amount.replace(".","");
                  //  Toast.makeText(getApplicationContext(),"Amount is "+amount,Toast.LENGTH_LONG).show();

//                    Intent i  = new Intent(AmountForBQR.this,ScanCodeActivity.class);
//                    i.putExtra("Amount",amount);
//                    i.putExtra("MerchantName",MerchantName);
//                    startActivity(i);
                    doBQRTransaction(amount);
                }
                // Toast.makeText(getApplicationContext(),"Swipe or Insert Card",Toast.LENGTH_LONG).show();
            }

            private void doBQRTransaction(String amount) {
                Log.d("AMount in BQR",amount);

                String url="https://europa-sandbox.perseuspay.com/pos/processtransaction";


                String stan = Utility.resize(Utility.getTime(), 6, "0", false);

                PostMsg2 msg = new PostMsg2();

                amount = Utility.resize(amount, 12, "0", false);
                System.out.println("Config Amount is "+amount);

                Log.d("AMount",amount);


                msg.setMsgType("0200");

                msg.setField(3,"630000");
                msg.setField(4, amount); //Amount
                msg.setField(11,stan);
                msg.setField(22,"010");
                msg.setField(41,"10002685"); //TID
                msg.setField(42,"113103000029178"); //MID
                msg.setField(46,FCMToken);
                msg.setField(57,"000002");
              //  msg.setField(62,"000017");
               // msg.setField(62,invoiceno); //Invoice Number or Transaction ID

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
                String f048 = "0";
                String f039 = "0";
                String f059 = "0";


//
//
                if(msg2.isFieldSet(PostMsg2.Field._048_ADDITIONAL_DATA))
                {
                    f048 = msg2.getField(PostMsg2.Field._048_ADDITIONAL_DATA);
                }
                if(msg2.isFieldSet(PostMsg2.Field._039_RSP_CODE)){
                    f039 = msg2.getField(PostMsg2.Field._039_RSP_CODE);
                }
                if(msg2.isFieldSet((PostMsg2.Field._059_TRAN_NR))){
                    f059 = msg2.getField(PostMsg2.Field._059_TRAN_NR);
                }
//

                if(f039.equals("00")){
                    Intent i  = new Intent(AmountForBQR.this,ScanCodeActivity.class);
                    i.putExtra("F048",f048);
                    i.putExtra("BQRAmount",BqrAmount.getText().toString());
                    i.putExtra("MerchantName",MerchantName);
                    i.putExtra("TranNr",f059);
                    i.putExtra("token",token);
                    i.putExtra("username",username);
                    i.putExtra("fcmToken",FCMToken);

                   startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Requested Transaction Failed",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    }

