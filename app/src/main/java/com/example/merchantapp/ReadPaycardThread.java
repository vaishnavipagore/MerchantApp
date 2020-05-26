package com.example.merchantapp;


import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import emv.Tlv;
import helper.Config;
import helper.ReadPaycardConstsHelper;
import object.AflObject;
import utils.AflUtil;
import utils.AidUtil;
import utils.DolUtil;
import utils.EmvUtil;
import utils.GacUtil;
import utils.GpoUtil;
import utils.HexUtil;
import utils.LogUtil;
import utils.PseUtil;
import utils.TlvUtil;
import utils.Translate;

import static helper.Config.tlv2;

public class ReadPaycardThread implements Runnable {
    private static final String TAG = ReadPaycardThread.class.getSimpleName();
    private Context mContext;
    public String myAID;
    public String CardNumber1;
    public String ExpiryDate1;
    public String CardType;
    public String TAmount;
    TextView AID;



    private IsoDep mIsoDep = null;
    public ReadPaycardThread(@NonNull Context context, @NonNull Tag tag, String str) {
        mContext = context;
        System.out.print("String in RPT"+str);
        TAmount = str;
      //  Log.d("ReadPayCardThread",str);

        try {
            mIsoDep = IsoDep.get(tag);
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    private void cannotReadPaycard() {
        // LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ReadPaycardActivity.ACTION_CANNOT_READ_PAYCARD_BROADCAST));
        Toast.makeText(mContext,"Cannot Read",Toast.LENGTH_LONG).show();
    }


    @Override
    public void run() {
        LogUtil.d(TAG, "\"" + TAG + "\": Thread run");
       // Toast.makeText(mContext,"Inside Run",Toast.LENGTH_LONG).show();
        if (mIsoDep == null) {
            return;
        }

        if (mIsoDep.getTag() == null) {
            return;
        }
        LogUtil.d(TAG, "ISO-DEP - Compatible NFC tag discovered: " + mIsoDep.getTag());

        // ISO-DEP - Connect
        connect();
// - ISO-DEP - Connect


        boolean isPayPass = false, isPayWave = false, isqSparc = false;

        // Thread relative
        // ATS (Answer To Select)
        // NfcA (ISO 14443-3A)
        byte[] historicalBytes = null;

        try {
            historicalBytes = mIsoDep.getHistoricalBytes();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (historicalBytes != null && historicalBytes.length > 0) {
            LogUtil.d(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_a) + ": Supported");

            LogUtil.d(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_a) + ": " + Arrays.toString(historicalBytes));
            LogUtil.d(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_a) + " Hexadecimal: " + HexUtil.bytesToHexadecimal(historicalBytes));
        } else {
            LogUtil.w(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_a) + ": Not supported");
        }
        // - NfcA (ISO 14443-3A)

        // NfcB (ISO 14443-3B)
        byte[] hiLayerResponse = null;

