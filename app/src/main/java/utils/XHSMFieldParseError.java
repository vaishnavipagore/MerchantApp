package utils;

/**
 *
 * @author Abhy
 */
public final class XHSMFieldParseError extends Exception
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7519842582361739998L;
	String data;
    String field;

    /**
     *
     * @param data
     * @param field
     * @param message
     */
    public XHSMFieldParseError(String data, String field, String message)
    {
        super(message);
        this.data = data;
        this.field = field;
    }
    
    /**
     * 
     * @return 
     */
    public String getErrorMsg()
    {
        String err_msg = "Error while parsing Data - [" + data
                + "] - for Field - [" + field + "] - Message - [" + super.
                getMessage() + "]";

        return err_msg;
    }

    /**
     *
     * @return
     */
    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
