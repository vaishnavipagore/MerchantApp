package emv;

import java.util.ArrayList;

/**
 *
 * @author abhishekmukhopadhyay
 */
public final class AID
{

    public static final String _VISA_CREDIT_DEBIT = "A0000000031010";
    public static final String _VISA_ELECTRON = "A0000000032010";
    public static final String _VISA_V_PAY = "A0000000032020";
    public static final String _VISA_PLUS = "A0000000038010";

    public static final String _MASTERCARD_CREDIT_DEBIT = "A0000000041010";
    public static final String _MASTERCARD = "A0000000049999";
    public static final String _MAESTRO = "A0000000043060";
    public static final String _CIRRUS = "A0000000046000";

    public static final String _MAESTRO_UK_SWITCH = "A0000000050001";
    
    private ArrayList<String> aid_list;

    public AID()
    {
        setAid_list();
    }

    public ArrayList<String> getAid_list()
    {
        return aid_list;
    }

    private void setAid_list()
    {
        aid_list = new ArrayList<>();
        
        aid_list.add(_VISA_CREDIT_DEBIT);
        aid_list.add(_VISA_ELECTRON);
        aid_list.add(_VISA_V_PAY);
        aid_list.add(_VISA_PLUS);
        
        aid_list.add(_MASTERCARD_CREDIT_DEBIT);
        aid_list.add(_MASTERCARD);
        aid_list.add(_MAESTRO);
        aid_list.add(_CIRRUS);
        aid_list.add(_MAESTRO_UK_SWITCH);
    }

}
