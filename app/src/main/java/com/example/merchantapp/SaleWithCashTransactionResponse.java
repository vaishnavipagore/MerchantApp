package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.DoubleUnaryOperator;

public class SaleWithCashTransactionResponse extends AppCompatActivity {

    private TextView Retrieval,AuthIDRSP,Date,Time,TerminalID,MerchantId,STAN,CardNo,Amount1,PC;
    TextView successText,declineText;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_with_cash_transaction_response);
        DecimalFormat df = new DecimalFormat("0.00");

        successText = (TextView) findViewById(R.id.txtStatus8);
        declineText = (TextView) findViewById(R.id.txtStatus9);
        successText.setVisibility(View.INVISIBLE);
        declineText.setVisibility(View.INVISIBLE);



        String cardnumber = getIntent().getStringExtra("CardNumber");
        String processingcode = getIntent().getStringExtra("ProcessingCode");
        String Amount = getIntent().getStringExtra("AmountTran");
        String stan = getIntent().getStringExtra("Stan");
        String time = getIntent().getStringExtra("Time");
        String date = getIntent().getStringExtra("Date");
        String retrieval = getIntent().getStringExtra("Retrieval");
        String AuthIdRsp = getIntent().getStringExtra("AuthIDRSP");
        String res = getIntent().getStringExtra("Response Code");
        String terminalid = getIntent().getStringExtra("Terminal ID");
        String MID  = getIntent().getStringExtra("MID");
        String trannr = getIntent().getStringExtra("TranNr");
        String trandesc = getIntent().getStringExtra("TranDesc");
        String token = getIntent().getStringExtra("token");
        String username = getIntent().getStringExtra("username");

        if(res.equals("00")){
            successText.setVisibility(View.VISIBLE);
        }
        else
            declineText.setVisibility(View.VISIBLE);


        Retrieval = (TextView) findViewById(R.id.retrievalsalewithcashresponse);
        Retrieval.setText("RRN: "+retrieval);

        PC = (TextView) findViewById(R.id.processingcodesalewithcashresponse);
        PC.setText("Process Code: "+processingcode);


        AuthIDRSP = (TextView) findViewById(R.id.authIdRspsalewithcashresponse);
        AuthIDRSP.setText("Auth ID RSP: " +AuthIdRsp);


        Date = (TextView) findViewById(R.id.datesalewithcashresponse);
        Date.setText("Date: " +date);


        Time = (TextView) findViewById(R.id.timesalewithcashresponse);
        Time.setText("Time: "+time);


        TerminalID = (TextView) findViewById(R.id.terminalidsalewithcashresponse);
        TerminalID.setText("Terminal ID: "+terminalid);

        MerchantId = (TextView)findViewById(R.id.midsalewithcashresponse);
        MerchantId.setText("MID: "+MID);

        STAN = (TextView)findViewById(R.id.stansalewithcashresponse);
        STAN.setText("STAN: "+stan);

        CardNo = (TextView)findViewById(R.id.CardNumbersalewithcashresponse);
        CardNo.setText("Card Number: "+cardnumber);



        Amount1 = (TextView)findViewById(R.id.TransactionAmountsalewithcashresponse);
        Double d;
        d = Double.parseDouble(Amount)/100;
        Amount = d.toString();


      //  double amount = 15;
        NumberFormat formatter = new DecimalFormat("#0.00");
       // System.out.println("The Decimal Value is:"+formatter.format(d));
        Amount = formatter.format(d);
        Amount1.setText("₹ "+Amount);


        done = (Button) findViewById(R.id.donesalewithcashresponse);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SaleWithCashTransactionResponse.this,HomeActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });




        // System.out.print(mid+"Mid in response");



    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Press Done Button To Go Home",Toast.LENGTH_LONG).show();
    }
}
