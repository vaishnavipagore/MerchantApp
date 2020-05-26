package messagebus;

/**
 *
 * @author Abhy
 */
public final class ProcessingCode
{

    String trantype;
    String fromaccount;
    String toaccount;

    /**
     *
     * @param trantype
     * @param fromaccount
     * @param toaccount
     */
    public ProcessingCode(String trantype, String fromaccount, String toaccount)
    {
        this.trantype = trantype;
        this.fromaccount = fromaccount;
        this.toaccount = toaccount;
    }

    /**
     *
     * @param pcode
     */
    public ProcessingCode(String pcode)
    {
        if (pcode.length() == 6)
        {
            this.trantype = pcode.substring(0, 2);
            this.fromaccount = pcode.substring(2, 4);
            this.toaccount = pcode.substring(4, 6);
        } else
        {
            this.trantype = "";
            this.fromaccount = "";
            this.toaccount = "";
        }
    }

    /**
     *
     * @return
     */
    public String getFromaccount()
    {
        return fromaccount;
    }

    /**
     *
     * @param fromaccount
     */
    public void setFromaccount(String fromaccount)
    {
        this.fromaccount = fromaccount;
    }

    /**
     *
     * @return
     */
    public String getToaccount()
    {
        return toaccount;
    }

    /**
     *
     * @param toaccount
     */
    public void setToaccount(String toaccount)
    {
        this.toaccount = toaccount;
    }

    /**
     *
     * @return
     */
    public String getTrantype()
    {
        return trantype;
    }

    /**
     *
     * @param trantype
     */
    public void setTrantype(String trantype)
    {
        this.trantype = trantype;
    }

    /**
     *
     * @return
     */
    public String getPCodeString()
    {
        return trantype.trim() + fromaccount.trim() + toaccount.trim();
    }
}
