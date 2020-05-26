package messagebus;

import utils.Utility;

/**
 *
 * @author Abhy
 */
public final class ReplacementAmounts
{

    private String actual_tran_amount;
    private String actual_stl_amount;
    private String actual_tran_amount_fee;
    private String actual_stl_amount_fee;

    /**
     *
     */
    public ReplacementAmounts()
    {
        this.actual_tran_amount = "000000000000";
        this.actual_stl_amount = "000000000000";
        this.actual_tran_amount_fee = "D00000000";
        this.actual_stl_amount_fee = "D00000000";
    }

    /**
     *
     * @param actual_tran_amount
     * @param actual_stl_amount
     * @param actual_tran_amount_fee
     * @param actual_stl_amount_fee
     */
    public ReplacementAmounts(String actual_tran_amount,
            String actual_stl_amount, String actual_tran_amount_fee,
            String actual_stl_amount_fee)
    {
        this.actual_tran_amount = actual_tran_amount;
        this.actual_stl_amount = actual_stl_amount;
        this.actual_tran_amount_fee = actual_tran_amount_fee;
        this.actual_stl_amount_fee = actual_stl_amount_fee;
    }

    /**
     *
     * @param ra
     */
    public ReplacementAmounts(String ra)
    {
        if (ra.trim().length() == 42)
        {
            actual_tran_amount = ra.substring(0, 12);
            actual_stl_amount = ra.substring(12, 24);
            actual_tran_amount_fee = ra.substring(24, 33);
            actual_stl_amount_fee = ra.substring(33, 42);
        } else
        {
            this.actual_tran_amount = "000000000000";
            this.actual_stl_amount = "000000000000";
            this.actual_tran_amount_fee = "D00000000";
            this.actual_stl_amount_fee = "D00000000";
        }
    }

    /**
     *
     * @return
     */
    public String getReplacementAmt()
    {
        String ra;

        ra = Utility.resize(actual_tran_amount, 12, "0", false)
                + Utility.resize(actual_stl_amount, 12, "0", false) + Utility.
                resize(actual_tran_amount_fee, 8, "0", false) + Utility.resize(
                actual_stl_amount_fee, 8, "0", false);

        return ra;
    }

    /**
     *
     * @return
     */
    public String getActual_stl_amount()
    {
        return actual_stl_amount;
    }

    /**
     *
     * @param actual_stl_amount
     */
    public void setActual_stl_amount(String actual_stl_amount)
    {
        this.actual_stl_amount = actual_stl_amount;
    }

    /**
     *
     * @return
     */
    public String getActual_stl_amount_fee()
    {
        return actual_stl_amount_fee;
    }

    /**
     *
     * @param actual_stl_amount_fee
     */
    public void setActual_stl_amount_fee(String actual_stl_amount_fee)
    {
        this.actual_stl_amount_fee = actual_stl_amount_fee;
    }

    /**
     *
     * @return
     */
    public String getActual_tran_amount()
    {
        return actual_tran_amount;
    }

    /**
     *
     * @param actual_tran_amount
     */
    public void setActual_tran_amount(String actual_tran_amount)
    {
        this.actual_tran_amount = actual_tran_amount;
    }

    /**
     *
     * @return
     */
    public String getActual_tran_amount_fee()
    {
        return actual_tran_amount_fee;
    }

    /**
     *
     * @param actual_tran_amount_fee
     */
    public void setActual_tran_amount_fee(String actual_tran_amount_fee)
    {
        this.actual_tran_amount_fee = actual_tran_amount_fee;
    }

}
