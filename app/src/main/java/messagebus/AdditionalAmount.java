package messagebus;

import utils.Utility;

/**
 *
 * @author Abhy
 */
public final class AdditionalAmount
{

    String accounttype;
    String amounttype;
    String amountcurrency;
    String amountsign;
    String amounts;

    /**
     *
     * @param accounttype
     * @param amounttype
     * @param amountcurrency
     * @param amountsign
     * @param amounts
     */
    public AdditionalAmount(String accounttype, String amounttype,
            String amountcurrency, String amountsign, String amounts)
    {
        setAccounttype(accounttype);
        setAmounttype(amounttype);
        setAmountcurrency(amountcurrency);
        setAmountsign(amountsign);
        setAmounts(amounts);
    }

    /**
     *
     * @param value
     */
    public AdditionalAmount(String value)
    {
        int startpos = 0;

        if (value.length() == 20)
        {
            setAccounttype(value.substring(startpos, 2));
            setAmounttype(value.substring(startpos + 2, 4));
            setAmountcurrency(value.substring(startpos + 4, 7));
            setAmountsign(value.substring(startpos + 7, 8));
            setAmounts(value.substring(startpos + 8, 20));
        }
    }

    /**
     *
     * @return
     */
    private String getAccounttype()
    {
        return accounttype;
    }

    /**
     *
     * @param accounttype
     */
    public void setAccounttype(String accounttype)
    {
        if (accounttype == null || accounttype.length() != 2)
        {
            accounttype = "  ";
        }

        this.accounttype = accounttype;
    }

    /**
     *
     * @return
     */
    public String getAmountcurrency()
    {
        return amountcurrency;
    }

    /**
     *
     * @param amountcurrency
     */
    public void setAmountcurrency(String amountcurrency)
    {
        if (amountcurrency == null || amountcurrency.length() != 3)
        {
            amountcurrency = "   ";
        }

        this.amountcurrency = amountcurrency;
    }

    /**
     *
     * @return
     */
    public String getAmounts()
    {
        return amounts;
    }

    /**
     *
     * @param amounts
     */
    public void setAmounts(String amounts)
    {
        if (amounts == null)
        {
            amounts = "0";
        }

        if (amounts.length() != 12)
        {
            amounts = Utility.resize(amounts, 12, "0", false);
        }

        this.amounts = amounts;
    }

    /**
     *
     * @return
     */
    public String getAmountsign()
    {
        return amountsign;
    }

    /**
     *
     * @param amountsign
     */
    public void setAmountsign(String amountsign)
    {
        if (amountsign == null || amountsign.length() != 1)
        {
            amountsign = " ";
        }

        this.amountsign = amountsign;
    }

    /**
     *
     * @return
     */
    public String getAmounttype()
    {
        return amounttype;
    }

    /**
     *
     * @param amounttype
     */
    public void setAmounttype(String amounttype)
    {
        if (amounttype == null || amounttype.length() != 2)
        {
            amounttype = "  ";
        }

        this.amounttype = amounttype;
    }

    /**
     *
     * @return
     */
    public String getAdditionalAmountStr()
    {
        String value;

        value = getAccounttype() + getAmounttype() + getAmountcurrency()
                + getAmountsign() + getAmounts();

        return value;
    }
}