package emv;

import utils.Translate;

/**
 *
 * @author Abhishek Mukhopadhyay
 */
public final class Tag
{

    public static final String _4F_APPLICATION_IDENTIFIER = Translate.fromHexToBin("4F");
    public static final String _50_APPLICATION_LABEL = Translate.fromHexToBin("50");
    public static final String _56_TRACK1_EQUIVALENT_DATA = Translate.fromHexToBin("56");
    public static final String _57_TRACK2_EQUIVALENT_DATA = Translate.fromHexToBin("57");
    public static final String _5A_APPLICATION_PAN = Translate.fromHexToBin("5A");
    public static final String _5F20_CARDHOLDER_NAME = Translate.fromHexToBin("5F20");
    public static final String _5F24_APPLICATION_EXPIRY_DATE = Translate.fromHexToBin("5F24");
    public static final String _5F25_APPLICATION_EFFECTIVE_DATE = Translate.fromHexToBin("5F25");
    public static final String _5F2A_TRANSACTION_CURRENCY_CODE = Translate.fromHexToBin("5F2A");
    public static final String _5F28_ISSUER_COUNTRY_CODE = Translate.fromHexToBin("5F28");
    public static final String _5F2D_LANGUAGE_PREFERENCE = Translate.fromHexToBin("5F2D");
    public static final String _5F30_SERVICE_CODE = Translate.fromHexToBin("5F30");
    public static final String _5F34_APPLICATION_PAN_SEQ_NR = Translate.fromHexToBin("5F34");
    public static final String _5F50_ISSUER_URL = Translate.fromHexToBin("5F50");
    public static final String _6F_FCI_TEMPLATE = Translate.fromHexToBin("6F");
    public static final String _70_AEF_DATA_TEMPLATE = Translate.fromHexToBin("70");
    public static final String _71_ISSUER_SCRIPT_TEMPLATE_1 = Translate.fromHexToBin("71");
    public static final String _72_ISSUER_SCRIPT_TEMPLATE_2 = Translate.fromHexToBin("72");
    public static final String _77_RSP_MSG_TEMPLATE_FORMAT_2 = Translate.fromHexToBin("77");
    public static final String _80_RSP_MSG_TEMPLATE_FORMAT_1 = Translate.fromHexToBin("80");
    public static final String _82_APPLICATION_INTERCHANGE_PROFILE = Translate.fromHexToBin("82");
    public static final String _83_COMMAND_TEMPLATE = Translate.fromHexToBin("83");
    public static final String _84_DF_NAME = Translate.fromHexToBin("84");
    public static final String _87_APPLICATION_PRIORITY_INDICATOR = Translate.fromHexToBin("87");
    public static final String _89_AUTHORISATION_CODE = Translate.fromHexToBin("89");
    public static final String _8A_AUTHORIZATION_RESPONSE_CODE = Translate.fromHexToBin("8A");
    public static final String _8C_CDOL1 = Translate.fromHexToBin("8C");
    public static final String _8D_CDOL2 = Translate.fromHexToBin("8D");
    public static final String _8E_CVM_LIST = Translate.fromHexToBin("8E");
    public static final String _8F_CA_PUBLIC_KEY_INDEX = Translate.fromHexToBin("8F");
    public static final String _90_ISSUER_PUBLIC_KEY_CERTIFICATE = Translate.fromHexToBin("90");
    public static final String _91_ISSUER_AUTHENTICATION_DATA = Translate.fromHexToBin("91");
    public static final String _92_ISSUER_PUBLIC_KEY_REMAINDER = Translate.fromHexToBin("92");
    public static final String _93_SIGNED_STATIC_APPLICATION_DATA = Translate.fromHexToBin("93");
    public static final String _94_APPLICATION_FILE_LOCATOR = Translate.fromHexToBin("94");
    public static final String _95_TERMINAL_VERIFICATION_RESULTS = Translate.fromHexToBin("95");
    public static final String _99_ENCRYPTED_PIN_BLOCK = Translate.fromHexToBin("99");
    public static final String _9A_TRANSACTION_DATE = Translate.fromHexToBin("9A");
    public static final String _9B_TRANSACTION_STATUS_INFORMATION = Translate.fromHexToBin("9B");
    public static final String _9C_TRANSACTION_TYPE = Translate.fromHexToBin("9C");
    public static final String _9F02_AMOUNT_AUTHORIZED_NUMERIC = Translate.fromHexToBin("9F02");
    public static final String _9F03_AMOUNT_OTHER_NUMERIC = Translate.fromHexToBin("9F03");
    public static final String _9F06_APPLICATION_IDENTIFIER = Translate.fromHexToBin("9F06");
    public static final String _9F07_APPLICATION_USAGE_CONTROL = Translate.fromHexToBin("9F07");
    public static final String _9F08_APPLICATION_VERSION_NR_ICC = Translate.fromHexToBin("9F08");
    public static final String _9F09_TERM_APPLICATION_VERSION_NR = Translate.fromHexToBin("9F09");
    public static final String _9F0D_ISSUER_ACTION_CODE_DEFAULT = Translate.fromHexToBin("9F0D");
    public static final String _9F0E_ISSUER_ACTION_CODE_DENIAL = Translate.fromHexToBin("9F0E");
    public static final String _9F0F_ISSUER_ACTION_CODE_ONLINE = Translate.fromHexToBin("9F0F");
    public static final String _9F10_ISSUER_APPLICATION_DATA = Translate.fromHexToBin("9F10");
    public static final String _9F12_APPLICATION_PREFERRED_NAME = Translate.fromHexToBin("9F12");
    public static final String _9F13_LAST_ONLINE_ATC_REGISTER = Translate.fromHexToBin("9F13");
    public static final String _9F14_LOWER_CONSEC_OFFLINE_LIMIT = Translate.fromHexToBin("9F14");
    public static final String _9F15_MERCHANT_CATEGORY_CODE = Translate.fromHexToBin("9F15");
    public static final String _9F17_PIN_TRY_COUNTER = Translate.fromHexToBin("9F17");
    public static final String _9F18_ISSUER_SCRIPT_IDENTIFIER = Translate.fromHexToBin("9F18");
    public static final String _9F1A_TERMINAL_COUNTRY_CODE = Translate.fromHexToBin("9F1A");
    public static final String _9F1B_TERMINAL_FLOOR_LIMIT = Translate.fromHexToBin("9F1B");
    public static final String _9F1C_TERMINAL_IDENTIFICATION = Translate.fromHexToBin("9F1C");
    public static final String _9F1E_IFD_SERIAL_NUMBER = Translate.fromHexToBin("9F1E");
    public static final String _9F1F_TRACK1_DISC_DATA = Translate.fromHexToBin("9F1F");
    public static final String _9F20_TRACK_2_DISCRETIONARY_DATA = Translate.fromHexToBin("9F20");
    public static final String _9F21_TRANSACTION_TIME = Translate.fromHexToBin("9F21");
    public static final String _9F23_UPPER_CONSEC_OFFLINE_LIMIT = Translate.fromHexToBin("9F23");
    public static final String _9F26_APPLICATION_CRYPTOGRAM = Translate.fromHexToBin("9F26");
    public static final String _9F27_CRYPTOGRAM_INFORMATION_DATA = Translate.fromHexToBin("9F27");
    public static final String _9F2D_ICC_PIN_ENCR_PUB_KEY_CERTIFICATE = Translate.fromHexToBin("9F2D");
    public static final String _9F2E_ICC_PIN_ENCR_PUB_KEY_EXPONENT = Translate.fromHexToBin("9F2E");
    public static final String _9F2F_ICC_PIN_ENCR_PUB_KEY_REMAINDER = Translate.fromHexToBin("9F2F");
    public static final String _9F32_ISSUER_PUBLIC_KEY_EXPONENT = Translate.fromHexToBin("9F32");
    public static final String _9F33_TERMINAL_CAPABILITIES = Translate.fromHexToBin("9F33");
    public static final String _9F34_CVM_RESULTS = Translate.fromHexToBin("9F34");
    public static final String _9F35_TERMINAL_TYPE = Translate.fromHexToBin("9F35");
    public static final String _9F36_APPLICATION_TRANSACTION_COUNTER = Translate.fromHexToBin("9F36");
    public static final String _9F37_UNPREDICTABLE_NUMBER = Translate.fromHexToBin("9F37");
    public static final String _9F38_PDOL = Translate.fromHexToBin("9F38");
    public static final String _9F39_POS_ENTRY_MODE = Translate.fromHexToBin("9F39");
    public static final String _9F40_ADD_TERMINAL_CAPABILITIES = Translate.fromHexToBin("9F40");
    public static final String _9F41_TRANSACTION_SEQUENCE_COUNTER = Translate.fromHexToBin("9F41");
    public static final String _9F42_APPLICATION_CURRENCY_CODE = Translate.fromHexToBin("9F42");
    public static final String _9F44_APPLICATION_CURRENCY_EXPONENT = Translate.fromHexToBin("9F44");
    public static final String _9F45_DATA_AUTHENTICATION_CODE = Translate.fromHexToBin("9F45");
    public static final String _9F46_ICC_PUBLIC_KEY_CERTIFICATE = Translate.fromHexToBin("9F46");
    public static final String _9F47_ICC_PUBLIC_KEY_EXPONENT = Translate.fromHexToBin("9F47");
    public static final String _9F48_ICC_PUBLIC_KEY_REMAINDER = Translate.fromHexToBin("9F48");
    public static final String _9F4E_MERCHANT_NAME_LOCATION = Translate.fromHexToBin("9F4E");
    public static final String _9F53_TRANSACTION_CATEGORY_CODE = Translate.fromHexToBin("9F53");
    public static final String _9F5B_ISSUER_SCRIPT_RESULT = Translate.fromHexToBin("9F5B");
    public static final String _A5_FCI_PROPRIETARY_TEMPLATE = Translate.fromHexToBin("A5");
    public static final String _CB_UPPER_CUMULATIVE_OFFLINE_TRAN_AMT = Translate.fromHexToBin("CB");
    public static final String _9F6B_TRACK2_EQUIVALENT = Translate.fromHexToBin("9F6B");
    public static final String _9F6E_FORM_FACTOR_INDICATOR = Translate.fromHexToBin("9F6E");
    public static final String _9F7C_CUSTOMER_EXCLUSIVE_DATA = Translate.fromHexToBin("9F7C");
    //
    public static final String _DF3A_ADDITIONAL_TERMINAL_CAP = Translate.fromHexToBin("DF3A");
    public static final String _DF16_SERVICE_ID = Translate.fromHexToBin("DF16");

    protected Tag()
    {
    }

    /**
     *
     * @param tagged_data
     * @param pos
     * @return
     */
    public static String getDataFromData(byte[] tagged_data, int pos)
    {
        if (tagged_data == null || pos > (tagged_data.length - 1))
        {
            return null;
        }
        int length = 1;
        if (!singleByte(tagged_data[pos]))
        {
            for (; tagged_data.length > pos + length && !lastByte(tagged_data[pos + length]); length++);
            length++;
        }
        if (tagged_data.length < pos + length)
        {
            return null;
        } else
        {
            return Translate.getString(Translate.getData(tagged_data, pos, length));
        }
    }

    /**
     *
     * @param tag_1st_byte
     * @return
     */
    protected static boolean singleByte(byte tag_1st_byte)
    {
        return (tag_1st_byte & 0x1f) != 31;
    }

    /**
     *
     * @param tag_byte
     * @return
     */
    protected static boolean lastByte(byte tag_byte)
    {
        return (tag_byte & 0x80) != 128;
    }
}
