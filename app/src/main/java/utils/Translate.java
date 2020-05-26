package utils;

import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used for data conversion from one format to another.
 *
 * @author Abhishek M
 * @version 1.0
 */
public final class Translate
{

    private static final short ebcdic_to_ascii[] = new short[256];
    private static final short ascii_to_ebcdic[] = new short[256];
    private static final HexNibbleConverter hex_nibble_conv = new HexNibbleConverter();
    private static final PexNibbleConverter pex_nibble_conv = new PexNibbleConverter();

    static
    {
        initializeASCIIToEBCDICMap();
        initializeEBCDICToASCIIMap();
    }

    /**
     *
     */
    public static class Representation
    {

        /**
         *
         * @return
         */
        @Override
        public String toString()
        {
            return (new StringBuilder()).append("representation.").append(name).
                    toString();
        }
        public static final Representation HEX = new Representation("hex");
        public static final Representation PEX = new Representation("pex");
        public static final Representation BINARY = new Representation("binary");
        private String name;

        private Representation(String name)
        {
            this.name = name;
        }
    }

    /**
     *
     */
    public static class Encoding
    {

        public String getName()
        {
            return name;
        }

        /**
         *
         * @return
         */
        @Override
        public String toString()
        {
            return (new StringBuilder()).append("encoding.").append(name).
                    toString();
        }
        public static final Encoding ASCII = new Encoding("ASCII");
        public static final Encoding ASCII_8_BIT = new Encoding("ISO8859_1");
        public static final Encoding EBCDIC = new Encoding("EBCDIC");
        private String name;

        private Encoding(String name)
        {
            this.name = name;
        }
    }

    /**
     *
     */
    public Translate()
    {
    }

    /**
     *
     * @param input
     * @return
     */
    public static String getString(byte input[])
    {
        return getString(input, Encoding.ASCII_8_BIT);
    }

    /**
     *
     * @param input
     * @param encoding
     * @return
     */
    public static String getString(byte input[], Encoding encoding)
    {
        try
        {
            if (encoding == Encoding.EBCDIC)
            {
                return getString(encode(input, Encoding.EBCDIC,
                        Encoding.ASCII_8_BIT), Encoding.ASCII_8_BIT);
            }
            if (encoding == Encoding.ASCII_8_BIT)
            {
                return new String(input, "ISO-8859-1");
            }
            if (encoding == Encoding.ASCII)
            {
                return new String(input, encoding.getName());
            }
            return new String(input, encoding.getName());
        } catch (UnsupportedEncodingException e)
        {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE,
                    "Encoding Error", e);
        }