        try {
            hiLayerResponse = mIsoDep.getHiLayerResponse();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (hiLayerResponse != null && hiLayerResponse.length > 0) {
            LogUtil.d(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_b) + ": Supported");

            LogUtil.d(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_b) + ": " + Arrays.toString(hiLayerResponse));
            LogUtil.d(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_b) + " Hexadecimal: " + HexUtil.bytesToHexadecimal(hiLayerResponse));
        } else {
            LogUtil.w(TAG, "ISO-DEP - " + mContext.getString(R.string.nfc_b) + ": Not supported");
        }
        // - NfcB (ISO 14443-3B)
        // - ATS (Answer To Select)

        // PSE (Payment System Environment)
        byte[] cPse = null, rPse = null;
        boolean pseSucceed = false;

        cPse = PseUtil.selectPse(null);

        if (cPse != null) {
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.pse) + "\": " + Arrays.toString(cPse));
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.pse) + "\" Hexadecimal: " + HexUtil.bytesToHexadecimal(cPse));

            try {
                rPse = mIsoDep.transceive(cPse);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                LogUtil.e(TAG, e.toString());

                e.printStackTrace();
            }

            if (rPse != null) {
                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.pse) + "\": " + Arrays.toString(rPse));

                String rPseHexadecimal = HexUtil.bytesToHexadecimal(rPse);
                if (rPseHexadecimal != null) {
                    LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.pse) + "\" Hexadecimal: " + rPseHexadecimal);
                }

                // ----

                if (EmvUtil.isOk(rPse)) {
                    pseSucceed = true;

                    LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.pse) + "\": Succeed");
                } else {
                    LogUtil.w(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.pse) + "\": Not succeed");

                    // TODO: Get response SW1 & SW2, check response SW1 & SW2, log the result
                }
            }
        }
        // - PSE (Payment System Environment)
        // PPSE (Proximity Payment System Environment)
        byte[] cPpse = null, rPpse = null;
        boolean ppseSucceed = false;

        cPpse = PseUtil.selectPpse(null);

        if (cPpse != null) {
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.ppse) + "\": " + Arrays.toString(cPpse));
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.ppse) + "\" Hexadecimal: " + HexUtil.bytesToHexadecimal(cPpse));

            try {
                rPpse = mIsoDep.transceive(PseUtil.selectPpse(null));
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                LogUtil.e(TAG, e.toString());

                e.printStackTrace();
            }

            if (rPpse != null) {
                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.ppse) + "\": " + Arrays.toString(rPpse));

                String rPpseHexadecimal = HexUtil.bytesToHexadecimal(rPpse);
                if (rPpseHexadecimal != null) {
                    LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.ppse) + "\" Hexadecimal: " + rPpseHexadecimal);
                }

                // ----

                if (EmvUtil.isOk(rPpse)) {
                    ppseSucceed = true;

                    LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.ppse) + "\": Succeed");
                } else {
                    LogUtil.w(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.ppse) + "\": Not succeed");

                    // TODO: Get response SW1 & SW2, check response SW1 & SW2, log the result
                }
            }
        }
        // - PPSE (Proximity Payment System Environment)

        if (!pseSucceed && !ppseSucceed) {
            cannotReadPaycard();
            return;
        }

        // TLV Extractable Data
        byte[] aid = null; // AID (Application Identifier)
        byte[] applicationLabel = null; // Application Label
        String applicationLabelAscii = null; // Application Label ASCII
        byte[] applicationPan = null; // Application PAN (Primary Account Number)
        byte[] cardholderName = null; // Cardholder Name
        String cardholderNameAscii = null; // Cardholder Name ASCII
        byte[] applicationExpirationDate = null; // Application Expiration Date
        // - TLV Extractable Data



        // AID (Application Identifier)
        if (aid == null && pseSucceed) {
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(rPse);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                LogUtil.e(TAG, e.toString());

                e.printStackTrace();
            }

            if (byteArrayInputStream != null) {
                if (byteArrayInputStream.available() < 2) {
                    try {
                        throw new Exception("Cannot preform TLV byte array stream actions, available bytes < 2; Length is " + byteArrayInputStream.available());
                    } catch (Exception e) {
                        LogUtil.e(TAG, e.getMessage());
                        LogUtil.e(TAG, e.toString());

                        e.printStackTrace();
                    }
                } else {
                    int i = 0, resultSize;

                    byte[] aidTlvTagLength = new byte[ReadPaycardConstsHelper.AID_TLV_TAG.length];

                    while (byteArrayInputStream.read() != -1) {
                        i += 1;

                        if (i >= ReadPaycardConstsHelper.AID_TLV_TAG.length) {
                            aidTlvTagLength = Arrays.copyOfRange(rPse, i - ReadPaycardConstsHelper.AID_TLV_TAG.length, i);
                        }

                        if (Arrays.equals(ReadPaycardConstsHelper.AID_TLV_TAG, aidTlvTagLength)) {
                            resultSize = byteArrayInputStream.read();

                            if (resultSize > byteArrayInputStream.available()) {
                                continue;
                            }

                            if (resultSize != -1) {
                                byte[] resultRes = new byte[resultSize];

                                if (byteArrayInputStream.read(resultRes, 0, resultSize) != 0) {
                                    if (Arrays.equals(resultRes, AidUtil.A0000000041010)) {
                                        isPayPass = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    }
                                    else if (Arrays.equals(resultRes, AidUtil.A0000005241010)) {
                                            isqSparc = true;

                                            aid = resultRes;

                                            LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                        }


                                    else if (Arrays.equals(resultRes, AidUtil.A0000000043060)) {
                                        isPayPass = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    } else if (Arrays.equals(resultRes, AidUtil.A0000000031010)) {
                                        isPayWave = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    } else if (Arrays.equals(resultRes, AidUtil.A0000000032010)) {
                                        isPayWave = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    }
                                }
                            }

                        }

                    }
                }

                try {
                    byteArrayInputStream.close();
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        }

        if (aid == null && ppseSucceed) {
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(rPpse);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                LogUtil.e(TAG, e.toString());

                e.printStackTrace();
            }

            if (byteArrayInputStream != null) {
                if (byteArrayInputStream.available() < 2) {
                    try {
                        throw new Exception("Cannot preform TLV byte array stream actions, available bytes < 2; Length is " + byteArrayInputStream.available());
                    } catch (Exception e) {
                        LogUtil.e(TAG, e.getMessage());
                        LogUtil.e(TAG, e.toString());

                        e.printStackTrace();
                    }
                } else {
                    int i = 0, resultSize;

                    byte[] aidTlvTagLength = new byte[ReadPaycardConstsHelper.AID_TLV_TAG.length];

                    while (byteArrayInputStream.read() != -1) {
                        i += 1;

                        if (i >= ReadPaycardConstsHelper.AID_TLV_TAG.length) {
                            aidTlvTagLength = Arrays.copyOfRange(rPpse, i - ReadPaycardConstsHelper.AID_TLV_TAG.length, i);
                        }

                        if (Arrays.equals(ReadPaycardConstsHelper.AID_TLV_TAG, aidTlvTagLength)) {
                            resultSize = byteArrayInputStream.read();

                            if (resultSize > byteArrayInputStream.available()) {
                                continue;
                            }

                            if (resultSize != -1) {
                                byte[] resultRes = new byte[resultSize];

                                if (byteArrayInputStream.read(resultRes, 0, resultSize) != 0) {
                                    if (Arrays.equals(resultRes, AidUtil.A0000000041010)) {
                                        isPayPass = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    }
                                    else if (Arrays.equals(resultRes, AidUtil.A0000005241010)) {
                                        isqSparc = true;


                                            aid = resultRes;

                                            LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                        }



                                        else if (Arrays.equals(resultRes, AidUtil.A0000000043060)) {
                                        isPayPass = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    } else if (Arrays.equals(resultRes, AidUtil.A0000000031010)) {
                                        isPayWave = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    } else if (Arrays.equals(resultRes, AidUtil.A0000000032010)) {
                                        isPayWave = true;

                                        aid = resultRes;

                                        LogUtil.d(TAG, mContext.getString(R.string.aid) + " Found: " + Arrays.toString(resultRes));
                                    }
                                }
                            }
                        }

                    }
                }

                try {
                    byteArrayInputStream.close();
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        }

        Config.tlv = new Tlv();

        if (aid != null) {
            LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.aid) + " [4F]\": " + Arrays.toString(aid));

            String aidHexadecimal = HexUtil.bytesToHexadecimal(aid);
            if (aidHexadecimal != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.aid) + " [4F]\" Hexadecimal: " + aidHexadecimal);
