package com.example.merchantapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import helper.Config;
import messagebus.PostMsg2;
import utils.LogUtil;
import utils.Translate;
import utils.Utility;


public class SaleActivity extends AppCompatActivity {

    public static final String TAG = "NfcDemo";

    public static TextView mTextView,ResponseCode;
    public static ImageView img;
    public static TextView Tag1;
    public static TextView AID1;
    public static TextView CardNumber;
    public static TextView ExpiryDate;
    public static TextView Cardtype;
    private NfcAdapter mNfcAdapter;
    boolean found= false;
    public String str,token,username;
    private EditText Amount;

    Button btn;

    private NfcAdapter.ReaderCallback mReaderCallback = new NfcAdapter.ReaderCallback() {

        @Override
        public void onTagDiscovered(Tag tag) {

            runOnUiThread(new ReadPaycardThread(SaleActivity.this, tag,str));
//            Toast.makeText(getApplicationContext(),"Tag Detected",Toast.LENGTH_LONG).show();
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);


        String res1 = getIntent().getStringExtra("SaleAmount");
        Log.d("Intent amount is",res1);
        str = res1;

        token = getIntent().getStringExtra("token2");
        username = getIntent().getStringExtra("userid2");


        Log.d("Username in sale: ",username);
      //  Log.d("Sale Amount is",amount);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        AID1 = findViewById(R.id.AID);
        CardNumber = findViewById(R.id.cardnumber);
        ExpiryDate = findViewById(R.id.exp);
        Cardtype = findViewById(R.id.cardtype);
        Tag1= findViewById(R.id.mytext);
        img = findViewById(R.id.activity_read_paycard_image);
        btn = (Button) findViewById(R.id.pay);
        this.btn = btn;
        ResponseCode = findViewById(R.id.response);


        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            //finish();
            Intent in = new Intent(SaleActivity.this,HomeActivity.class);
            in.putExtra("token",token);
            in.putExtra("username",username);
            startActivity(in);
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            // mTextView.setText("NFC is disabled.");
            Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show();
        } else {
            // mTextView.setText("Enabled");
            mNfcAdapter.enableReaderMode(SaleActivity.this, mReaderCallback, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B, null);


//            String str4 = CardNumber.getText().toString();
//            str4 = str4.substring(0,4)+ " " +str4.substring(4,str4.length());
//            str4 = str4.substring(0,9)+ " " +str4.substring(9,str4.length());
//            str4 = str4.substring(0,14)+ " " +str4.substring(14,str4.length());
//            System.out.println("Cardnumber is: "+str4);

            // Log.d("Card number length",CardNumber.toString());

            // Toast.makeText(this,"NFC is enabled",Toast.LENGTH_LONG).show();

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String myAmount = Amount.getText().toString();
                    Format f = new SimpleDateFormat("MMdd");
                    String strDate = f.format(new Date());

                    f = new SimpleDateFormat("HHmmss");
                    String strTime = f.format(new Date());
                    Log.d("Hours min sec",strTime);

                    doTransaction(strDate,strTime,res1);
                }
            });


        }

    }
    private void doTransaction(String date,String hhmmss,String res1) {
        String url="https://europa-sandbox.perseuspay.com/pos/processtransaction";

        String track2 = Config.tlv2.getField(emv.Tag._57_TRACK2_EQUIVALENT_DATA);
//        if(track2 == null){
//            track2 = "CwH`\u00041\u0007BÑ\u0090\u0092\u0006����\u0005`����\u000F";
//        }
        System.out.println("track2 is"+track2);
        track2 = Translate.fromBinToHex(track2);

        track2 = track2.replaceAll("F","");

        String emv_data = Translate.fromBinToHex(Translate.getString(Config.tlv2.toMsg()));

        Config.tlv2.removeField(emv.Tag._57_TRACK2_EQUIVALENT_DATA);

        String stan = Utility.resize(Utility.getTime(), 6, "0", false);

        Log.d("Config Amount",Config.tran_amount);

        PostMsg2 msg = new PostMsg2();

        msg.setMsgType("0200");
        msg.setField(3,"000000");
        msg.setField(4, Config.tran_amount);
        msg.setField(11, stan);
        msg.setField(12,hhmmss);
        msg.setField(13,date);
        msg.setField(22,"071");
        msg.setField(35, track2);
        msg.setField(41,"10002685");
        msg.setField(42,"113103000029178");
        msg.setField(55,emv_data);

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

        String f037 = "0";
        String f038 = "0";

        String f039 = "0";
        String f041 = "0";
        String f042 = "0";
        String f011 = "0";
        String f002 = "0";
        String f040 = "0";

        String amt = Config.tran_amount;
        amt = amt.replaceFirst ("^0*", "");
        System.out.println("Config Amount is "+amt);

//        String amt = res1;
//        System.out.println(amt);

        if(msg2.isFieldSet(PostMsg2.Field._037_RETRIEVAL_REF_NR))
        {
            f037 = msg2.getField(PostMsg2.Field._037_RETRIEVAL_REF_NR);
        }
        if(msg2.isFieldSet(PostMsg2.Field._038_AUTH_ID_RSP)){
            f038 = msg2.getField(PostMsg2.Field._038_AUTH_ID_RSP);
        }
        if(msg2.isFieldSet(PostMsg2.Field._039_RSP_CODE)){
            f039 = msg2.getField(PostMsg2.Field._039_RSP_CODE);
        }
        if(msg2.isFieldSet(PostMsg2.Field._040_SERVICE_RESTRICTION_CODE)){
            f040 = msg2.getField(PostMsg2.Field._040_SERVICE_RESTRICTION_CODE);
        }
        if(msg2.isFieldSet(PostMsg2.Field._041_CARD_ACCEPTOR_TERM_ID)){
            f041 = msg2.getField(PostMsg2.Field._041_CARD_ACCEPTOR_TERM_ID);
        }
        if(msg2.isFieldSet(PostMsg2.Field._042_CARD_ACCEPTOR_ID_CODE)){
            f042 = msg2.getField(PostMsg2.Field._042_CARD_ACCEPTOR_ID_CODE);
        }
        if(msg2.isFieldSet(PostMsg2.Field._011_SYSTEMS_TRACE_AUDIT_NR)){
            f011 = msg2.getField(PostMsg2.Field._011_SYSTEMS_TRACE_AUDIT_NR);
        }
        if(msg2.isFieldSet(PostMsg2.Field._002_PAN)){
            f002 = msg2.getField(PostMsg2.Field._002_PAN);

        }


        System.out.println(f037+" f037 "+f038+" f038 "+f039+" f039"+f040+"f040");



        if(f039.equals("00")){
            Intent intent = new Intent(SaleActivity.this,ResponseSuccessActivity.class);
            intent.putExtra("Retrieval",f037);
            intent.putExtra("AuthIDRSP",f038);
            intent.putExtra("Response Code",f039);
            intent.putExtra("Date",date);
            intent.putExtra("Time",hhmmss);
            intent.putExtra("Terminal ID",f041);
            intent.putExtra("MID","113103000029178");
            intent.putExtra("Stan",f011);
            intent.putExtra("CardNumber",f002);
            intent.putExtra("Amt",amt);

            intent.putExtra("token",token);
            intent.putExtra("username",username);

            Log.d("Response is","Retrieval"+f037+"AuthId"+f038+"Responsecode"+f039+"Date"+date+"Time"+hhmmss+"terminalid"+f041+"mid"+f042+"stan"+f011+"cardnumber"+f002+"amt"+amt);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(SaleActivity.this,ResponseActivity.class);
            intent.putExtra("Retrieval",f037);
            intent.putExtra("AuthIDRSP",f038);
            intent.putExtra("Response Code",f039);
            intent.putExtra("Date",date);
            intent.putExtra("Time",hhmmss);
            intent.putExtra("Terminal ID",f041);
            intent.putExtra("MID",f042);
            intent.putExtra("Stan",f011);
            intent.putExtra("CardNumber",f002);
            intent.putExtra("Amt",amt);
            intent.putExtra("token",token);
            intent.putExtra("username",username);
            startActivity(intent);

        }


    }
}
