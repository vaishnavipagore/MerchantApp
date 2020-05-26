package emv;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import utils.Translate;

public final class Tlv
{

    private Hashtable<String, Vector<String>> values_ht;

    /**
     *
     */
    public Tlv()
    {
        values_ht = new Hashtable<>();
    }

    /**
     *
     * @param data_str
     */
    public Tlv(String data_str)
    {
        values_ht = new Hashtable<>();
        fromMsg(Translate.getData(data_str));
    }

    /**
     *
     * @param tag
     * @param value
     */
    public void putField(String tag, String value)
    {
        if (value != null)
        {
            Vector<String> vals = values_ht.get(tag);
            if (vals == null)
            {
                vals = new Vector<String>();
                values_ht.put(tag, vals);
            }
            vals.addElement(value);
        }
    }

    /**
     *
     * @param tag
     */
    public void removeField(String tag)
    {
        if (tag != null)
        {
            if (values_ht.containsKey(tag))
            {
                values_ht.remove(tag);
            }
        }
    }

    /**
     *
     * @param tag
     * @param values
     */
    public void putFields(String tag, Vector<String> values)
    {
        if (values != null && !values.isEmpty())
        {
            Vector<String> our_vals = values_ht.get(tag);
            if (our_vals == null)
            {
                our_vals = new Vector<String>();
                values_ht.put(tag, our_vals);
            }
            for (Enumeration<String> en = values.elements(); en.hasMoreElements(); our_vals
                    .addElement((String) en.nextElement()))
				;
        }
    }

    /**
     *
     * @param tag
     * @return
     */
    public String getField(String tag)
    {
        Vector<String> vals = values_ht.get(tag);
        if (vals != null)
        {
            return (String) vals.elementAt(0);
        } else
        {
            return null;
        }
    }

    /**
     *
     * @param tag
     * @return
     */
    public Vector<String> getFields(String tag)
    {
        Vector<String> our_vals = values_ht.get(tag);
        if (our_vals == null)
        {
            return null;
        }
        Vector<String> return_vals = new Vector<String>();
        for (Enumeration<String> en = our_vals.elements(); en.hasMoreElements(); return_vals
                .addElement((String) en.nextElement()))
			;

        return return_vals;
    }

    /**
     *
     * @param tag
     * @return
     */
    public Tlv getTlv(String tag)
    {
        Vector<String> vals = values_ht.get(tag);
        if (vals != null)
        {
            return new Tlv((String) vals.elementAt(0));
        } else
        {
            return null;
        }
    }

    /**
     *
     * @param tag
     * @param tlv
     */
    public void putTlv(String tag, Tlv tlv)
    {
        if (tlv != null)
        {
            byte tlv_data[] = tlv.toMsg();
            if (tlv_data != null)
            {
                putField(tag, Translate.getString(tlv_data));
            }
        }
    }

    /**
     *
     * @return
     */
    public byte[] toMsg()
    {
        String tlv_string = new String();
        for (Enumeration<String> keySet = values_ht.keys(); keySet.hasMoreElements();)
        {
            String tag = (String) keySet.nextElement();
            Vector<String> value = values_ht.get(tag);
            Enumeration<String> v = value.elements();
            while (v.hasMoreElements())
            {
                String va  = (String) v.nextElement();
                int length = va.length();
                if (length > 0)
                {
                    tlv_string = (new StringBuilder()).append(tlv_string).append(tag).append(getLengthString(length))
                            .append(va).toString();
                }
            }
        }

        if (tlv_string != null && tlv_string.length() > 0)
        {
            return Translate.getData(tlv_string);
        } else
        {
            return null;
        }
    }

    /**
     *
     * @param data
     * @return
     */
    public int fromMsg(byte data[])
    {
        values_ht.clear();
        int pos = 0;
        String tag = null;
        int length = 0;
        String value = null;
        while (pos < data.length)
        {
            tag = Tag.getDataFromData(data, pos);
            if (tag == null)
            {
                return -1;
            }
            pos += tag.length();
            if (pos + 1 > data.length)
            {
                return -1;
            }
            length = data[pos++];
            if (length < 0)
            {
                int len_length = length & 0xf;
                if (pos + len_length > data.length)
                {
                    return -1;
                }
                byte length_bytes[] = Translate.getData(data, pos, len_length);
                length = 0;
                for (int i = 0; i < len_length;)
                {
                    length = length << 8 | length_bytes[i] & 0xff;
                    i++;
                    pos++;
                }

            }
            if (pos + length > data.length)
            {
                return -1;
            }
            value = Translate.getString(Translate.getData(data, pos, length));
            pos += length;
            putField(tag, value);
        }
        return pos;
    }

    /**
     *
     * @return
     */
    public Enumeration<String> tags()
    {
        return values_ht.keys();
    }

    /**
     *
     * @return
     */
    public String toString()
    {
        byte msg[] = toMsg();
        if (msg != null)
        {
            return Translate.fromBinToHex(Translate.getString(msg));
        } else
        {
            return "";
        }
    }

    /**
     *
     * @return
     */
    public String toFormattedString()
    {
        String tlv_string = new String();
        for (Enumeration<String> en = values_ht.keys(); en.hasMoreElements();)
        {
            String tag = (String) en.nextElement();
            Vector<?> vals = (Vector<?>) values_ht.get(tag);
            Enumeration<?> val_enum = vals.elements();
            while (val_enum.hasMoreElements())
            {
                String value = (String) val_enum.nextElement();
                int length = value.length();
                tlv_string = (new StringBuilder()).append(tlv_string).append("[").append(Translate.fromBinToHex(tag))
                        .append("\t").append(Translate.fromBinToHex(getLengthString(length))).append("]").append("\t")
                        .append(Translate.fromBinToHex(value)).append("\n").toString();
            }
        }

        return tlv_string;
    }

    /**
     *
     * @param length
     * @return
     */
    private String getLengthString(int length)
    {
        String length_string;
        if (length > 127)
        {
            if (length > 255)
            {
                length_string = (new StringBuilder()).append("82")
                        .append(Translate.resize(Integer.toHexString(length), 4, '0', false).substring(2)).toString();
            } else
            {
                length_string = (new StringBuilder()).append("81")
                        .append(Translate.resize(Integer.toHexString(length), 4, '0', false).substring(2)).toString();
            }
            length_string = Translate.fromHexToBin(length_string);
        } else
        {
            length_string = (new StringBuilder()).append("").append((char) length).toString();
        }
        return length_string;
    }
}
