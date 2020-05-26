package messagebus;

import java.util.HashMap;
import java.util.Iterator;

import utils.Utility;

/**
 *
 * @author Abhishek
 */
public final class KeyValuePair
{

    HashMap<String, String> input;

    /**
     *
     */
    public KeyValuePair()
    {
        input = new HashMap<>();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void put(String key, String value)
    {
        input.put(key, value);
    }

    /**
     *
     * @return
     */
    public String formMsg()
    {
        Iterator<String> itr = input.keySet().iterator();
        String key;
        String value;
        StringBuilder sb = new StringBuilder("");

        while (itr.hasNext())
        {
            key = itr.next();
            value = input.get(key);

            sb.append(Utility.resize(Integer.toString(key.length()), 3, "0", false));
            sb.append(key);
            sb.append(Utility.resize(Integer.toString(value.length()), 5, "0", false));
            sb.append(value);
        }

        return sb.toString();
    }

    /**
     *
     * @param data
     * @return
     */
    public HashMap<String, String> formHM(String data)
    {
        HashMap<String, String> hm_output = new HashMap<>();

        int key_len;
        int value_len;
        String key;
        String value;

        int startpos = 0;
        int len;

        try
        {
            while (startpos < data.length())
            {
                len = 3;

                key_len = Integer.parseInt(data.substring(startpos, startpos + len));
                startpos = startpos + len;
                len = key_len;
                key = data.substring(startpos, startpos + len);
                startpos = startpos + len;
                len = 5;
                value_len = Integer.parseInt(
                        data.substring(startpos, startpos + len));
                startpos = startpos + len;
                len = value_len;
                value = data.substring(startpos, startpos + len);
                startpos = startpos + len;

                hm_output.put(key, value);
            }
        } catch (Exception ex)
        {
            Utility.debugPrint(Utility.getFormattedDateTime(), "Error Parsing Data : "
                    + data + "\n" + ex.getMessage());

            return new HashMap<>();
        }

        return hm_output;
    }

    /**
     *
     * @return
     */
    public HashMap<String, String> getKVPHM()
    {
        return input;
    }
    
    /**
     * 
     * @param key
     * @return 
     */
    public boolean containsKey(String key)
    {
        return input.containsKey(key);
    }
    
    /**
     * 
     * @param key
     * @return 
     */
    public String remove(String key)
    {
        return input.remove(key);
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    public String replace(String key,String value)
    {
        return input.replace(key, value);
    }

}
