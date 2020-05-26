package utils;

/**
 *
 * @author Abhy
 */
public final class XConfigError extends Exception
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8561052982462268034L;
	private String methodname;
    private String parameter;
    private String value;

    /**
     *
     * @param methodname
     * @param parameter
     * @param value
     */
    public XConfigError(String methodname, String parameter, String value)
    {
        this.methodname = methodname;
        this.parameter = parameter;
        this.value = value;
    }

    /**
     * 
     * @return 
     */
    public String getErrorMsg()
    {
        String errormsg = "Configuration Error in Method / Process - ["
                + methodname + "] - Parameter - [" + parameter
                + "] - Value received - [" + value + "]";

        return errormsg;
    }
}
