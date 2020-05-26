package messagebus;

/**
 *
 * @author Abhy
 */
public final class OriginalDataElements
{

    String mti;
    String stan;
    String datetime;
    String acquiring_id;
    String fwd_id;

    String ode_data;

    public OriginalDataElements(String ode_data)
    {
        this.ode_data = ode_data;

        if (ode_data.length() == 42)
        {
            mti = ode_data.substring(0, 4);
            stan = ode_data.substring(4, 10);
            datetime = ode_data.substring(10, 20);
            acquiring_id = ode_data.substring(20, 31);
            fwd_id = ode_data.substring(31, 42);
        } else
        {
            mti = "0000";
            stan = "000000";
            acquiring_id = "000000000000";
            fwd_id = "000000000000";
        }
    }

    public String getMti()
    {
        return mti;
    }

    public void setMti(String mti)
    {
        this.mti = mti;
    }

    public String getStan()
    {
        return stan;
    }

    public void setStan(String stan)
    {
        this.stan = stan;
    }

    public String getAcquiring_id()
    {
        return acquiring_id;
    }

    public void setAcquiring_id(String acquiring_id)
    {
        this.acquiring_id = acquiring_id;
    }

    public String getFwd_id()
    {
        return fwd_id;
    }

    public void setFwd_id(String fwd_id)
    {
        this.fwd_id = fwd_id;
    }

    public String getDatetime()
    {
        return datetime;
    }

    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }
}
