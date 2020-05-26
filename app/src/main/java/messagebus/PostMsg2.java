package messagebus;

import java.util.concurrent.ConcurrentHashMap;
import utils.FormatData;
import utils.Utility;

/**
 *
 * @author abhishekmukhopadhyay
 */
public class PostMsg2
{

    ConcurrentHashMap<String, String> mswipe_trans_obj;

    public PostMsg2()
    {
        mswipe_trans_obj=new ConcurrentHashMap<>();
    }

    public static class Field
    {

        public static final String _MTI = "MTI";
        public static final String _SID = "SID";
        public static final String _KSN = "KSN";
        public static final String _SWIPER_TYPE = "SwiperType";
        public static final String _PAD = "PAD";
        public static final String _IMEI = "IMEI";
        public static final String _MOBILE = "MOBILE";
        public static final String _4DBC = "4DBC";
        public static final String _CARDTYPE = "CARDTYPE";
        public static final int _002_PAN = 2;
        public static final int _003_PROCESSING_CODE = 3;
        public static final int _004_AMOUNT_TRANSACTION = 4;
        public static final int _005_AMOUNT_SETTLE = 5;
        public static final int _006_AMOUNT_CARDHOLDER_BILL = 6;
        public static final int _007_TRANSMISSION_DATE_TIME = 7;
        public static final int _008_AMOUNT_CARDHOLDER_BILL_FEE = 8;
        public static final int _009_CONV_RATE_SETTLE = 9;
        public static final int _010_CONV_RATE_CARDHOLDER_BILL = 10;
        public static final int _011_SYSTEMS_TRACE_AUDIT_NR = 11;
        public static final int _012_TIME_LOCAL = 12;
        public static final int _013_DATE_LOCAL = 13;
        public static final int _014_DATE_EXPIRATION = 14;
        public static final int _015_DATE_SETTLE = 15;
        public static final int _016_DATE_CONV = 16;
        public static final int _017_DATE_CAPTURE = 17;
        public static final int _018_MERCHANT_TYPE = 18;
        public static final int _019_ACQUIRING_INST_COUNTRY_CODE = 19;
        public static final int _020_PAN_EXTENDED_COUNTRY_CODE = 20;
        public static final int _021_FORWARDING_INST_COUNTRY_CODE = 21;
        public static final int _022_POS_ENTRY_MODE = 22;
        public static final int _023_CARD_SEQ_NR = 23;
        public static final int _024_NETWORK_INTL_ID = 24;
        public static final int _025_POS_CONDITION_CODE = 25;
        public static final int _026_POS_PIN_CAPTURE_CODE = 26;
        public static final int _027_AUTH_ID_RSP_LEN = 27;
        public static final int _028_AMOUNT_TRAN_FEE = 28;
        public static final int _029_AMOUNT_SETTLE_FEE = 29;
        public static final int _030_AMOUNT_TRAN_PROC_FEE = 30;
        public static final int _031_AMOUNT_SETTLE_PROC_FEE = 31;
        public static final int _032_ACQUIRING_INST_ID_CODE = 32;
        public static final int _033_FORWARDING_INST_ID_CODE = 33;
        public static final int _034_PAN_EXTENDED = 34;
        public static final int _035_TRACK_2_DATA = 35;
        public static final int _036_TRACK_3_DATA = 36;
        public static final int _037_RETRIEVAL_REF_NR = 37;
        public static final int _038_AUTH_ID_RSP = 38;
        public static final int _039_RSP_CODE = 39;
        public static final int _040_SERVICE_RESTRICTION_CODE = 40;
        public static final int _041_CARD_ACCEPTOR_TERM_ID = 41;
        public static final int _042_CARD_ACCEPTOR_ID_CODE = 42;
        public static final int _043_CARD_ACCEPTOR_NAME_LOC = 43;
        public static final int _044_ADDITIONAL_RSP_DATA = 44;
        public static final int _045_TRACK_1_DATA = 45;
        public static final int _046_ADDITIONAL_DATA_ISO = 46;
        public static final int _047_ADDITIONAL_DATA_NATIONAL = 47;
        public static final int _048_ADDITIONAL_DATA = 48;
        public static final int _049_CURRENCY_CODE_TRAN = 49;
        public static final int _050_CURRENCY_CODE_SETTLE = 50;
        public static final int _051_CURRENCY_CODE_BILL = 51;
        public static final int _052_PIN_DATA = 52;
        public static final int _053_SECURITY_INFO = 53;
        public static final int _054_ADDITIONAL_AMOUNTS = 54;
        public static final int _055_EMV_DATA = 55;
        public static final int _059_TRAN_NR = 59;
        public static final int _060_ADVICE_REASON_CODE = 60;
        public static final int _061_POS_DATA = 61;
        public static final int _062_TRANS_ID = 62;
        public static final int _063_PRIVATE_USE = 63;
        public static final int _064_MAC_NORMAL = 64;
        public static final int _066_SETTLEMENT_CODE = 66;
        public static final int _067_EXTENDED_PAYMENT_CODE = 67;
        public static final int _068_RECEIVING_INST_COUNTRY_CODE = 68;
        public static final int _069_SETTLE_INST_COUNTRY_CODE = 69;
        public static final int _070_NETWORK_MNG_INFO_CODE = 70;
        public static final int _071_MSG_NR = 71;
        public static final int _072_MSG_NR_LAST = 72;
        public static final int _073_DATE_ACTION = 73;
        public static final int _074_NR_CR = 74;
        public static final int _075_NR_CR_REV = 75;
        public static final int _076_NR_DT = 76;
        public static final int _077_NR_DT_REV = 77;
        public static final int _078_NR_TRANSFER = 78;
        public static final int _079_NR_TRANSFER_REV = 79;
        public static final int _080_NR_INQUIRIES = 80;
        public static final int _081_NR_AUTH = 81;
        public static final int _082_AMOUNT_CR_PROC_FEE = 82;
        public static final int _083_AMOUNT_CR_TRAN_FEE = 83;
        public static final int _084_AMOUNT_DT_PROC_FEE = 84;
        public static final int _085_AMOUNT_DT_TRAN_FEE = 85;
        public static final int _086_AMOUNT_CR = 86;
        public static final int _087_AMOUNT_CR_REV = 87;
        public static final int _088_AMOUNT_DT = 88;
        public static final int _089_AMOUNT_DT_REV = 89;
        public static final int _090_ORIGINAL_DATA_ELEMENTS = 90;
        public static final int _091_FILE_UPDATE_CODE = 91;
        public static final int _092_FILE_SECURITY_CODE = 92;
        public static final int _093_RSP_IND = 93;
        public static final int _094_SERVICE_IND = 94;
        public static final int _095_REPLACEMENT_AMOUNTS = 95;
        public static final int _096_MSG_SECURITY_CODE = 96;
        public static final int _097_AMOUNT_NET_SETTLE = 97;
        public static final int _098_PAYEE = 98;
        public static final int _099_SETTLE_INST_ID_CODE = 99;
        public static final int _100_RECEIVING_INST_ID_CODE = 100;
        public static final int _101_FILE_NAME = 101;
        public static final int _102_ACCOUNT_ID_1 = 102;
        public static final int _103_ACCOUNT_ID_2 = 103;
        public static final int _104_TRAN_DESCRIPTION = 104;
        public static final int _120_TRAN_DATA_REQ = 120;
        public static final int _121_TRAN_DATA_RSP = 121;
        public static final int _122_NODE_INFO = 122;
        public static final int _123_EXTENDED_FIELD = 123;
        public static final int _124_PREV_ACQ_NODE_KEY = 124;
        public static final int _128_MAC_EXTENDED = 128;
        public static final String _REPEAT = "_REPEAT";
        //
        // Field for Internet Payment Gateway
        //
        public static final int _111_3DS_ENROLLMENT_DATA = 111;
        public static final int _112_3DS_AUTHENTICATION_RETURN_CODE = 112;
        public static final int _113_XID = 113;
        public static final int _114_CAVV = 114;
        public static final int _115_UCAF = 115;
        public static final int _116_ECI_INDICATOR = 116;
        public static final int _117_ALGO = 117;
        public static final int _118_CVV2 = 118;
        //

    }

