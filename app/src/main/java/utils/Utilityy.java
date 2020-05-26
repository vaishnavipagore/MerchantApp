/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kishore
 */
public class Utilityy {
    
    public static String getStan()
    {
        File stanfile;
        String str = "";
        
        try
        {
            stanfile = new File("D:\\stan.txt");
            if (stanfile.exists())
            {
                BufferedReader br = new BufferedReader(new FileReader(stanfile)); 
                String stan; 
                while ((stan = br.readLine()) != null) 
                {
                  str = stan;
                }     
            }                     
            else
            {
                stanfile.createNewFile();
                FileWriter writer = new FileWriter(stanfile);
                writer.write("0");
                writer.flush();
                
                BufferedReader br = new BufferedReader(new FileReader(stanfile)); 
                String stan; 
                while ((stan = br.readLine()) != null) 
                {
                  str = stan;
                }
            }
            
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Log.d("Str",str);
        return str;
    }
    
    public static String getCurrentStan()
    {
        File stanfile;
        String currentStan = "";
        int stan = 0;
        
        try
        {
            stanfile = new File("D:\\stan.txt");
            
            BufferedReader br = new BufferedReader(new FileReader(stanfile)); 
            String stn; 
            while ((stn = br.readLine()) != null) 
            {
              currentStan = stn;
            }
                
            stan = Integer.parseInt(currentStan)+1;

            FileWriter writer = new FileWriter(stanfile);
            writer.write(String.valueOf(stan));
            writer.flush();
            
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return String.valueOf(stan);
    }
    
    public static String getCurrentInvoice()
    {
        File invoicefile;
        String str = "";
        
        try
        {
            invoicefile = new File("D:\\Invoice.txt");

            if (invoicefile.exists())
            {
                BufferedReader br = new BufferedReader(new FileReader(invoicefile)); 
                String invoice; 
                while ((invoice = br.readLine()) != null) 
                {
                  str = invoice;
                }
            }                     
            else
            {
                invoicefile.createNewFile();
                FileWriter writer = new FileWriter(invoicefile);
                writer.write("0");
                writer.flush();
                
                BufferedReader br = new BufferedReader(new FileReader(invoicefile)); 
                String invoice; 
                while ((invoice = br.readLine()) != null) 
                {
                  str = invoice;
                }
            }
            
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return str;
    }
    
    public static String incrementInvoice()
    {
        File invoicefile;
        String incrementInvoice = "";
        int invoice = 0;
        try
        {
            invoicefile = new File("D:\\Invoice.txt");
            
            BufferedReader br = new BufferedReader(new FileReader(invoicefile)); 
            String inv; 
            while ((inv = br.readLine()) != null) 
            {
              incrementInvoice = inv;
            }
                
            invoice = Integer.parseInt(incrementInvoice)+1;

            FileWriter writer = new FileWriter(invoicefile);
            writer.write(String.valueOf(invoice));
            writer.flush();
                
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return String.valueOf(invoice);
    }
    
    public static String getBatchNr()
    {
        File batchnrfile;
        String str = "";
        
        try
        {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMdd");
            String filename = formatter.format(date);
            batchnrfile = new File("D:\\Batch_"+filename+".txt");

            if (batchnrfile.exists())
            {
                BufferedReader br = new BufferedReader(new FileReader(batchnrfile)); 
                String invoice; 
                while ((invoice = br.readLine()) != null) 
                {
                  str = invoice;
                }
            }                     
            else
            {
                batchnrfile.createNewFile();
                FileWriter writer = new FileWriter(batchnrfile);
                writer.write("0");
                writer.flush();
                
                BufferedReader br = new BufferedReader(new FileReader(batchnrfile)); 
                String invoice; 
                while ((invoice = br.readLine()) != null) 
                {
                  str = invoice;
                }
            }
            
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return str;
    }
    
    public static String incrementBatchNr()
    {
        File batchnrfile;
        String incrementBatchNr = "";
        int batchNr = 0;
        try
        {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMdd");
            String filename = formatter.format(date);
            batchnrfile = new File("D:\\Batch_"+filename+".txt");
            
            BufferedReader br = new BufferedReader(new FileReader(batchnrfile)); 
            String batch; 
            while ((batch = br.readLine()) != null) 
            {
              incrementBatchNr = batch;
            }
                
            batchNr = Integer.parseInt(incrementBatchNr)+1;

            FileWriter writer = new FileWriter(batchnrfile);
            writer.write(String.valueOf(batchNr));
            writer.flush();
                
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return String.valueOf(batchNr);
    }
    
    public static String SendandReceiveHTTPMsg(String request)
    {
        String response;
        try
        {
            URL url = new URL("");
            URLConnection uconn = url.openConnection();
            uconn.setReadTimeout(4000);
            uconn.setDoInput(true);
            uconn.setDoOutput(true);

            try ( DataOutputStream dos = new DataOutputStream(uconn.getOutputStream()))
            {
                dos.writeBytes(request);
                dos.flush();

                try ( DataInputStream dis = new DataInputStream(uconn.getInputStream()))
                {
                    StringBuilder str = new StringBuilder();
                    int x;

                    while ((x = dis.read()) != -1)
                    {
                        str.append((char) x);
                    }

                    response = str.toString();
                }
            }
        } catch (SocketTimeoutException stex)
        {
            return "TO";
        } catch (IOException ex)
        {
            return "IO";
        }

        return response;

    }
    
}
