package apdu;

import emv.AID;
import utils.Translate;
import utils.Utility;

/**
 *
 * @author Abhishek M
 */
public class GetAID
{

    private AID aid;
    private int size;

    /**
     *
     */
    public GetAID()
    {
        aid = new AID();

        size = aid.getAid_list().size();
    }

    /**
     *
     * @return
     */
    public int getAIDCount()
    {
        return size;
    }

    /**
     *
     * @param index
     * @return
     */
    public byte[] getAIDCmdBytes(int index)
    {
        String aid_value;
        String apdu_cmd;
        int aid_len;

        if (index > size || index < 0)
        {
            return null;
        }

        aid_value = aid.getAid_list().get(index);

        // get byte size
        aid_len = aid_value.length() / 2;

        // Pad Length (p1 & p2)
        aid_value = Utility.resize(Integer.toString(aid_len), 2, "0", false) + aid_value;

        // add CLA & INS
        apdu_cmd = Translate.fromHexToBin("00A404" + aid_value + "00")+"SW"+
                Translate.fromHexToBin("9000");

        return Translate.getData(apdu_cmd);
    }

    /**
     *
     * @param rsp
     * @return
     */
    public boolean validateAIDRspBytes(byte[] rsp)
    {
        String data;

        if (rsp == null)
        {
            return false;
        }

        data = Translate.fromBinToHex(Translate.getString(rsp));

        // Refer EMV Book3, page 44
        return data.startsWith("9000");
    }
}