    /**
     *
     * @param msg_from_remote
     * @return
     */
    public boolean parseMsgFromRemote(String msg_from_remote)
    {
        String[] params;

        if (msg_from_remote == null)
        {
            return false;
        } else if (msg_from_remote.trim().length() == 0)
        {
            return false;
        }

        params = msg_from_remote.split("&");

        if (params.length < 2)
        {
            return false;
        }

        mswipe_trans_obj = parseAmpString(msg_from_remote);

        return true;
    }

    /**
     *
     * @param value
     * @return
     */
    public boolean setMsgType(String value)
    {
        String fieldName = Field._MTI;

        mswipe_trans_obj.remove(fieldName);

        mswipe_trans_obj.put(fieldName, value);

        return true;
    }

    /**
     *
     * @return
     */
    public String getMsgType()
    {
        String fieldName = Field._MTI;
        return (String) mswipe_trans_obj.get(fieldName);
    }

    /**
     *
     * @return
     */
    public String get4DBC()
    {
        String fieldName = Field._4DBC;
        return (String) mswipe_trans_obj.get(fieldName);
    }

    /**
     *
     * @return
     */
    public String getIMEI()
    {
        String fieldName = Field._IMEI;
        String value = (String) mswipe_trans_obj.get(fieldName);

        if (value == null)
        {
            value = "";
        }

        return value;
    }

