package utils;




import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;


import com.example.merchantapp.ReadPaycardThread;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import helper.Config;
import helper.ReadPaycardConstsHelper;
import object.TlvObject;

public class GpoUtil {
    public byte[] TAMOUNT;
    private static final String TAG = GpoUtil.class.getName();
    Context context;
    Tag tag;
    private static final byte GPO_P1 = (byte) 0x00, GPO_P2 = (byte) 0x00;

    public static boolean isGpoCommand(@NonNull byte[] commandApdu) {
        return (commandApdu.length > 4
                && commandApdu[0] == ReadPaycardConstsHelper.GET_PROCESSING_OPTIONS[0]
                && commandApdu[1] == ReadPaycardConstsHelper.GET_PROCESSING_OPTIONS[1]
                && commandApdu[2] == GPO_P1
                && commandApdu[3] == GPO_P2
        );
    }

    @Nullable
    public byte[] cGpo(@NonNull byte[] pdolConstructed) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.write(ReadPaycardConstsHelper.GET_PROCESSING_OPTIONS); // Cla, Ins

                byteArrayOutputStream.write(new byte[]{
                        GPO_P1, // P1
                        GPO_P2, // P2
                        (byte) pdolConstructed.length // Lc
                });

                byteArrayOutputStream.write(pdolConstructed); // Data

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                byteArrayOutputStream.close();

                // Temporary result
                byte[] tempResult = byteArrayOutputStream.toByteArray();
                /// - Temporary result

                if (tempResult != null && isGpoCommand(tempResult)) {
                    result = tempResult;
                }
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                LogUtil.e(TAG, e.toString());

