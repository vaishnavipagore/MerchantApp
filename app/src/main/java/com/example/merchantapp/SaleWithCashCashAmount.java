package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;

public class SaleWithCashCashAmount extends AppCompatActivity {

    Button paybtn;
    EditText amount;
    Float f;
    FileOutputStream fOut;
    String mytoken,username,saleamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_with_cash_cash_amount);
        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token2");
        username = intent.getStringExtra("userid2");
        saleamount = intent.getStringExtra("amount");

        Log.d("token in SWCCO",mytoken);
        Log.d("username in SWCCO",username);
        Log.d("Amount is",saleamount);

        paybtn = (Button) findViewById(R.id.paysalewithcash2);
        amount = (EditText) findViewById(R.id.amountsalewithcash2);

        amount.setFocusableInTouchMode(true);
        amount.requestFocus();

        if(amount.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }



        amount.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString();
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                    amount.setText(text.substring(0, text.length() - 1));
                    amount.setSelection(amount.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
            }
        });

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForSaleWithCashTrasaction();


            }

            private void goForSaleWithCashTrasaction() {
                f = Float.valueOf(amount.getText().toString());
                f = f * 10;
                String amount1 = f + "";
                amount1 = amount1.replace(".","");
                Toast.makeText(getApplicationContext(),"Sale Amount is"+saleamount+"Cash Amount"+amount1,Toast.LENGTH_LONG).show();
                Intent i = new Intent(SaleWithCashCashAmount.this,SaleWithCashTransaction.class);
                i.putExtra("SaleAmount",saleamount);
                i.putExtra("CashAmount",amount1);
                i.putExtra("token2",mytoken);
                i.putExtra("userid2",username);
                startActivity(i);
            }
        });



    }
}