    /**
     *
     * @return
     */
    public String getMobile()
    {
        String fieldName = Field._MOBILE;
        String value = (String) mswipe_trans_obj.get(fieldName);

        if (value == null)
        {
            value = "";
        }

        return value;
    }

    /**
     *
     * @return
     */
    public String getSwiperID()
    {
        String fieldName = Field._SID;
        String value = (String) mswipe_trans_obj.get(fieldName);

        if (value == null)
        {
            value = "";
        }

        return value;
    }

    /**
     *
     * @param value
     */
    public void setSwiperID(String value)
    {
        String fieldName = Field._SID;
        mswipe_trans_obj.put(fieldName, value);
    }

    /**
     *
     * @return
     */
    public String getSwiperType()
    {
        String fieldName = Field._SWIPER_TYPE;
        String value = (String) mswipe_trans_obj.get(fieldName);

        if (value == null)
        {
            value = "";
        }

        return value;
    }

    /**
     *
     * @param value
     */
    public void setSwiperType(String value)
    {
        String fieldName = Field._SWIPER_TYPE;
        mswipe_trans_obj.put(fieldName, value);
    }

    /**
     *
     * @return
     */
    public String getPad()
    {
        String fieldName = Field._PAD;
        String value = (String) mswipe_trans_obj.get(fieldName);

        if (value == null)
        {
            value = "";
        }

        return value;
    }

    /**
     *
     * @param value
     */
    public void setPad(String value)
    {
        String fieldName = Field._PAD;
        mswipe_trans_obj.put(fieldName, value);
    }

    /**
     *
     * @return
     */
    public String getCardType()
    {
        String fieldName = Field._CARDTYPE;
        String value = (String) mswipe_trans_obj.get(fieldName);

        if (value == null)
        {
            value = "";
        }

        return value;
    }

    /**
     *
     * @param value
     */
    public void setIMEI(String value)
    {
        String fieldName = Field._IMEI;
        mswipe_trans_obj.put(fieldName, value);
    }

    /**
     *
     * @param value
     */
    public void setMobile(String value)
    {
        String fieldName = Field._MOBILE;
        mswipe_trans_obj.put(fieldName, value);
    }

    /**
     *
     * @param value
     */
    public void setCardType(String value)
    {
        String fieldName = Field._CARDTYPE;
        mswipe_trans_obj.put(fieldName, value);
    }

    /**
     *
     * @param value
     */
    public void set4DBC(String value)
    {
        String fieldName = Field._4DBC;
        mswipe_trans_obj.put(fieldName, value);
    }

    /**
     *
     * @param fieldnum
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setField(int fieldnum, String value)
    {
        if (fieldnum < 1 || fieldnum > 128)
        {
            return false;
        }

        if (value != null && value.length() > 0)
        {
            removeField(fieldnum);

            String fieldName = "F"
                    + FormatData.padleft(Integer.toString(fieldnum), 3, '0');
            mswipe_trans_obj.put(fieldName, value);
        }

        return true;
    }

    /**
     *
     * @param fieldnum
     * @return
     */
    public String getField(int fieldnum)
    {
        if (fieldnum < 1 || fieldnum > 128)
        {
            return null;
        }

        String fieldName = "F" + FormatData.padleft(Integer.toString(fieldnum), 3, '0');
        String value = (String) mswipe_trans_obj.get(fieldName);

        if (value == null)
        {
            return "";
        }

        if (fieldnum == 43)
        {
            value = value.replaceAll("\\+", " ");
        }

        return value;
    }

    /**
     *
     * @param fieldnum
     */
    public void removeField(int fieldnum)
    {
        if (fieldnum < 1 || fieldnum > 128)
        {
            return;
        }

        String fieldName = "F"
                + FormatData.padleft(Integer.toString(fieldnum), 3, '0');
        mswipe_trans_obj.remove(fieldName);
    }

    /**
     *
     * @param fieldnum
     * @return
     */
    public boolean isFieldSet(int fieldnum)
    {
        String fieldValue = getField(fieldnum);

        return fieldValue != null && fieldValue.trim().length() > 0;
    }

    /**
     *
     */
    public void setRspMsgType()
    {
        if (getMsgType() != null)
        {
            setMsgType("0"
                    + Integer.toString(Integer.parseInt(getMsgType()) + 10));
        } else
        {
            setMsgType("0010");
        }
    }

