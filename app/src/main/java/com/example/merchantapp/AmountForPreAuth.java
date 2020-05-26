package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AmountForPreAuth extends AppCompatActivity {

    Button paybtn;
    EditText amount;
    Float f1;
    FileOutputStream fOut;
    String mytoken,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_for_pre_auth);
        Intent intent = getIntent();
        mytoken = intent.getStringExtra("token2");
        username = intent.getStringExtra("userid2");
//        Log.d("token is",mytoken);
//        Log.d("username is",username);


        paybtn = (Button) findViewById(R.id.pay);
        amount = (EditText) findViewById(R.id.amount);

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


                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Amount",Toast.LENGTH_LONG).show();
                }
                else if(Float.parseFloat(amount.getText().toString()) >  2000){
                    Toast.makeText(getApplicationContext(),"Enter amount less than or equal to ₹2000.00",Toast.LENGTH_LONG).show();
                }
                else
                {
                    f1 = Float.valueOf(amount.getText().toString());
                    f1 = f1 * 10;
                    String amount = f1 + "";
                    amount = amount.replace(".","");
                   // Toast.makeText(getApplicationContext(),"Amount is "+amount,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"Tap Your Card",Toast.LENGTH_LONG).show();

                    Intent i = new Intent(AmountForPreAuth.this,PreAuthActivity.class);
                    i.putExtra("SaleAmount",amount);
                    i.putExtra("token2",mytoken);
                    i.putExtra("userid2",username);

                    startActivity(i);
                }
                // Toast.makeText(getApplicationContext(),"Swipe or Insert Card",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getStann()
    {
        try {
            File root = new File(Environment.getExternalStorageState(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "abc.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("123");
            writer.flush();
            writer.close();
            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void createFile() {
        try {
            // catches IOException below
            final String TESTSTRING = "125";
            fOut = openFileOutput("samplefile.txt",
                    MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(TESTSTRING);
            osw.flush();
            osw.close();
            Log.i("","file stored");
        } catch (IOException ioe)
        {ioe.printStackTrace();}


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra("token",mytoken);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}