        return null;
    }

    /**
     *
     * @param input
     * @return
     */
    public static byte[] getData(String input)
    {
        return getData(input, Encoding.ASCII_8_BIT);
    }

    /**
     *
     * @param input
     * @param encoding
     * @return
     */
    public static byte[] getData(String input, Encoding encoding)
    {
        try
        {
            if (encoding == Encoding.EBCDIC)
            {
                return encode(input.getBytes(Encoding.ASCII_8_BIT.getName()),
                        Encoding.ASCII_8_BIT, Encoding.EBCDIC);
            }
            if (encoding == Encoding.ASCII_8_BIT)
            {
                char input_chars[] = input.toCharArray();
                byte ascii_bytes[] = new byte[input_chars.length];
                for (int i = 0; i < input_chars.length; i++)
                {
                    ascii_bytes[i] = (byte) (input_chars[i] & 0xff);
                }

                return ascii_bytes;
            }
            return input.getBytes(encoding.getName());
        } catch (UnsupportedEncodingException e)
        {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE,
                    "Encoding Error", e);
        }

        return null;
    }

    /**
     *
     * @param input
     * @param in
     * @param out
     * @return
     */
    public static String represent(String input, Representation in,
            Representation out)
    {
        if (in == out)
        {
            return input;
        }
        if (out == Representation.BINARY)
        {
            CharToNibbleConverter c2n_converter = null;
            if (in == Representation.HEX)
            {
                c2n_converter = hex_nibble_conv;
            } else if (in == Representation.PEX)
            {
                c2n_converter = pex_nibble_conv;
            }
            if (c2n_converter != null)
            {
                char input_chars[] = input.toCharArray();
                byte data[] = new byte[input_chars.length / 2];
                int data_offset = 0;
                for (int i = 0; i < input_chars.length; i += 2)
                {
                    byte high = c2n_converter.convertCharToNibble(input_chars[i]);
                    byte low = c2n_converter.convertCharToNibble(input_chars[i
                            + 1]);
                    data[data_offset++] = (byte) (high << 4 | low);
                }

                return getString(data, Encoding.ASCII_8_BIT);
            }
        }
        if (in == Representation.BINARY)
        {
            NibbleToCharConverter n2c_convert = null;
            if (out == Representation.HEX)
            {
                n2c_convert = hex_nibble_conv;
            } else if (out == Representation.PEX)
            {
                n2c_convert = pex_nibble_conv;
            }
            if (n2c_convert != null)
            {
                char input_chars[] = input.toCharArray();
                StringBuilder sb = new StringBuilder(input_chars.length * 2);
                for (int i = 0; i < input_chars.length; i++)
                {
                    byte high = (byte) ((input_chars[i] & 0xff) >> 4);
                    byte low = (byte) (input_chars[i] & 0xff & 0xf);
                    sb.append(n2c_convert.convertNibbleToChar(high));
                    sb.append(n2c_convert.convertNibbleToChar(low));
                }

                return sb.toString();
            }
        }

        return null;
    }

    /**
     *
     * @param hex_data
     * @return
     */
    public static byte[] fromHexDataToBinData(byte hex_data[])
    {
        return getData(represent(getString(hex_data), Representation.HEX,
                Representation.BINARY));
    }

    /**
     *
     * @param bin_data
     * @return
     */
    public static byte[] fromBinDataToHexData(byte bin_data[])
    {
        return getData(represent(getString(bin_data), Representation.BINARY,
                Representation.HEX));
    }

    /**
     *
     * @param hex_str
     * @return
     */
    public static String fromHexToBin(String hex_str)
    {
        return represent(hex_str, Representation.HEX, Representation.BINARY);
    }

    /**
     *
     * @param bin_str
     * @return
     */
    public static String fromBinToHex(String bin_str)
    {
        return represent(bin_str, Representation.BINARY, Representation.HEX);
    }

    /**
     *
     * @param pex_data
     * @return
     */
    public static byte[] fromPexDataToBinData(byte pex_data[])
    {
        return getData(represent(getString(pex_data), Representation.PEX,
                Representation.BINARY));
    }

    /**
     *
     * @param bin_data
     * @return
     */
    public static byte[] fromBinDataToPexData(byte bin_data[])
    {
        return getData(represent(getString(bin_data), Representation.BINARY,
                Representation.PEX));
    }

    /**
     *
     * @param pex_str
     * @return
     */
    public static String fromPexToBin(String pex_str)
    {
        return represent(pex_str, Representation.PEX, Representation.BINARY);
    }

    /**
     *
     * @param bin_str
     * @return
     */
    public static String fromBinToPex(String bin_str)
    {
        return represent(bin_str, Representation.BINARY, Representation.PEX);
    }

    /**
     *
     * @param input
     * @param in
     * @param out
     * @return
     */
    public static byte[] encode(byte input[], Encoding in, Encoding out)
    {
        if (in == out)
        {
            return input;
        }
        short map[] = null;
        if (in == Encoding.ASCII_8_BIT && out == Encoding.EBCDIC)
        {
            map = ascii_to_ebcdic;
        } else if (in == Encoding.ASCII && out == Encoding.EBCDIC)
        {
            map = ascii_to_ebcdic;
        } else if (in == Encoding.EBCDIC && out == Encoding.ASCII_8_BIT)
        {
            map = ebcdic_to_ascii;
        } else if (in == Encoding.EBCDIC && out == Encoding.ASCII)
        {
            map = ebcdic_to_ascii;
        }
        if (map != null)
        {
            int len = input.length;
            byte ret[] = new byte[len];
            for (int i = 0; i < len; i++)
            {
                ret[i] = (byte) map[input[i] & 0xff];
            }

            return ret;
        } else
        {
            return null;
        }
    }

    /**
     *
     * @param ascii_data
     * @return
     */
    public static byte[] fromAsciiDataToEbcdicData(byte ascii_data[])
    {
        return encode(ascii_data, Encoding.ASCII, Encoding.EBCDIC);
    }

    /**
     *
     * @param ebcdic_data
     * @return
     */
    public static byte[] fromEbcdicDataToAsciiData(byte ebcdic_data[])
    {
        return encode(ebcdic_data, Encoding.EBCDIC, Encoding.ASCII);
    }

    /**
     *
     * @param ascii_str
     * @return
     */
    public static String fromAsciiToEbcdic(String ascii_str)
    {
        return getString(encode(getData(ascii_str), Encoding.ASCII,
                Encoding.EBCDIC));
    }

    /**
     *
     * @param ebcdic_str
     * @return
     */
    public static String fromEbcdicToAscii(String ebcdic_str)
    {
        return getString(encode(getData(ebcdic_str), Encoding.EBCDIC,
                Encoding.ASCII));
    }

    /**
     *
     * @param data
     * @return
     */
    public static String fromHexDataToBase32String(byte data[])
    {
        if (data.length == 1 && data[0] == 0)
        {
            return "0";
        }
        StringBuilder s = new StringBuilder();
        int bit_count = 0;
        int accum = 0;
        for (int i = data.length - 1; i >= 0; i--)
        {
            int nibble = data[i] & 0xf;
            accum |= nibble << bit_count;
            if ((bit_count += 4) == 20)
            {
                s.insert(0, Utility.resize(Integer.toString(accum, 32), 4, "0",
                        false));
                bit_count = 0;
                accum = 0;
            }
            nibble = data[i] >> 4 & 0xf;
            accum |= nibble << bit_count;
            if ((bit_count += 4) != 20)
            {
                continue;
            }
            if (i == 0 && accum > 0)
            {
                s.insert(0, Integer.toString(accum, 32));
            } else
            {
                s.insert(0, Utility.resize(Integer.toString(accum, 32), 4, "0",
                        false));
            }
            bit_count = 0;
            accum = 0;
        }

        if (accum != 0)
        {
            s.insert(0, Integer.toString(accum, 32));
        }
        return s.toString().toUpperCase();
    }

    /**
     *
     * @param base32
     * @return
     */
    public static byte[] fromBase32StringToHexData(String base32)
    {
        if (base32.length() == 1 && base32.equals("0"))
        {
            return (new byte[]
                    {
                        0
                    });
        }
        int len = base32.length();
        int result_len = (len * 5 + 3) / 4;
        if (result_len % 2 == 1)
        {
            result_len = (result_len + 1) / 2;
        } else
        {
            result_len /= 2;
        }
        byte result[] = new byte[result_len];
        int result_offset = result_len - 1;
        boolean flag = true;
        for (int i = len - 1; i >= 0; i -= 4)
        {
            String s;
            int n;
            if (i - 3 >= 0)
            {
                s = base32.substring(i - 3, i + 1);
                n = Integer.parseInt(s, 32);
                if (flag)
                {
                    result[result_offset] = (byte) (n & 0xff);
                    result[result_offset - 1] = (byte) (n >>> 8 & 0xff);
                    result[result_offset - 2] = (byte) (n >>> 16 & 0xf);
                    result_offset -= 2;
                } else
                {
                    result[result_offset] |= (byte) ((n & 0xf) << 4);
                    result[result_offset - 1] = (byte) (n >>> 4 & 0xff);
                    result[result_offset - 2] = (byte) (n >>> 12 & 0xff);
                    result_offset -= 3;
                }
                flag = !flag;
                continue;
            }
            s = base32.substring(0, i + 1);
            n = Integer.parseInt(s, 32);
            if (!flag)
            {
                result[result_offset] |= (byte) ((n & 0xf) << 4);
                n >>>= 4;
                result_offset--;
            }
            for (; result_offset >= 0; result_offset--)
            {
                result[result_offset] = (byte) (n & 0xff);
                n >>>= 8;
            }

        }

        return result;
    }

    /**
     *
     * @param data
     * @param length
     * @param pad_byte
     * @param left_justify
     * @return
     */
    public static byte[] resizeData(byte data[], int length, byte pad_byte,
            boolean left_justify)
    {
        byte pad_data[] = new byte[length];
        int data_len = data.length;
        if (left_justify)
        {
            if (data_len >= length)
            {
                System.arraycopy(data, 0, pad_data, 0, length);
            } else
            {
                System.arraycopy(data, 0, pad_data, 0, data_len);
                for (; data_len < length; data_len++)
                {
                    pad_data[data_len] = pad_byte;
                }

            }
        } else if (data_len >= length)
        {
            System.arraycopy(data, data_len - length, pad_data, 0, length);
        } else
        {
            data_len = length - data_len;
            System.arraycopy(data, 0, pad_data, data_len, data.length);
            for (data_len--; data_len >= 0; data_len--)
            {
                pad_data[data_len] = pad_byte;
            }

        }
        return pad_data;
    }

    /**
     * 
     * @param str
     * @param length
     * @param pad_char
     * @param left_justify
     * @return 
     */
    public static String resize(String str, int length, char pad_char,
            boolean left_justify)
    {
        return getString(resizeData(getData(str, Encoding.ASCII_8_BIT), length,
                (byte) pad_char, left_justify), Encoding.ASCII_8_BIT);
    }

    /**
     * 
     * @param data
     * @param pad_byte
     * @param left_justify
     * @param min_length
     * @return 
     */
    public static byte[] stripData(byte data[], byte pad_byte,
            boolean left_justify, int min_length)
    {
        int len = data.length;
        if (left_justify)
        {
            for (len--; len >= min_length && data[len] == pad_byte; len--);
            if (len < min_length)
            {
                len++;
                byte temp[] = new byte[min_length];
                System.arraycopy(data, 0, temp, 0, len);
                for (; len < min_length; len++)
                {
                    temp[len] = pad_byte;
                }

                return temp;
            } else
            {
                byte temp[] = new byte[++len];
                System.arraycopy(data, 0, temp, 0, len);
                return temp;
            }
        }
        int pos = 0;
        int end;
        for (end = len - min_length; pos < end && data[pos] == pad_byte; pos++);
        if (pos > end)
        {
            len -= pos;
            byte temp[] = new byte[min_length];
            System.arraycopy(data, pos, temp, min_length - len, len);
            min_length -= len;
            for (min_length--; min_length >= 0; min_length--)
            {
                temp[min_length] = pad_byte;
            }

            return temp;
        } else
        {
            len -= pos;
            byte temp[] = new byte[len];
            System.arraycopy(data, pos, temp, 0, len);
            return temp;
        }
    }

    /**
     * 
     * @param str
     * @param pad_char
     * @param left_justify
     * @param min_length
     * @return 
     */
    public static String strip(String str, char pad_char, boolean left_justify,
            int min_length)
    {
        return getString(stripData(getData(str, Encoding.ASCII_8_BIT),
                (byte) pad_char, left_justify, min_length), Encoding.ASCII_8_BIT);
    }

    /**
     * 
     * @param str
     * @param sep
     * @return 
     */
    public static String[] split(String str, char sep)
    {
        Vector entries = new Vector();
        int offset = 0;
        int old_offset = offset;
        do
        {
            offset = str.indexOf(sep, old_offset);
            if (offset == -1)
            {
                if (str.length() > 0)
                {
                    entries.addElement(str.substring(old_offset));
                }
                break;
            }
            entries.addElement(str.substring(old_offset, offset));
            old_offset = offset + 1;
        } while (true);
        return (String[]) (String[]) entries.toArray(new String[0]);
    }

    /**
     * 
     */
    public static String[] splitParams(String params)
    {
        if (params == null)
        {
            return null;
        }
        int len = params.length();
        char sep[] =
        {
            ' ', '\t'
        };
        char special_sep[] =
        {
            '"', "'".charAt(0)
        };
        Vector v = new Vector();
        for (int i = 0; i < len;)
        {
            int next_sep = len;
            for (int j = 0; j < sep.length; j++)
            {
                int temp = params.indexOf(sep[j], i);
                if (temp != -1 && temp < next_sep)
                {
                    next_sep = temp;
                }
            }

            int next_special_sep = len;
            for (int j = 0; j < special_sep.length; j++)
            {
                int temp = params.indexOf(special_sep[j], i);
                if (temp != -1 && temp < next_special_sep)
                {
                    next_special_sep = temp;
                }
            }

            if (next_special_sep == i)
            {
                char c = params.charAt(i);
                int end = params.indexOf(c, i + 1);
                if (end == -1)
                {
                    end = len;
                }
                v.addElement(params.substring(i + 1, end));
                i = end + 1;
            } else if (next_sep == i)
            {
                i++;
            } else
            {
                v.addElement(params.substring(i, next_sep));
                i = next_sep;
            }
        }

        String temp[] = new String[v.size()];
        for (int j = 0; j < v.size(); j++)
        {
            temp[j] = (String) v.elementAt(j);
        }

        return temp;
    }

    /**
     * 
     * @param source
     * @param destination
     * @param offset 
     */
    public static void putData(byte source[], byte destination[], int offset)
    {
        System.arraycopy(source, 0, destination, offset, source.length);
    }

    /**
     * 
     * @param data
     * @param offset
     * @param length
     * @return 
     */
    public static byte[] getData(byte data[], int offset, int length)
    {
        byte temp[] = new byte[length];
        System.arraycopy(data, offset, temp, 0, length);
        return temp;
    }

    /**
     *
     */
    private static void initializeASCIIToEBCDICMap()
    {
        ascii_to_ebcdic[0] = 0;
        ascii_to_ebcdic[1] = 1;
        ascii_to_ebcdic[2] = 2;
        ascii_to_ebcdic[3] = 3;
        ascii_to_ebcdic[4] = 55;
        ascii_to_ebcdic[5] = 45;
        ascii_to_ebcdic[6] = 46;
        ascii_to_ebcdic[7] = 47;
        ascii_to_ebcdic[8] = 22;
        ascii_to_ebcdic[9] = 5;
        ascii_to_ebcdic[10] = 37;
        ascii_to_ebcdic[11] = 11;
        ascii_to_ebcdic[12] = 12;
        ascii_to_ebcdic[13] = 13;
        ascii_to_ebcdic[14] = 14;
        ascii_to_ebcdic[15] = 15;
        ascii_to_ebcdic[16] = 16;
        ascii_to_ebcdic[17] = 17;
        ascii_to_ebcdic[18] = 18;
        ascii_to_ebcdic[19] = 19;
        ascii_to_ebcdic[20] = 60;
        ascii_to_ebcdic[21] = 61;
        ascii_to_ebcdic[22] = 50;
        ascii_to_ebcdic[23] = 38;
        ascii_to_ebcdic[24] = 24;
        ascii_to_ebcdic[25] = 25;
        ascii_to_ebcdic[26] = 63;
        ascii_to_ebcdic[27] = 39;
        ascii_to_ebcdic[28] = 28;
        ascii_to_ebcdic[29] = 29;
        ascii_to_ebcdic[30] = 30;
        ascii_to_ebcdic[31] = 31;
        ascii_to_ebcdic[32] = 64;
        ascii_to_ebcdic[33] = 90;
        ascii_to_ebcdic[34] = 127;
        ascii_to_ebcdic[35] = 123;
        ascii_to_ebcdic[36] = 91;
        ascii_to_ebcdic[37] = 108;
        ascii_to_ebcdic[38] = 80;
        ascii_to_ebcdic[39] = 125;
        ascii_to_ebcdic[40] = 77;
        ascii_to_ebcdic[41] = 93;
        ascii_to_ebcdic[42] = 92;
        ascii_to_ebcdic[43] = 78;
        ascii_to_ebcdic[44] = 107;
        ascii_to_ebcdic[45] = 96;
        ascii_to_ebcdic[46] = 75;
        ascii_to_ebcdic[47] = 97;
        ascii_to_ebcdic[48] = 240;
        ascii_to_ebcdic[49] = 241;
        ascii_to_ebcdic[50] = 242;
        ascii_to_ebcdic[51] = 243;
        ascii_to_ebcdic[52] = 244;
        ascii_to_ebcdic[53] = 245;
        ascii_to_ebcdic[54] = 246;
        ascii_to_ebcdic[55] = 247;
        ascii_to_ebcdic[56] = 248;
        ascii_to_ebcdic[57] = 249;
        ascii_to_ebcdic[58] = 122;
        ascii_to_ebcdic[59] = 94;
        ascii_to_ebcdic[60] = 76;
        ascii_to_ebcdic[61] = 126;
        ascii_to_ebcdic[62] = 110;
        ascii_to_ebcdic[63] = 111;
        ascii_to_ebcdic[64] = 124;
        ascii_to_ebcdic[65] = 193;
        ascii_to_ebcdic[66] = 194;
        ascii_to_ebcdic[67] = 195;
        ascii_to_ebcdic[68] = 196;
        ascii_to_ebcdic[69] = 197;
        ascii_to_ebcdic[70] = 198;
        ascii_to_ebcdic[71] = 199;
        ascii_to_ebcdic[72] = 200;
        ascii_to_ebcdic[73] = 201;
        ascii_to_ebcdic[74] = 209;
        ascii_to_ebcdic[75] = 210;
        ascii_to_ebcdic[76] = 211;
        ascii_to_ebcdic[77] = 212;
        ascii_to_ebcdic[78] = 213;
        ascii_to_ebcdic[79] = 214;
        ascii_to_ebcdic[80] = 215;
        ascii_to_ebcdic[81] = 216;
        ascii_to_ebcdic[82] = 217;
        ascii_to_ebcdic[83] = 226;
        ascii_to_ebcdic[84] = 227;
        ascii_to_ebcdic[85] = 228;
        ascii_to_ebcdic[86] = 229;
        ascii_to_ebcdic[87] = 230;
        ascii_to_ebcdic[88] = 231;
        ascii_to_ebcdic[89] = 232;
        ascii_to_ebcdic[90] = 233;
        ascii_to_ebcdic[91] = 173;
        ascii_to_ebcdic[92] = 224;
        ascii_to_ebcdic[93] = 189;
        ascii_to_ebcdic[94] = 95;
        ascii_to_ebcdic[95] = 109;
        ascii_to_ebcdic[96] = 121;
        ascii_to_ebcdic[97] = 129;
        ascii_to_ebcdic[98] = 130;
        ascii_to_ebcdic[99] = 131;
        ascii_to_ebcdic[100] = 132;
        ascii_to_ebcdic[101] = 133;
        ascii_to_ebcdic[102] = 134;
        ascii_to_ebcdic[103] = 135;
        ascii_to_ebcdic[104] = 136;
        ascii_to_ebcdic[105] = 137;
        ascii_to_ebcdic[106] = 145;
        ascii_to_ebcdic[107] = 146;
        ascii_to_ebcdic[108] = 147;
        ascii_to_ebcdic[109] = 148;
        ascii_to_ebcdic[110] = 149;
        ascii_to_ebcdic[111] = 150;
        ascii_to_ebcdic[112] = 151;
        ascii_to_ebcdic[113] = 152;
        ascii_to_ebcdic[114] = 153;
        ascii_to_ebcdic[115] = 162;
        ascii_to_ebcdic[116] = 163;
        ascii_to_ebcdic[117] = 164;
        ascii_to_ebcdic[118] = 165;
        ascii_to_ebcdic[119] = 166;
        ascii_to_ebcdic[120] = 167;
        ascii_to_ebcdic[121] = 168;
        ascii_to_ebcdic[122] = 169;
        ascii_to_ebcdic[123] = 192;
        ascii_to_ebcdic[124] = 79;
        ascii_to_ebcdic[125] = 208;
        ascii_to_ebcdic[126] = 161;
        ascii_to_ebcdic[127] = 7;
        ascii_to_ebcdic[128] = 32;
        ascii_to_ebcdic[129] = 33;
        ascii_to_ebcdic[130] = 34;
        ascii_to_ebcdic[131] = 35;
        ascii_to_ebcdic[132] = 36;
        ascii_to_ebcdic[133] = 21;
        ascii_to_ebcdic[134] = 6;
        ascii_to_ebcdic[135] = 23;
        ascii_to_ebcdic[136] = 40;
        ascii_to_ebcdic[137] = 41;
        ascii_to_ebcdic[138] = 42;
        ascii_to_ebcdic[139] = 43;
        ascii_to_ebcdic[140] = 44;
        ascii_to_ebcdic[141] = 9;
        ascii_to_ebcdic[142] = 10;
        ascii_to_ebcdic[143] = 27;
        ascii_to_ebcdic[144] = 48;
        ascii_to_ebcdic[145] = 49;
        ascii_to_ebcdic[146] = 26;
        ascii_to_ebcdic[147] = 51;
        ascii_to_ebcdic[148] = 52;
        ascii_to_ebcdic[149] = 53;
        ascii_to_ebcdic[150] = 54;
        ascii_to_ebcdic[151] = 8;
        ascii_to_ebcdic[152] = 56;
        ascii_to_ebcdic[153] = 57;
        ascii_to_ebcdic[154] = 58;
        ascii_to_ebcdic[155] = 59;
        ascii_to_ebcdic[156] = 4;
        ascii_to_ebcdic[157] = 20;
        ascii_to_ebcdic[158] = 62;
        ascii_to_ebcdic[159] = 255;
        ascii_to_ebcdic[160] = 65;
        ascii_to_ebcdic[161] = 170;
        ascii_to_ebcdic[162] = 74;
        ascii_to_ebcdic[163] = 177;
        ascii_to_ebcdic[164] = 159;
        ascii_to_ebcdic[165] = 178;
        ascii_to_ebcdic[166] = 106;
        ascii_to_ebcdic[167] = 181;
        ascii_to_ebcdic[168] = 187;
        ascii_to_ebcdic[169] = 180;
        ascii_to_ebcdic[170] = 154;
        ascii_to_ebcdic[171] = 138;
        ascii_to_ebcdic[172] = 176;
        ascii_to_ebcdic[173] = 202;
        ascii_to_ebcdic[174] = 175;
        ascii_to_ebcdic[175] = 188;
        ascii_to_ebcdic[176] = 144;
        ascii_to_ebcdic[177] = 143;
        ascii_to_ebcdic[178] = 234;
        ascii_to_ebcdic[179] = 250;
        ascii_to_ebcdic[180] = 190;
        ascii_to_ebcdic[181] = 160;
        ascii_to_ebcdic[182] = 182;
        ascii_to_ebcdic[183] = 179;
        ascii_to_ebcdic[184] = 157;
        ascii_to_ebcdic[185] = 218;
        ascii_to_ebcdic[186] = 155;
        ascii_to_ebcdic[187] = 139;
        ascii_to_ebcdic[188] = 183;
        ascii_to_ebcdic[189] = 184;
        ascii_to_ebcdic[190] = 185;
        ascii_to_ebcdic[191] = 171;
        ascii_to_ebcdic[192] = 100;
        ascii_to_ebcdic[193] = 101;
        ascii_to_ebcdic[194] = 98;
        ascii_to_ebcdic[195] = 102;
        ascii_to_ebcdic[196] = 99;
        ascii_to_ebcdic[197] = 103;
        ascii_to_ebcdic[198] = 158;
        ascii_to_ebcdic[199] = 104;
        ascii_to_ebcdic[200] = 116;
        ascii_to_ebcdic[201] = 113;
        ascii_to_ebcdic[202] = 114;
        ascii_to_ebcdic[203] = 115;
        ascii_to_ebcdic[204] = 120;
        ascii_to_ebcdic[205] = 117;
        ascii_to_ebcdic[206] = 118;
        ascii_to_ebcdic[207] = 119;
        ascii_to_ebcdic[208] = 172;
        ascii_to_ebcdic[209] = 105;
        ascii_to_ebcdic[210] = 237;
        ascii_to_ebcdic[211] = 238;
        ascii_to_ebcdic[212] = 235;
        ascii_to_ebcdic[213] = 239;
        ascii_to_ebcdic[214] = 236;
        ascii_to_ebcdic[215] = 191;
        ascii_to_ebcdic[216] = 128;
        ascii_to_ebcdic[217] = 253;
        ascii_to_ebcdic[218] = 254;
        ascii_to_ebcdic[219] = 251;
        ascii_to_ebcdic[220] = 252;
        ascii_to_ebcdic[221] = 186;
        ascii_to_ebcdic[222] = 174;
        ascii_to_ebcdic[223] = 89;
        ascii_to_ebcdic[224] = 68;
        ascii_to_ebcdic[225] = 69;
        ascii_to_ebcdic[226] = 66;
        ascii_to_ebcdic[227] = 70;
        ascii_to_ebcdic[228] = 67;
        ascii_to_ebcdic[229] = 71;
        ascii_to_ebcdic[230] = 156;
        ascii_to_ebcdic[231] = 72;
        ascii_to_ebcdic[232] = 84;
        ascii_to_ebcdic[233] = 81;
        ascii_to_ebcdic[234] = 82;
        ascii_to_ebcdic[235] = 83;
        ascii_to_ebcdic[236] = 88;
        ascii_to_ebcdic[237] = 85;
        ascii_to_ebcdic[238] = 86;
        ascii_to_ebcdic[239] = 87;
        ascii_to_ebcdic[240] = 140;
        ascii_to_ebcdic[241] = 73;
        ascii_to_ebcdic[242] = 205;
        ascii_to_ebcdic[243] = 206;
        ascii_to_ebcdic[244] = 203;
        ascii_to_ebcdic[245] = 207;
        ascii_to_ebcdic[246] = 204;
        ascii_to_ebcdic[247] = 225;
        ascii_to_ebcdic[248] = 112;
        ascii_to_ebcdic[249] = 221;
        ascii_to_ebcdic[250] = 222;
        ascii_to_ebcdic[251] = 219;
        ascii_to_ebcdic[252] = 220;
        ascii_to_ebcdic[253] = 141;
        ascii_to_ebcdic[254] = 142;
        ascii_to_ebcdic[255] = 223;
    }

    /**
     *
     */
    private static void initializeEBCDICToASCIIMap()
    {
        ebcdic_to_ascii[0] = 0;
        ebcdic_to_ascii[1] = 1;
        ebcdic_to_ascii[2] = 2;
        ebcdic_to_ascii[3] = 3;
        ebcdic_to_ascii[4] = 156;
        ebcdic_to_ascii[5] = 9;
        ebcdic_to_ascii[6] = 134;
        ebcdic_to_ascii[7] = 127;
        ebcdic_to_ascii[8] = 151;
        ebcdic_to_ascii[9] = 141;
        ebcdic_to_ascii[10] = 142;
        ebcdic_to_ascii[11] = 11;
        ebcdic_to_ascii[12] = 12;
        ebcdic_to_ascii[13] = 13;
        ebcdic_to_ascii[14] = 14;
        ebcdic_to_ascii[15] = 15;
        ebcdic_to_ascii[16] = 16;
        ebcdic_to_ascii[17] = 17;
        ebcdic_to_ascii[18] = 18;
        ebcdic_to_ascii[19] = 19;
        ebcdic_to_ascii[20] = 157;
        ebcdic_to_ascii[21] = 133;
        ebcdic_to_ascii[22] = 8;
        ebcdic_to_ascii[23] = 135;
        ebcdic_to_ascii[24] = 24;
        ebcdic_to_ascii[25] = 25;
        ebcdic_to_ascii[26] = 146;
        ebcdic_to_ascii[27] = 143;
        ebcdic_to_ascii[28] = 28;
        ebcdic_to_ascii[29] = 29;
        ebcdic_to_ascii[30] = 30;
        ebcdic_to_ascii[31] = 31;
        ebcdic_to_ascii[32] = 128;
        ebcdic_to_ascii[33] = 129;
        ebcdic_to_ascii[34] = 130;
        ebcdic_to_ascii[35] = 131;
        ebcdic_to_ascii[36] = 132;
        ebcdic_to_ascii[37] = 10;
        ebcdic_to_ascii[38] = 23;
        ebcdic_to_ascii[39] = 27;
        ebcdic_to_ascii[40] = 136;
        ebcdic_to_ascii[41] = 137;
        ebcdic_to_ascii[42] = 138;
        ebcdic_to_ascii[43] = 139;
        ebcdic_to_ascii[44] = 140;
        ebcdic_to_ascii[45] = 5;
        ebcdic_to_ascii[46] = 6;
        ebcdic_to_ascii[47] = 7;
        ebcdic_to_ascii[48] = 144;
        ebcdic_to_ascii[49] = 145;
        ebcdic_to_ascii[50] = 22;
        ebcdic_to_ascii[51] = 147;
        ebcdic_to_ascii[52] = 148;
        ebcdic_to_ascii[53] = 149;
        ebcdic_to_ascii[54] = 150;
        ebcdic_to_ascii[55] = 4;
        ebcdic_to_ascii[56] = 152;
        ebcdic_to_ascii[57] = 153;
        ebcdic_to_ascii[58] = 154;
        ebcdic_to_ascii[59] = 155;
        ebcdic_to_ascii[60] = 20;
        ebcdic_to_ascii[61] = 21;
        ebcdic_to_ascii[62] = 158;
        ebcdic_to_ascii[63] = 26;
        ebcdic_to_ascii[64] = 32;
        ebcdic_to_ascii[65] = 160;
        ebcdic_to_ascii[66] = 226;
        ebcdic_to_ascii[67] = 228;
        ebcdic_to_ascii[68] = 224;
        ebcdic_to_ascii[69] = 225;
        ebcdic_to_ascii[70] = 227;
        ebcdic_to_ascii[71] = 229;
        ebcdic_to_ascii[72] = 231;
        ebcdic_to_ascii[73] = 241;
        ebcdic_to_ascii[74] = 162;
        ebcdic_to_ascii[75] = 46;
        ebcdic_to_ascii[76] = 60;
        ebcdic_to_ascii[77] = 40;
        ebcdic_to_ascii[78] = 43;
        ebcdic_to_ascii[79] = 124;
        ebcdic_to_ascii[80] = 38;
        ebcdic_to_ascii[81] = 233;
        ebcdic_to_ascii[82] = 234;
        ebcdic_to_ascii[83] = 235;
        ebcdic_to_ascii[84] = 232;
        ebcdic_to_ascii[85] = 237;
        ebcdic_to_ascii[86] = 238;
        ebcdic_to_ascii[87] = 239;
        ebcdic_to_ascii[88] = 236;
        ebcdic_to_ascii[89] = 223;
        ebcdic_to_ascii[90] = 33;
        ebcdic_to_ascii[91] = 36;
        ebcdic_to_ascii[92] = 42;
        ebcdic_to_ascii[93] = 41;
        ebcdic_to_ascii[94] = 59;
        ebcdic_to_ascii[95] = 94;
        ebcdic_to_ascii[96] = 45;
        ebcdic_to_ascii[97] = 47;
        ebcdic_to_ascii[98] = 194;
        ebcdic_to_ascii[99] = 196;
        ebcdic_to_ascii[100] = 192;
        ebcdic_to_ascii[101] = 193;
        ebcdic_to_ascii[102] = 195;
        ebcdic_to_ascii[103] = 197;
        ebcdic_to_ascii[104] = 199;
        ebcdic_to_ascii[105] = 209;
        ebcdic_to_ascii[106] = 166;
        ebcdic_to_ascii[107] = 44;
        ebcdic_to_ascii[108] = 37;
        ebcdic_to_ascii[109] = 95;
        ebcdic_to_ascii[110] = 62;
        ebcdic_to_ascii[111] = 63;
        ebcdic_to_ascii[112] = 248;
        ebcdic_to_ascii[113] = 201;
        ebcdic_to_ascii[114] = 202;
        ebcdic_to_ascii[115] = 203;
        ebcdic_to_ascii[116] = 200;
        ebcdic_to_ascii[117] = 205;
        ebcdic_to_ascii[118] = 206;
        ebcdic_to_ascii[119] = 207;
        ebcdic_to_ascii[120] = 204;
        ebcdic_to_ascii[121] = 96;
        ebcdic_to_ascii[122] = 58;
        ebcdic_to_ascii[123] = 35;
        ebcdic_to_ascii[124] = 64;
        ebcdic_to_ascii[125] = 39;
        ebcdic_to_ascii[126] = 61;
        ebcdic_to_ascii[127] = 34;
        ebcdic_to_ascii[128] = 216;
        ebcdic_to_ascii[129] = 97;
        ebcdic_to_ascii[130] = 98;
        ebcdic_to_ascii[131] = 99;
        ebcdic_to_ascii[132] = 100;
        ebcdic_to_ascii[133] = 101;
        ebcdic_to_ascii[134] = 102;
        ebcdic_to_ascii[135] = 103;
        ebcdic_to_ascii[136] = 104;
        ebcdic_to_ascii[137] = 105;
        ebcdic_to_ascii[138] = 171;
        ebcdic_to_ascii[139] = 187;
        ebcdic_to_ascii[140] = 240;
        ebcdic_to_ascii[141] = 253;
        ebcdic_to_ascii[142] = 254;
        ebcdic_to_ascii[143] = 177;
        ebcdic_to_ascii[144] = 176;
        ebcdic_to_ascii[145] = 106;
        ebcdic_to_ascii[146] = 107;
        ebcdic_to_ascii[147] = 108;
        ebcdic_to_ascii[148] = 109;
        ebcdic_to_ascii[149] = 110;
        ebcdic_to_ascii[150] = 111;
        ebcdic_to_ascii[151] = 112;
        ebcdic_to_ascii[152] = 113;
        ebcdic_to_ascii[153] = 114;
        ebcdic_to_ascii[154] = 170;
        ebcdic_to_ascii[155] = 186;
        ebcdic_to_ascii[156] = 230;
        ebcdic_to_ascii[157] = 184;
        ebcdic_to_ascii[158] = 198;
        ebcdic_to_ascii[159] = 164;
        ebcdic_to_ascii[160] = 181;
        ebcdic_to_ascii[161] = 126;
        ebcdic_to_ascii[162] = 115;
        ebcdic_to_ascii[163] = 116;
        ebcdic_to_ascii[164] = 117;
        ebcdic_to_ascii[165] = 118;
        ebcdic_to_ascii[166] = 119;
        ebcdic_to_ascii[167] = 120;
        ebcdic_to_ascii[168] = 121;
        ebcdic_to_ascii[169] = 122;
        ebcdic_to_ascii[170] = 161;
        ebcdic_to_ascii[171] = 191;
        ebcdic_to_ascii[172] = 208;
        ebcdic_to_ascii[173] = 91;
        ebcdic_to_ascii[174] = 222;
        ebcdic_to_ascii[175] = 174;
        ebcdic_to_ascii[176] = 172;
        ebcdic_to_ascii[177] = 163;
        ebcdic_to_ascii[178] = 165;
        ebcdic_to_ascii[179] = 183;
        ebcdic_to_ascii[180] = 169;
        ebcdic_to_ascii[181] = 167;
        ebcdic_to_ascii[182] = 182;
        ebcdic_to_ascii[183] = 188;
        ebcdic_to_ascii[184] = 189;
        ebcdic_to_ascii[185] = 190;
        ebcdic_to_ascii[186] = 221;
        ebcdic_to_ascii[187] = 168;
        ebcdic_to_ascii[188] = 175;
        ebcdic_to_ascii[189] = 93;
        ebcdic_to_ascii[190] = 180;
        ebcdic_to_ascii[191] = 215;
        ebcdic_to_ascii[192] = 123;
        ebcdic_to_ascii[193] = 65;
        ebcdic_to_ascii[194] = 66;
        ebcdic_to_ascii[195] = 67;
        ebcdic_to_ascii[196] = 68;
        ebcdic_to_ascii[197] = 69;
        ebcdic_to_ascii[198] = 70;
        ebcdic_to_ascii[199] = 71;
        ebcdic_to_ascii[200] = 72;
        ebcdic_to_ascii[201] = 73;
        ebcdic_to_ascii[202] = 173;
        ebcdic_to_ascii[203] = 244;
        ebcdic_to_ascii[204] = 246;
        ebcdic_to_ascii[205] = 242;
        ebcdic_to_ascii[206] = 243;
        ebcdic_to_ascii[207] = 245;
        ebcdic_to_ascii[208] = 125;
        ebcdic_to_ascii[209] = 74;
        ebcdic_to_ascii[210] = 75;
        ebcdic_to_ascii[211] = 76;
        ebcdic_to_ascii[212] = 77;
        ebcdic_to_ascii[213] = 78;
        ebcdic_to_ascii[214] = 79;
        ebcdic_to_ascii[215] = 80;
        ebcdic_to_ascii[216] = 81;
        ebcdic_to_ascii[217] = 82;
        ebcdic_to_ascii[218] = 185;
        ebcdic_to_ascii[219] = 251;
        ebcdic_to_ascii[220] = 252;
        ebcdic_to_ascii[221] = 249;
        ebcdic_to_ascii[222] = 250;
        ebcdic_to_ascii[223] = 255;
        ebcdic_to_ascii[224] = 92;
        ebcdic_to_ascii[225] = 247;
        ebcdic_to_ascii[226] = 83;
        ebcdic_to_ascii[227] = 84;
        ebcdic_to_ascii[228] = 85;
        ebcdic_to_ascii[229] = 86;
        ebcdic_to_ascii[230] = 87;
        ebcdic_to_ascii[231] = 88;
        ebcdic_to_ascii[232] = 89;
        ebcdic_to_ascii[233] = 90;
        ebcdic_to_ascii[234] = 178;
        ebcdic_to_ascii[235] = 212;
        ebcdic_to_ascii[236] = 214;
        ebcdic_to_ascii[237] = 210;
        ebcdic_to_ascii[238] = 211;
        ebcdic_to_ascii[239] = 213;
        ebcdic_to_ascii[240] = 48;
        ebcdic_to_ascii[241] = 49;
        ebcdic_to_ascii[242] = 50;
        ebcdic_to_ascii[243] = 51;
        ebcdic_to_ascii[244] = 52;
        ebcdic_to_ascii[245] = 53;
        ebcdic_to_ascii[246] = 54;
        ebcdic_to_ascii[247] = 55;
        ebcdic_to_ascii[248] = 56;
        ebcdic_to_ascii[249] = 57;
        ebcdic_to_ascii[250] = 179;
        ebcdic_to_ascii[251] = 219;
        ebcdic_to_ascii[252] = 220;
        ebcdic_to_ascii[253] = 217;
        ebcdic_to_ascii[254] = 218;
        ebcdic_to_ascii[255] = 0;
    }
}