    /**
     *
     * @return
     */
    public String getMsg()
    {
        String str;
        String value;

        str = "MTI=" + getMsgType() + "&";

        value = getSwiperID();
        if (value != null && value.trim().length() > 0)
        {
            str = str + Field._SID + "=" + value + "&";
        }

        value = getSwiperType();
        if (value != null && value.trim().length() > 0)
        {
            str = str + Field._SWIPER_TYPE + "=" + value + "&";
        }

        value = getPad();
        if (value != null && value.trim().length() > 0)
        {
            str = str + Field._PAD + "=" + value + "&";
        }

        value = getCardType();
        if (value != null && value.trim().length() > 0)
        {
            str = str + Field._CARDTYPE + "=" + value + "&";
        }

        value = getIMEI();
        if (value != null && value.trim().length() > 0)
        {
            str = str + Field._IMEI + "=" + value + "&";
        }

        value = getMobile();
        if (value != null && value.trim().length() > 0)
        {
            str = str + Field._MOBILE + "=" + value + "&";
        }

        // put all the fields
        for (int i = 2; i < 120; i++)
        {
            value = getField(i);

            if (value != null && value.trim().length() > 0)
            {
                str = str + "F"
                        + Utility.resize(Integer.toString(i), 3, "0", false)
                        + "=" + getField(i) + "&";
            }
        }

        if (str.endsWith("&"))
        {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    /**
     *
     * @return
     */
    public String dumpMsg()
    {
        String str;
        String value;

        str = "\n" + getMsgType() + ":\n";

        value = getSwiperID();
        if (value != null && value.trim().length() > 0)
        {
            str = str + "\t[" + Field._SID + "] = [" + FormatData.protectAll(value) + "]\n";
        }

        value = getSwiperType();
        if (value != null && value.trim().length() > 0)
        {
            str = str + "\t[" + Field._SWIPER_TYPE + "] = [" + value + "]\n";
        }

        value = getPad();
        if (value != null && value.trim().length() > 0)
        {
            str = str + "\t[" + Field._PAD + "] = ["
                    + FormatData.protectAll(value) + "]\n";
        }

        value = get4DBC();
        if (value != null && value.trim().length() > 0)
        {
            str = str + "\t[" + Field._4DBC + "] = ["
                    + FormatData.protectAll(value) + "]\n";
        }

        value = getCardType();
        if (value != null && value.trim().length() > 0)
        {
            str = str + "\t[" + Field._CARDTYPE + "] = [" + value + "]\n";
        }

        value = getIMEI();
        if (value != null && value.trim().length() > 0)
        {
            str = str + "\t[" + Field._IMEI + "] = [" + value + "]\n";
        }

        value = getMobile();
        if (value != null && value.trim().length() > 0)
        {
            str = str + "\t[" + Field._MOBILE + "] = [" + value + "]\n";
        }

        // print all the fields
        for (int i = 2; i < 129; i++)
        {
            value = getField(i);

            if (i == 2 || i == 35 || i == 45 || i == 52 || i == 53 || i == 55)
            {
                if (value != null && value.trim().length() > 0)
                {
                    str = str
                            + "\t[F"
                            + Utility.resize(Integer.toString(i), 3, "0",
                                    false) + "] = ["
                            + FormatData.protectAll(getField(i)) + "]\n";
                }
            } else
            {
                if (value != null && value.trim().length() > 0)
                {
                    str = str
                            + "\t[F"
                            + Utility.resize(Integer.toString(i), 3, "0",
                                    false) + "] = [" + getField(i) + "]\n";
                }
            }
        }

        return str;
    }

    /**
     *
     * @param inputdata
     * @return
     */
    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, String> parseAmpString(String inputdata)
    {
        int startpos = 0;
        int pos;
        String temp;
        String[] arr;
        ConcurrentHashMap<String, String> parsedData = new ConcurrentHashMap();
        String prev_key = "";

        while (startpos < inputdata.length())
        {
            pos = inputdata.indexOf("&", startpos);

            if (pos == -1)
            {
                pos = inputdata.length();
            }
            temp = inputdata.substring(startpos, pos);
            arr = temp.split("=");

            if (arr.length == 2)
            {
                parsedData.put(arr[0], arr[1]);
            } else if (arr.length == 1)
            {
                String value = (String) parsedData.get(prev_key) + "&" + arr[0];
                parsedData.put(prev_key, value);
            }
            startpos = pos + 1;
            prev_key = arr[0];
        }

        return parsedData;
    }
}
