package com.example.merchantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ScanCodeActivity extends AppCompatActivity {

    String TAG="GenerateQrCode";
    ImageView qring;
    Button start;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String value;
    TextView Amt;
    String MerchantName,AMount;
    TextView merchant,merchantAmount,TraNNr;
    Double d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        qring = (ImageView) findViewById(R.id.qrcode);
        start = (Button) findViewById(R.id.donebqresponse);
        String token = getIntent().getStringExtra("token");
        String username = getIntent().getStringExtra("username");
        String FCMTOKEN = getIntent().getStringExtra("fcmToken");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanCodeActivity.this,HomeActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("username",username);
                intent.putExtra("fcmToken",FCMTOKEN);
                intent.putExtra("ownername",MerchantName);
                startActivity(intent);

            }
        });


        value =  getIntent().getStringExtra("F048");

        MerchantName = getIntent().getStringExtra("MerchantName");
        merchant = (TextView) findViewById(R.id.merchantname);
        merchant.setText("Merchant Name: "+MerchantName);


        Amt = (TextView) findViewById(R.id.merchantamount);
        AMount = getIntent().getStringExtra("BQRAmount");
        Amt.setText("Requested Amount: ₹" +AMount);

        String TransactionNo = getIntent().getStringExtra("TranNr");
        TraNNr = (TextView) findViewById(R.id.merchantTranNr);
        TraNNr.setText("Transaction Nr: " +TransactionNo);


        if(value.length() > 0){
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerdimension = width<height ? width:height;
            smallerdimension= smallerdimension*3/4;
            qrgEncoder=new QRGEncoder(value,null, QRGContents.Type.TEXT,smallerdimension);

            try{
                bitmap = qrgEncoder.encodeAsBitmap();
                qring.setImageBitmap(bitmap);

            }
            catch (WriterException e){
                Log.v(TAG,e.toString());

            }
        }
        else
        {
            value = "Required";
        }

       // Amt.setText("QR Generated for the Amount: "+value);




//        d = Double.parseDouble(value.trim())/100;
//        value = d.toString();


        //  double amount = 15;
//        NumberFormat formatter = new DecimalFormat("#0.00");
//        // System.out.println("The Decimal Value is:"+formatter.format(d));
//        value = formatter.format(d);


    //    merchantAmount.setText("Requested Amount: ₹"+  value);

    }
}