//                SaleActivity saleActivity = new SaleActivity();
//                saleActivity.myFunc(aidHexadecimal);
                myAID = aidHexadecimal;

                Config.tlv.putField(emv.Tag._4F_APPLICATION_IDENTIFIER, Translate.fromHexToBin(myAID));

            }
        } else {
            cannotReadPaycard();
            return;
        }
        // - AID (Application Identifier)

        // FCI (File Control Information)
        byte[] cFci = null, rFci = null;

        if (Arrays.equals(aid, AidUtil.A0000000041010)) {
            cFci = AidUtil.selectAid(AidUtil.A0000000041010); // Mastercard (PayPass)

            if (cFci != null) {
                try {
                    rFci = mIsoDep.transceive(AidUtil.selectAid(AidUtil.A0000000041010));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        } else if (Arrays.equals(aid, AidUtil.A0000000043060)) {
            cFci = AidUtil.selectAid(AidUtil.A0000000043060); // Maestro (PayPass)

            if (cFci != null) {
                try {
                    rFci = mIsoDep.transceive(AidUtil.selectAid(AidUtil.A0000000043060));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        }
        else if (Arrays.equals(aid, AidUtil.A0000005241010)) {
            cFci = AidUtil.selectAid(AidUtil.A0000005241010); // RUPAY (qSparc)

            if (cFci != null) {
                try {
                    rFci = mIsoDep.transceive(AidUtil.selectAid(AidUtil.A0000005241010));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        }else if (Arrays.equals(aid, AidUtil.A0000000031010)) {
            cFci = AidUtil.selectAid(AidUtil.A0000000031010); // Visa (PayWave)

            if (cFci != null) {
                try {
                    rFci = mIsoDep.transceive(AidUtil.selectAid(AidUtil.A0000000031010));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        } else if (Arrays.equals(aid, AidUtil.A0000000032010)) {
            cFci = AidUtil.selectAid(AidUtil.A0000000032010); // Visa Electron (PayWave)

            if (cFci != null) {
                try {
                    rFci = mIsoDep.transceive(AidUtil.selectAid(AidUtil.A0000000032010));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        }

        if (cFci != null) {
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.fci) + "\": " + Arrays.toString(cFci));
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.fci) + "\" Hexadecimal: " + HexUtil.bytesToHexadecimal(cFci));
        }

        if (rFci != null) {
            LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.fci) + "\": " + Arrays.toString(rFci));

            String rFciHexadecimal = HexUtil.bytesToHexadecimal(rFci);
            if (rFciHexadecimal != null) {
                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.fci) + "\" Hexadecimal: " + rFciHexadecimal);
            }

            if (EmvUtil.isOk(rFci)) {
                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.fci) + "\": Succeed");
            } else {
                LogUtil.w(TAG, "EMV (R-APDU) - Command: \"Select\"; Data: \"" + mContext.getString(R.string.fci) + "\": Not succeed");

                // TODO: Get response SW1 & SW2, check response SW1 & SW2, log the result

                cannotReadPaycard();
                return;
            }
        } else {
            cannotReadPaycard();
            return;
        }
        // - FCI (File Control Information)

        // Application Label (May be ASCII convertible)
        if (applicationLabel == null) {
            applicationLabel = new TlvUtil().getTlvValue(rFci, ReadPaycardConstsHelper.APPLICATION_LABEL_TLV_TAG);

            if (applicationLabel != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_label) + " [50]\": " + Arrays.toString(applicationLabel));

                String applicationLabelHexadecimal = HexUtil.bytesToHexadecimal(applicationLabel);
                if (applicationLabelHexadecimal != null) {
                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_label) + " [50]\" Hexadecimal: " + applicationLabelHexadecimal);

                    // ----

                    String tempApplicationLabelAscii = HexUtil.hexadecimalToAscii(applicationLabelHexadecimal);
                    if (tempApplicationLabelAscii != null) {
                        applicationLabelAscii = tempApplicationLabelAscii;

                        LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_label) + " [50]\" ASCII: " + applicationLabelAscii);
                        CardType = applicationLabelAscii;
                    }
                }
            }
        }
        // - Application Label (May be ASCII convertible)

        // PDOL (Processing Options Data Object List)
        byte[] pdol = null, tempPdol = new TlvUtil().getTlvValue(rFci, ReadPaycardConstsHelper.PDOL_TLV_TAG);

        if (tempPdol != null && DolUtil.isValidDol(tempPdol, ReadPaycardConstsHelper.PDOL_TLV_TAG)) {
            pdol = tempPdol;

            LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.pdol) + " [9F38]\": " + Arrays.toString(pdol));

            String pdolHexadecimal = HexUtil.bytesToHexadecimal(pdol);
            if (pdolHexadecimal != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.pdol) + " [9F38]\" Hexadecimal: " + pdolHexadecimal);
            }
        }
        // - PDOL (Processing Options Data Object List)



        // PDOL Constructed
        byte[] pdolConstructed = new GpoUtil().fillPdol(pdol,TAmount);
        System.out.print("TAmount"+TAmount);
        LogUtil.d("TLV",Config.tlv.toFormattedString());

        if (pdolConstructed != null) {
            LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.pdol) + " Constructed\": " + Arrays.toString(pdolConstructed));

            String pdolConstructedHexadecimal = HexUtil.bytesToHexadecimal(pdolConstructed);
            if (pdolConstructedHexadecimal != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.pdol) + " Constructed\" Hexadecimal: " + pdolConstructedHexadecimal);
            }
        } else {
            cannotReadPaycard();
            return;
        }
        // - PDOL Constructed


        // GPO (Get Processing Options)
        byte[] cGpo = new GpoUtil().cGpo(pdolConstructed), rGpo = null; // C-APDU & R-APDU

        if (cGpo != null) {
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Get Data\"; Data: \"" + mContext.getString(R.string.gpo) + "\": " + Arrays.toString(cGpo));
            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Get Data\"; Data: \"" + mContext.getString(R.string.gpo) + "\" Hexadecimal: " + HexUtil.bytesToHexadecimal(cGpo));

            try {
                rGpo = mIsoDep.transceive(cGpo);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                LogUtil.e(TAG, e.toString());

                e.printStackTrace();
            }
        }

        if (rGpo != null) {
            LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Get Data\"; Data: \"" + mContext.getString(R.string.gpo) + "\": " + Arrays.toString(rGpo));

            String rGpoHexadecimal = HexUtil.bytesToHexadecimal(rGpo);
            if (rGpoHexadecimal != null) {
                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Get Data\"; Data: \"" + mContext.getString(R.string.gpo) + "\" Hexadecimal: " + rGpoHexadecimal);
            }

            if (EmvUtil.isOk(rGpo)) {
                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Get Data\"; Data: \"" + mContext.getString(R.string.gpo) + "\": Succeed");

                String emv_data=Translate.fromBinToHex(Translate.getString(Config.tlv.toMsg()))+rGpoHexadecimal.substring(4);
                LogUtil.d("EMV2",emv_data);
                Config.tlv2 = new Tlv(Translate.fromHexToBin(emv_data));
                LogUtil.d("TLV2",Config.tlv2.toFormattedString());
            } else {
                LogUtil.w(TAG, "EMV (R-APDU) - Command: \"Get Data\"; Data: \"" + mContext.getString(R.string.gpo) + "\": Not succeed");

                // TODO: Get response SW1 & SW2, check response SW1 & SW2, log the result

                cannotReadPaycard();
                return;
            }
        } else {
            cannotReadPaycard();
            return;
        }
        // - GPO (Get Processing Options)


        // PayWave Only
        if (isPayWave) {
            // Application PAN (Primary Account Number)
            if (applicationPan == null) {
                applicationPan = new TlvUtil().getTlvValue(rGpo, ReadPaycardConstsHelper.APPLICATION_PAN_TLV_TAG);

                if (applicationPan != null) {
                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_pan) + " [5A]\": " + Arrays.toString(applicationPan));

                    String applicationPanHexadecimal = HexUtil.bytesToHexadecimal(applicationPan);
                    if (applicationPanHexadecimal != null) {
                        LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_pan) + " [5A]\" Hexadecimal: " + applicationPanHexadecimal);
                        CardNumber1 = applicationPanHexadecimal;
                        Log.d("CardNumber Thread",CardNumber1);
                    }
                }
            }
            // - Application PAN (Primary Account Number)

            // Cardholder Name (May be ASCII convertible)
            if (cardholderName == null) {
                cardholderName = new TlvUtil().getTlvValue(rGpo, ReadPaycardConstsHelper.CARDHOLDER_NAME_TLV_TAG);

                if (cardholderName != null) {
                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cardholder_name) + " [5F20]\": " + Arrays.toString(cardholderName));

                    String cardholderNameHexadecimal = HexUtil.bytesToHexadecimal(cardholderName);
                    if (cardholderNameHexadecimal != null) {
                        LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cardholder_name) + " [5F20]\" Hexadecimal: " + cardholderNameHexadecimal);

                        // ----

                        String tempCardholderNameAscii = HexUtil.hexadecimalToAscii(cardholderNameHexadecimal);
                        if (tempCardholderNameAscii != null) {
                            cardholderNameAscii = tempCardholderNameAscii;

                            LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cardholder_name) + " [5F20]\" ASCII: " + cardholderNameAscii);
                        }
                    }
                }
            }
            // - Cardholder Name (May be ASCII convertible)

            // Application Expiration Date
            if (applicationExpirationDate == null) {
                applicationExpirationDate = new TlvUtil().getTlvValue(rGpo, ReadPaycardConstsHelper.APPLICATION_EXPIRATION_DATE_TLV_TAG);

                if (applicationExpirationDate != null) {
                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_expiration_date) + "[5F24]\": " + Arrays.toString(applicationExpirationDate));

                    String applicationExpirationDateHexadecimal = HexUtil.bytesToHexadecimal(applicationExpirationDate);
                    if (applicationExpirationDateHexadecimal != null) {
                        LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_expiration_date) + " [5F24]\" Hexadecimal: " + applicationExpirationDateHexadecimal);
                        ExpiryDate1 = applicationExpirationDateHexadecimal;
                    }
                }
            }
            // - Application Expiration Date
        }
        // - PayWave Only


        // AFL (Application File Locator) [GPO] Data
        byte[] aflData = null;

        // Response message template 1 (without tags and lengths)
        if (rGpo[0] == ReadPaycardConstsHelper.GPO_RMT1_TLV_TAG[0]) {
            LogUtil.d(TAG, mContext.getString(R.string.gpo) + " Response message template 1");

            byte[] gpoData80 = null;
        }
        // - Response message 1 (without tags and lengths)


        // Response message template 2 (with tags and lengths)
        if (rGpo[0] == ReadPaycardConstsHelper.GPO_RMT2_TLV_TAG[0]) {
            LogUtil.d(TAG, mContext.getString(R.string.gpo) + " Response message template 2");

            byte[] gpoData77 = null;

            gpoData77 = new TlvUtil().getTlvValue(rGpo, ReadPaycardConstsHelper.GPO_RMT2_TLV_TAG);

            if (gpoData77 != null) {
                // AFL (Application File Locator)
                byte[] afl; // TLV (Type-length-value) tag specified for AFL (Application File Locator) and result variable

                afl = new TlvUtil().getTlvValue(rGpo, ReadPaycardConstsHelper.AFL_TLV_TAG);

                if (afl != null) {
                    aflData = afl;
                }
                // - AFL (Application File Locator)
            }
        }
        // - Response message template 2 (with tags and lengths)


        if (aflData != null) {
            LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.afl) + " [94]\": " + Arrays.toString(aflData));

            String alfDataHexadecimal = HexUtil.bytesToHexadecimal(aflData);
            if (alfDataHexadecimal != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.afl) + " [94]\" Hexadecimal: " + alfDataHexadecimal);
            }
        } else {
            cannotReadPaycard();
            return;
        }
        // - AFL (Application File Locator) [GPO] Data

        byte[] cdol_1 = null, cdol_2 = null; // CDOL1 (Card Risk Management Data Object List 1) & CDOL2 (Card Risk Management Data Object List 2)

        // AFL (Application File Locator) Record(s)
        //  byte[] cAflRecordsList = new RealmList<>(), rAflRecordsList = new RealmList<>();
