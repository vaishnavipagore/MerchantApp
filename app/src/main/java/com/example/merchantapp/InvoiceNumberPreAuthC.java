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

public class InvoiceNumberPreAuthC extends AppCompatActivity {
    String mytoken;
    String username;
    EditText invoicenumber;
    Button pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_number_pre_auth_c);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token2");
        username = intent.getStringExtra("userid2");
//        Log.d("token is",mytoken);
//        Log.d("username is",username);

        invoicenumber = (EditText) findViewById(R.id.invoicenumberpreauthc);


        pay = (Button) findViewById(R.id.proceedpreauthc);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCompletionAmount(invoicenumber.getText().toString());
            }
        });


        invoicenumber.setFocusableInTouchMode(true);
        invoicenumber.requestFocus();

        if (invoicenumber.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }



    }

    private void getCompletionAmount(String invoicenumber) {
        if(invoicenumber.length() != 6){
            Toast.makeText(getApplicationContext(),"Enter 6 Digit Invoice Number",Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent i = new Intent(InvoiceNumberPreAuthC.this,PreAuthCompletionAmount.class);
            i.putExtra("InvoiceNumberPreAuthCompletion",invoicenumber);
            i.putExtra("token2",mytoken);
            i.putExtra("userid2",username);

            startActivity(i);
        }

    }
}
