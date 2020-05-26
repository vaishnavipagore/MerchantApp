package com.example.merchantapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

   private static final String TAG = "FirebaseMessagingServ";
   String TranNr;
   Context context;

//    AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("TOKEN",s);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // super.onMessageReceived(remoteMessage);
       // RemoteMessage.Notification notification = remoteMessage.getNotification();

        Log.d(TAG,"From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0){

            Log.d(TAG,"Message data payload: " + remoteMessage.getData());


//            TranNr = TranNr.substring(5,10);
//            Log.d("Tran",TranNr);

        }
        if(remoteMessage.getNotification() != null){
//            Log.d(TAG,"Message Body" + remoteMessage.getNotification().getBody());
//            TranNr = remoteMessage.getData().toString();
//            TranNr = TranNr.substring(1,2).toString();
//            Log.d("Tran",TranNr);

            String params = remoteMessage.getNotification().getBody();

            Log.e("Result", params.toString());

            String TranNr = params.substring(0,12);
            Log.e("TranNr is",TranNr);


            String resultDate = params.substring(14,24);
            Log.e("Date is",resultDate);

            String resultAmt = params.substring(36,params.length());

            Double d = Double.parseDouble(resultAmt.trim())/100;
            resultAmt = d.toString();


            //  double amount = 15;
            NumberFormat formatter = new DecimalFormat("#0.00");
            // System.out.println("The Decimal Value is:"+formatter.format(d));
            resultAmt = formatter.format(d);
            Log.e("Amt is",resultAmt);




           doPopup(resultAmt,resultDate,TranNr);





        }

     //   String message = remoteMessage.getData().get("message");

       // sendNotification(TranNr);
    }

    private void doPopup(String amt,String date,String tran) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i < 15; i++) {
                    Toast.makeText(getApplicationContext(), "The transaction of amount ₹" + amt + " is successful.\n Date/Time: " + date + " \nRef: " + tran, Toast.LENGTH_SHORT).show();
                }






            }
        });
    }


    private void sendNotification(String messageBody) {
        int notificationId = new Random().nextInt();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ac_unit)
                .setContentTitle("Testing Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}


