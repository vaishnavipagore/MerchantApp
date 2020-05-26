package messagebus;

/**
 *
 * @author Abhy
 */
public final class Track2
{

    String cardnumber;
    String expirydate;
    String service_rest_code;
    String track2;

    /**
     *
     */
    public Track2()
    {
    }

    /**
     *
     * @param track
     */
    public Track2(String track)
    {
        int pos = 0;

        this.track2 = track;

        if (track != null)
        {
            if (track.contains("D"))
            {
                pos = track.indexOf("D");
                cardnumber = track.substring(0, pos);
            } else if (track.contains("="))
            {
                pos = track.indexOf("=");
                cardnumber = track.substring(0, pos);
            }

            if (cardnumber.trim().length() > 0)
            {
                expirydate = track.substring(pos + 1, pos + 1 + 4);
                service_rest_code = track.substring(pos + 1 + 4,
                        pos + 1 + 4 + 3);
            }
        }
    }

    /**
     *
     * @return
     */
    public String getCardnumber()
    {
        return cardnumber;
    }

    /**
     *
     * @return
     */
    public String getExpirydate()
    {
        return expirydate;
    }

    /**
     *
     * @return
     */
    public String getService_rest_code()
    {
        return service_rest_code;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return track2;
    }

}
