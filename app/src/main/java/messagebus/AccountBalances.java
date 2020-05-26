package messagebus;

public final class AccountBalances
{

    AdditionalAmount[] additionalamount;
    int numofaccounts;

    /**
     *
     * @param numofaccounts
     */
    public AccountBalances(int numofaccounts)
    {
        this.numofaccounts = numofaccounts;

        additionalamount = new AdditionalAmount[numofaccounts];
    }

    /**
     *
     * @param value
     */
    public AccountBalances(String value)
    {
        this.numofaccounts = value.length() / 20;

        additionalamount = new AdditionalAmount[numofaccounts];

        int startpos = 0;
        for (int i = 0; i < numofaccounts; i++)
        {
            additionalamount[i] = new AdditionalAmount(value);

            startpos = startpos + 20;
        }
    }

    /**
     *
     * @param acntnr
     * @param aa
     */
    public void putAdditionalAmount(int acntnr, AdditionalAmount aa)
    {
        if (acntnr <= numofaccounts)
        {
            additionalamount[acntnr] = aa;
        }
    }

    /**
     *
     * @param acntnr
     * @return
     */
    public AdditionalAmount getAdditionalAmount(int acntnr)
    {
        if (acntnr <= numofaccounts)
        {
            return additionalamount[acntnr];
        }

        return null;
    }

    /**
     *
     * @return
     */
    public String getAdditionalAmtsString()
    {
        String value = "";

        for (int i = 0; i < numofaccounts; i++)
        {
            value = value + additionalamount[i].getAdditionalAmountStr();
        }

        return value;
    }

}