//
        ArrayList<AflObject> aflObjectArrayList = new AflUtil().getAflDataRecords(aflData);

        if (aflObjectArrayList != null && !aflObjectArrayList.isEmpty()) {
            for (AflObject aflObject : aflObjectArrayList) {
                byte[] cReadRecord = aflObject.getReadCommand(), rReadRecord = null; // C-APDU & R-APDU

                if (cReadRecord != null) {
                    // cAflRecordsList.add(cReadRecord);

                    LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Read Record\"; Data: \"Read Record\": " + Arrays.toString(cReadRecord));
                    LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Read Record\"; Data: \"Read Record\" Hexadecimal: " + HexUtil.bytesToHexadecimal(cReadRecord));

                    try {
                        rReadRecord = mIsoDep.transceive(cReadRecord);
                    } catch (Exception e) {
                        LogUtil.e(TAG, e.getMessage());
                        LogUtil.e(TAG, e.toString());

                        e.printStackTrace();
                    }
                }

                if (rReadRecord != null) {
                    // rAflRecordsList.add(rReadRecord);

                    boolean succeedLe = false;

                    LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Read Record\"; Data: \"Read Record\": " + Arrays.toString(rReadRecord));

                    String rReadRecordHexadecimal = HexUtil.bytesToHexadecimal(rReadRecord);
                    if (rReadRecordHexadecimal != null) {
                        LogUtil.d(TAG, "EMV (R-APDU57) - Command: \"Read Record\"; Data: \"Read Record\" Hexadecimal: " + rReadRecordHexadecimal);
                    }

                    if (EmvUtil.isOk(rReadRecord)) {
                        succeedLe = true;
                    } else if (EmvUtil.getSwBytes(rReadRecord)[0] == (byte) 0x6C) {
                        cReadRecord[cReadRecord.length - 1] = (byte) (rReadRecord.length - 1); // Custom Le

                        if (cReadRecord != null) {
                            //  cAflRecordsList.add(cReadRecord);

                            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Read Record\"; Data: \"Read Record\": " + Arrays.toString(cReadRecord));
                            LogUtil.d(TAG, "EMV (C-APDU) - Command: \"Read Record\"; Data: \"Read Record\" Hexadecimal: " + HexUtil.bytesToHexadecimal(cReadRecord));

                            try {
                                rReadRecord = mIsoDep.transceive(cReadRecord);
                            } catch (Exception e) {
                                LogUtil.e(TAG, e.getMessage());
                                LogUtil.e(TAG, e.toString());

                                e.printStackTrace();
                            }
                        }

                        if (rReadRecord != null) {
                            // rAflRecordsList.add(rReadRecord);

                            LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Read Record\"; Data: \"Read Record\": " + Arrays.toString(rReadRecord));

                            String rReadRecordCustomLeHexadecimal = HexUtil.bytesToHexadecimal(rReadRecord);
                            if (rReadRecordCustomLeHexadecimal != null) {
                                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Read Record\"; Data: \"Read Record\" Hexadecimal: " + rReadRecordCustomLeHexadecimal);
                            }

                            if (EmvUtil.isOk(rReadRecord)) {
                                succeedLe = true;
                            }
                        }
                    }

                    if (succeedLe) {
                        LogUtil.d(TAG, "EMV (R-APDU) - Command: \"Read Record\"; Data: \"Read Record\": Succeed");

                        // CDOL1 (Card Risk Management Data Object List 1)
                        if (cdol_1 == null) {
                            byte[] tempCdol1 = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.CDOL_1_TLV_TAG);

                            if (tempCdol1 != null && DolUtil.isValidDol(tempCdol1, ReadPaycardConstsHelper.CDOL_1_TLV_TAG)) {
                                cdol_1 = tempCdol1;
                            }
                        }
                        // - CDOL1 (Card Risk Management Data Object List 1)

                        // CDOL2 (Card Risk Management Data Object List 2)
                        if (cdol_2 == null) {
                            byte[] tempCdol2 = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.CDOL_2_TLV_TAG);

                            if (tempCdol2 != null && DolUtil.isValidDol(tempCdol2, ReadPaycardConstsHelper.CDOL_2_TLV_TAG)) {
                                cdol_2 = tempCdol2;
                            }
                        }
                        // - CDOL2 (Card Risk Management Data Object List 2)

                        // Application PAN (Primary Account Number)
                        if (applicationPan == null) {
                            applicationPan = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.APPLICATION_PAN_TLV_TAG);

                            if (applicationPan != null) {
                                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_pan) + " [5A]\": " + Arrays.toString(applicationPan));

                                String applicationPanHexadecimal = HexUtil.bytesToHexadecimal(applicationPan);
                                if (applicationPanHexadecimal != null) {
                                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_pan) + " [5A]\" Hexadecimal: " + applicationPanHexadecimal);
                                    CardNumber1 = applicationPanHexadecimal;
                                }
                            }
                        }
                        // - Application PAN (Primary Account Number)

                        // Cardholder Name (May be ASCII convertible)
                        if (cardholderName == null) {
                            cardholderName = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.CARDHOLDER_NAME_TLV_TAG);

                            if (cardholderName != null) {
                                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cardholder_name) + " [5F20]\": " + Arrays.toString(cardholderName));

                                String cardholderNameHexadecimal = HexUtil.bytesToHexadecimal(cardholderName);
                                if (cardholderNameHexadecimal != null) {
                                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cardholder_name) + " [5F20]\" Hexadecimal: " + cardholderNameHexadecimal);

                                    // ----

                                    String tempCardholderNameAscii = HexUtil.hexadecimalToAscii(cardholderNameHexadecimal);
                                    if (tempCardholderNameAscii != null) {
                                        cardholderNameAscii = tempCardholderNameAscii;

                                        LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cardholder_name) + " [5F20]\" ASCII: " + cardholderNameAscii);
                                    }
                                }
                            }
                        }
                        // - Cardholder Name (May be ASCII convertible)

                        // Application Expiration Date
                        if (applicationExpirationDate == null) {
                            applicationExpirationDate = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.APPLICATION_EXPIRATION_DATE_TLV_TAG);

                            if (applicationExpirationDate != null) {
                                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_expiration_date) + "[5F24]\": " + Arrays.toString(applicationExpirationDate));

                                String applicationExpirationDateHexadecimal = HexUtil.bytesToHexadecimal(applicationExpirationDate);
                                if (applicationExpirationDateHexadecimal != null) {
                                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.application_expiration_date) + " [5F24]\" Hexadecimal: " + applicationExpirationDateHexadecimal);
                                    ExpiryDate1 = applicationExpirationDateHexadecimal;
                                }
                            }
                        }
                        // - Application Expiration Date

                        // Rupay qSparc


                        //Rupay qSparc Complete


                        // PayPass Only
                        if (isPayPass) {
                            // Without CVM; Signature; Offline -> Proceed with UNs
                            byte[] pUnAtcTrack1 = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.P_UN_ATC_TRACK1_TLV_TAG);
                            byte[] nAtcTrack1 = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.N_ATC_TRACK1_TLV_TAG);

                            byte[] pUnAtcTrack2 = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.P_UN_ATC_TRACK2_TLV_TAG);
                            byte[] nAtcTrack2 = new TlvUtil().getTlvValue(rReadRecord, ReadPaycardConstsHelper.N_ATC_TRACK2_TLV_TAG);

                            // PayPass cloning information
                            if (pUnAtcTrack1 != null && nAtcTrack1 != null && pUnAtcTrack2 != null && nAtcTrack2 != null) {
                                LogUtil.d(TAG, "This PayPass paycard can be cloned (using brute-force attack for unpredictable numbers)");

                                // Cloning information
                                int kTrack1 = 0, tTrack1 = nAtcTrack1[0];
                                for (byte byteOut : pUnAtcTrack1) {
                                    int i = byteOut;
                                    if (i < 0) {
                                        i += 256;
                                    }

                                    kTrack1 += Integer.bitCount(i);
                                }

                                int kTrack2 = 0, tTrack2 = nAtcTrack2[0];
                                for (byte byteOut : pUnAtcTrack2) {
                                    int i = byteOut;
                                    if (i < 0) {
                                        i += 256;
                                    }

                                    kTrack2 += Integer.bitCount(i);
                                }

                                int unDigits = Math.max(kTrack1 - tTrack1, kTrack2 - tTrack2);
                                LogUtil.d(TAG, "UN Digits: " + unDigits);

                                double totalUns = Math.pow(10, unDigits);
                                LogUtil.d(TAG, "Total UNs: " + totalUns);
                                // - Cloning information
                            } else {
                                LogUtil.d(TAG, "This PayPass paycard cannot be cloned (using brute-force attack for unpredictable numbers)");
                            }
                            // - PayPass cloning information
                            // - Without CVM; Signature; Offline -> Proceed with UNs
                        }
                        // - PayPass Only
                    } else {
                        LogUtil.w(TAG, "EMV (R-APDU) - Command: \"Read Record\"; Data: \"Read Record\": Not succeed");

                        // TODO: Get response SW1 & SW2, check response SW1 & SW2, log the result
                    }
                }
            }
        } else {
            LogUtil.w(TAG, "Will not read \"" + mContext.getString(R.string.afl) + "\" Record(s) (List is not available or empty)");

            cannotReadPaycard();
            return;
        }
        // - AFL (Application File Locator) Record(s)


        // CDOL1
        if (cdol_1 != null) {
            LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cdol_1) + " [8C]\": " + Arrays.toString(cdol_1));

            String cdol1Hexadecimal = HexUtil.bytesToHexadecimal(cdol_1);
            if (cdol1Hexadecimal != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cdol_1) + " [8C]\" Hexadecimal: " + cdol1Hexadecimal);
            }

            // CDOL1 Constructed
            byte[] cdol1Constructed = new GacUtil().fillCdol_1(cdol_1,TAmount);

            if (cdol1Constructed != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cdol_1) + " Constructed\": " + Arrays.toString(cdol1Constructed));

                String cdol1ConstructedHexadecimal = HexUtil.bytesToHexadecimal(cdol1Constructed);
                if (cdol1ConstructedHexadecimal != null) {
                    LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cdol_1) + " Constructed\" Hexadecimal: " + cdol1ConstructedHexadecimal);
                }
            }
            // - CDOL1 Constructed

            // First GAC (Generate Application Cryptogram)
            byte[] cFirstGac = new GacUtil().cGac(cdol1Constructed), rFirstGac = null; // C-APDU & R-APDU

            if (cFirstGac != null) {
                LogUtil.d(TAG, "EMV (C-APDU) - Command: \"" + mContext.getString(R.string.gac) + "\"; Data: \"First " + mContext.getString(R.string.cdol_1) + "\": " + Arrays.toString(cFirstGac));
                LogUtil.d(TAG, "EMV (C-APDU) - Command: \"" + mContext.getString(R.string.gac) + "\"; Data: \"First " + mContext.getString(R.string.cdol_1) + "\" Hexadecimal: " + HexUtil.bytesToHexadecimal(cFirstGac));

                try {
                    rFirstGac = mIsoDep.transceive(cFirstGac);
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    LogUtil.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }

            if (rFirstGac != null) {
                LogUtil.d(TAG, "EMV (R-APDU) - Command: \"" + mContext.getString(R.string.gac) + "\"; Data: \"First " + mContext.getString(R.string.cdol_1) + "\": " + Arrays.toString(rFirstGac));

                String rFirstGacHexadecimal = HexUtil.bytesToHexadecimal(rFirstGac);
                if (rFirstGacHexadecimal != null) {
                    LogUtil.d(TAG, "EMV (R-APDU) - Command: \"" + mContext.getString(R.string.gac) + "\"; Data: \"First " + mContext.getString(R.string.cdol_1) + "\" Hexadecimal: " + rFirstGacHexadecimal);
                }

                if (EmvUtil.isOk(rFirstGac)) {
                    LogUtil.d(TAG, "EMV (R-APDU) - Command: \"" + mContext.getString(R.string.gac) + "\"; Data: \"First " + mContext.getString(R.string.cdol_1) + "\": Succeed");
                } else {
                    LogUtil.w(TAG, "EMV (R-APDU) - Command: \"" + mContext.getString(R.string.gac) + "\"; Data: \"First " + mContext.getString(R.string.cdol_1) + "\": Not succeed");

                    // TODO: Get response SW1 & SW2, check response SW1 & SW2, log the result
                }
            }
            // - First GAC (Generate Application Cryptogram)
        }
        // - CDOL1

        // CDOL2
        if (cdol_2 != null) {
            LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cdol_2) + " [8D]\": " + Arrays.toString(cdol_2));

            String cdol_2Hexadecimal = HexUtil.bytesToHexadecimal(cdol_2);
            if (cdol_2Hexadecimal != null) {
                LogUtil.d(TAG, "EMV (TLV) - Data: \"" + mContext.getString(R.string.cdol_2) + " [8D]\" Hexadecimal: " + cdol_2Hexadecimal);
            }
        }
        // - CDOL2

        // PayPass Only
        if (isPayPass) {

        }
        // - PayPass Only

        // PayWave Only
        if (isPayWave) {

        }
        // - PayWave Only
        // - Thread relative

        // ISO-DEP - Close
        close();
        // - ISO-DEP - Close



    }



    private void connect() {
        if (mIsoDep == null) {
            LogUtil.w(TAG, "ISO-DEP - Connect failed, no actionable instance found");

            return;
        }

        if (mIsoDep.getTag() == null) {
            LogUtil.w(TAG, "ISO-DEP - Connect failed, tag not found");

            return;
        }

        // Try to enable I/O operations to the tag
        LogUtil.d(TAG, "ISO-DEP - Trying to enable I/O operations to the tag...");
        try {
            mIsoDep.connect();
        } catch (Exception e) {
            LogUtil.e(TAG, "ISO-DEP - Exception while trying to enable I/O operations to the tag");

            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        } finally {
            if (mIsoDep.isConnected()) {
                LogUtil.d(TAG, "ISO-DEP - Enabled I/O operations to the tag");
            } else {
                LogUtil.w(TAG, "ISO-DEP - Not enabled I/O operations to the tag");
            }
        }
        // - Try to enable I/O operations to the tag
    }

    private void close() {
        if (mIsoDep == null) {
            LogUtil.w(TAG, "ISO-DEP - Close failed, no actionable instance found");

            return;
        }

        if (mIsoDep.getTag() == null) {
            LogUtil.w(TAG, "ISO-DEP - Close failed, tag not found");

            return;
        }

        // Try to disable I/O operations to the tag
        LogUtil.d(TAG, "ISO-DEP - Trying to disable I/O operations to the tag...");
        try {

          //  SaleActivity.AID1.setText("AID: "+ myAID);

            String str4 = CardNumber1;
            str4 = str4.substring(0,4)+ " " +str4.substring(4,str4.length());
            str4 = str4.substring(0,9)+ " " +str4.substring(9,str4.length());
            str4 = str4.substring(0,14)+ " " +str4.substring(14,str4.length());
            System.out.println("Cardnumber is: "+str4);
            SaleActivity.CardNumber.setText(""+str4);
            String str5 = ExpiryDate1;
            str5 = str5.substring(0,2)+ "-" +str5.substring(2,str5.length());
            str5 = str5.substring(0,5)+ "-" +str5.substring(5,str5.length());
           // str5 = str5.substring(0,9)+ "O" +str5.substring(9,str5.length());
            str5 = str5.concat(" [YY-MM-DD]");
            SaleActivity.ExpiryDate.setText("Expiry Date: "+ str5);
            SaleActivity.Cardtype.setText("Card Type: "+CardType);
            SaleActivity.Tag1.setText("");
            SaleActivity.img.setVisibility(View.INVISIBLE);

            mIsoDep.close();
        } catch (Exception e) {
            LogUtil.e(TAG, "ISO-DEP - Exception while trying to disable I/O operations to the tag");

            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        } finally {
            if (mIsoDep.isConnected()) {
                LogUtil.w(TAG, "ISO-DEP - Not disabled I/O operations to the tag");
            } else {
                LogUtil.d(TAG, "ISO-DEP - Disabled I/O operations to the tag");
            }
        }



        try {

            PreAuthActivity.AID1.setText("AID: "+ myAID);
            PreAuthActivity.CardNumber.setText("CardNumber: "+CardNumber1);
            PreAuthActivity.ExpiryDate.setText("Expiry Date: "+ExpiryDate1);
            PreAuthActivity.Cardtype.setText("Card Type: "+CardType);
            PreAuthActivity.Tag1.setText("");
            PreAuthActivity.img.setVisibility(View.INVISIBLE);

            mIsoDep.close();
        } catch (Exception e) {
            LogUtil.e(TAG, "ISO-DEP - Exception while trying to disable I/O operations to the tag");

            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        } finally {
            if (mIsoDep.isConnected()) {
                LogUtil.w(TAG, "ISO-DEP - Not disabled I/O operations to the tag");
            } else {
                LogUtil.d(TAG, "ISO-DEP - Disabled I/O operations to the tag");
            }
        }


        try {

            SaleWithCashTransaction.AID1.setText("AID: "+ myAID);
            SaleWithCashTransaction.CardNumber.setText("CardNumber: "+CardNumber1);
            SaleWithCashTransaction.ExpiryDate.setText("Expiry Date: "+ExpiryDate1);
            SaleWithCashTransaction.Cardtype.setText("Card Type: "+CardType);
            SaleWithCashTransaction.Tag1.setText("");
            SaleWithCashTransaction.img.setVisibility(View.INVISIBLE);

            mIsoDep.close();
        } catch (Exception e) {
            LogUtil.e(TAG, "ISO-DEP - Exception while trying to disable I/O operations to the tag");

            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        } finally {
            if (mIsoDep.isConnected()) {
                LogUtil.w(TAG, "ISO-DEP - Not disabled I/O operations to the tag");
            } else {
                LogUtil.d(TAG, "ISO-DEP - Disabled I/O operations to the tag");
            }
        }


        // - Try to disable I/O operations to the tag
    }




    public void getAmount(String s) {
        Log.d("Amt",s);
    }
}