                e.printStackTrace();
            }
        }

        return result;
    }

    @Nullable
    public byte[] fillPdol(@Nullable byte[] pdol,String TAmount) {
        // Returning result



        byte[] result = null;
        // - Returning result

        int pdolLength = 0;

        ArrayList<TlvObject> tlvObjectArrayList = new ArrayList<>();

        if (pdol != null) {
            for (int i = 0; i < pdol.length; i++) {
                int goNext = i;

                byte[] tlvTag = {
                        pdol[goNext++]
                };

                if ((tlvTag[0] & 0x1F) == 0x1F) {
                    tlvTag = new byte[]{
                            tlvTag[0], pdol[goNext++]
                    };
                }

                TlvObject tlvObj = new TlvObject(tlvTag, pdol[goNext]);
                tlvObjectArrayList.add(tlvObj);

                i += tlvObj.getTlvTag().length;
            }

            for (TlvObject tlvObject : tlvObjectArrayList) {
                pdolLength += tlvObject.getTlvTagLength();
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x83,
                        (byte) pdolLength
                });

                if (pdol != null) {
                    for (TlvObject tlvObject : tlvObjectArrayList) {
                        byte[] generatePdolResult = new byte[tlvObject.getTlvTagLength()];

                        byte[] resultValue = null;

                        Date transactionDate = new Date();

                        // TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)
                        if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TTQ_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> TTQ (Terminal Transaction Qualifiers); " + "9F66" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            byte[] data = new byte[4];

                            data[0] |= 1 << 5; // Contactless EMV mode supported (bit index (in the example: "5") <= 7)

                            resultValue = Arrays.copyOf(data, data.length);
                        }
                        // - TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)

                        // Amount, Authorised (Numeric); 9F02; 6 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.AMOUNT_AUTHORISED_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Amount, Authorised (Numeric); " + "9F02" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");


                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            TAmount = Utility.resize(TAmount, 12, "0", false);

                            resultValue = HexUtil.hexadecimalToBytes(TAmount);
                            LogUtil.d("Result Value",resultValue.toString());
                            Config.tran_amount = TAmount;
                            Log.d("Amount in 9F02 is",Config.tran_amount);

                            Config.tlv.putField(emv.Tag._9F02_AMOUNT_AUTHORIZED_NUMERIC, Translate.fromHexToBin(TAmount));

//                            resultValue = new byte[]{
//                                    (byte) 0x00,
//                                    (byte) 0x00,
//                                    (byte) 0x00,
//                                    (byte) 0x00,
//                                    (byte) 0x12,
//                                    (byte) 0x00
//                            };
                            ReadPaycardThread readPaycardThread = new ReadPaycardThread(context,tag,resultValue.toString());
                            readPaycardThread.getAmount(resultValue.toString());
                        }
                        // - Amount, Authorised (Numeric); 9F02; 6 Byte(s)

                        // Amount, Other (Numeric); 9F03; 6 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.AMOUNT_OTHER_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Amount, Other (Numeric); " + "9F03" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._9F03_AMOUNT_OTHER_NUMERIC, Translate.fromHexToBin("000000000000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00
                            };
                        }



                        //Additional Terminal Capabilities


                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.ADDTIONAL_TERMINAL_CAPABILITIES_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Addtional Terminal Capabilities " + "9F40" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._9F40_ADD_TERMINAL_CAPABILITIES, Translate.fromHexToBin("600000003000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x60,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x30,
                                    (byte) 0x00
                            };
                        }


                        //Additional Terminal Capabilities Extension


                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.ADDTIONAL_TERMINAL_CAPABILITIES_EXTENSION_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Addtional Terminal Capabilities Extension" + "DF3A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._DF3A_ADDITIONAL_TERMINAL_CAP, Translate.fromHexToBin("000000000000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00
                            };
                        }


                        // Terminal Capabilities

                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TERMINAL_CAPABILITIES_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Terminal Capabilities " + "9F33" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._9F33_TERMINAL_CAPABILITIES, Translate.fromHexToBin("002000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x20,
                                    (byte) 0x00
                                    //(byte) 0x00,
                                    //(byte) 0x80,
                                    //(byte) 0x00
                            };
                        }


                        // Terminal Application Version

                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TERMINAL_APPLICATION_VERSION_NUMBER_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Terminal Application Version " + "9F33" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                             Config.tlv.putField(emv.Tag._9F09_TERM_APPLICATION_VERSION_NR, Translate.fromHexToBin("0000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00
                            };
                        }

                        // Merchant Category Code

                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.MERCHANT_CATEGORY_CODE_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Merchant Category Code " + "9F15" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                             Config.tlv.putField(emv.Tag._9F15_MERCHANT_CATEGORY_CODE, Translate.fromHexToBin("0000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00
                            };
                        }



                        // Service ID

                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.SERVICE_ID_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Service ID " + "DF16" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                             Config.tlv.putField(emv.Tag._DF16_SERVICE_ID, Translate.fromHexToBin("0000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00
                            };
                        }





                        // - Amount, Other (Numeric); 9F03; 6 Byte(s)

                        // Terminal Country Code; 9F1A; 2 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TERMINAL_COUNTRY_CODE_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Terminal Country Code; " + "9F1A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._9F1A_TERMINAL_COUNTRY_CODE, Translate.fromHexToBin("0356"));

                            resultValue = new byte[]{
                                    (byte) 0x03,
                                    (byte) 0x56
                            };

                            // https://en.wikipedia.org/wiki/ISO_3166-1

                            // Example: Bulgaria: 100 (Hexadecimal representation: 0100); Reference: https://en.wikipedia.org/wiki/ISO_3166-1
                        }
                        // - Terminal Country Code; 9F1A; 2 Byte(s)

                        // Transaction Currency Code; 5F2A, 2 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TRANSACTION_CURRENCY_CODE_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Transaction Currency Code; " + "5F2A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._5F2A_TRANSACTION_CURRENCY_CODE, Translate.fromHexToBin("0356"));

                            resultValue = new byte[]{
                                    (byte) 0x03,
                                    (byte) 0x56
                            };


                            // https://en.wikipedia.org/wiki/ISO_4217

                            // Example: Bulgaria (BGN; Bulgarian lev): 975 (Hexadecimal representation: 0975)
                        }
                        // - Transaction Currency Code; 5F2A, 2 Byte(s)

                        // TVR (Transaction Verification Results); 95; 5 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TVR_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> TVR (Transaction Verification Results); " + "95" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._95_TERMINAL_VERIFICATION_RESULTS, Translate.fromHexToBin("000000000000"));

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x80,
                                    (byte) 0x00
                            };
                        }
                        // - TVR (Transaction Verification Results); 95; 5 Byte(s)

                        // Transaction Date; 9A, 3 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TRANSACTION_DATE_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Transaction Date; " + "9A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            // "SimpleDateFormat" Reference: https://developer.android.com/reference/java/text/SimpleDateFormat.html
                            SimpleDateFormat simpleDateFormat = null;
                            try {
                                simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault()); // Format: Year, Month in year, Day in month
                            } catch (Exception e) {
                                LogUtil.e(TAG, e.getMessage());
                                LogUtil.e(TAG, e.toString());

                                e.printStackTrace();
                            }

                            if (simpleDateFormat != null) {
                                String dateFormat = null;
                                try {
                                    dateFormat = simpleDateFormat.format(transactionDate);
                                } catch (Exception e) {
                                    LogUtil.e(TAG, e.getMessage());
                                    LogUtil.e(TAG, e.toString());

                                    e.printStackTrace();
                                }

                                if (dateFormat != null) {
                                    Config.tlv.putField(emv.Tag._9A_TRANSACTION_DATE, Translate.fromHexToBin(dateFormat));

                                    resultValue = HexUtil.hexadecimalToBytes(dateFormat);
                                }
                            }
                        }
                        // - Transaction Date; 9A, 3 Byte(s)

                        // Transaction Type; 9C, 1 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TRANSACTION_TYPE_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Transaction Type; " + "9C" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");
                            Config.tlv.putField(emv.Tag._9C_TRANSACTION_TYPE, Translate.fromHexToBin("00"));

                            resultValue = new byte[]{
                                    (byte) 0x00
                            };
                        }
                        // - Transaction Type; 9C, 1 Byte(s)

                        // Transaction Time; 9F21; 3 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.TRANSACTION_TIME_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> Transaction Date; " + "9F21" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            // "SimpleDateFormat" Reference: https://developer.android.com/reference/java/text/SimpleDateFormat.html
                            SimpleDateFormat simpleDateFormat = null;
                            try {
                                simpleDateFormat = new SimpleDateFormat("HHmmss", Locale.getDefault()); // Format: Hour in day (0-23), Minute in hour, Second in minute
                            } catch (Exception e) {
                                LogUtil.e(TAG, e.getMessage());
                                LogUtil.e(TAG, e.toString());

                                e.printStackTrace();
                            }

                            if (simpleDateFormat != null) {
                                String dateFormat = null;
                                try {
                                    dateFormat = simpleDateFormat.format(transactionDate);
                                } catch (Exception e) {
                                    LogUtil.e(TAG, e.getMessage());
                                    LogUtil.e(TAG, e.toString());

                                    e.printStackTrace();
                                }

                                if (dateFormat != null) {
                                    Config.tlv.putField(emv.Tag._9F21_TRANSACTION_TIME, Translate.fromHexToBin(dateFormat));

                                    resultValue = HexUtil.hexadecimalToBytes(dateFormat);
                                }
                            }
                        }
                        // - Transaction Time; 9F21; 3 Byte(s)

                        // UN (Unpredictable Number); 9F37, 1 or 4 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), ReadPaycardConstsHelper.UN_TLV_TAG)) {
                            LogUtil.d(TAG, "Generate PDOL -> UN (Unpredictable Number); " + "9F37" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            // Generate random unpredictable number
                            SecureRandom unSecureRandom = null;
                            try {
                                unSecureRandom = new SecureRandom();
                            } catch (Exception e) {
                                LogUtil.e(TAG, e.getMessage());
                                LogUtil.e(TAG, e.toString());

                                e.printStackTrace();
                            }

                            if (unSecureRandom != null) {
                                try {
                                    unSecureRandom.nextBytes(generatePdolResult);
                                 //   LogUtil.d("Un",Translate.fromBinToHex(Translate.getString(generatePdolResult)));
                                    Config.tlv.putField(emv.Tag._9F37_UNPREDICTABLE_NUMBER, Translate.getString(generatePdolResult));
                                } catch (Exception e) {
                                    LogUtil.e(TAG, e.getMessage());
                                    LogUtil.e(TAG, e.toString());

                                    e.printStackTrace();
                                }
                            }
                            // - Generate random unpredictable number
                        }
                        // - UN (Unpredictable Number); 9F37, 1 or 4 Byte(s)

                        if (resultValue != null) {
                            try {
                                System.arraycopy(resultValue, 0, generatePdolResult, 0, Math.min(resultValue.length, generatePdolResult.length));
                            } catch (Exception e) {
                                LogUtil.e(TAG, e.getMessage());
                                LogUtil.e(TAG, e.toString());

                                e.printStackTrace();
                            }
                        }

                        byteArrayOutputStream.write(generatePdolResult); // Data
                    }
                }

                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                LogUtil.e(TAG, e.toString());

                e.printStackTrace();
            }
        }

        return result;
    }
}
