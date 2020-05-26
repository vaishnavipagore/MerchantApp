package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import messagebus.AcquirerNodeInfo;

public final class Utility
{

    /**
     *
     * @param input
     * @param length
     * @param padchar
     * @param left_justify
     * @return
     */
    public static String resize(String input, int length, String padchar,
            boolean left_justify)
    {
        if (input == null)
        {
            input = " ";
        }

        String output = input;

        if (input.trim().length() >= length)
        {
            return input;
        }

        while (output.length() != length)
        {
            if (left_justify)
            {
                output = output + padchar;
            } else
            {
                output = padchar + output;
            }
        }

        return output;
    }

    /**
     *
     * @param msg
     * @param array
     * @param leng
     */
    public static void printByteArray(String msg, byte[] array, int leng)
    {
        byte b;

        System.out.println("\n" + msg);
        for (int i = 0; i < leng; i++)
        {
            b = array[i];

            System.out.print(byteToHex(b) + " ");

            if (i != 0)
            {
                if (((i + 1) % 8) == 0)
                {
                    System.out.println();
                }
            }
        }
        System.out.println();
    }

    /**
     * Converts from byte to hex
     *
     * @param b input data in byte format
     * @return data in hex
     */
    public static String byteToHex(byte b)
    {
        int i = b & 0xFF;
        String op = Integer.toHexString(i);

        if (op.length() == 1)
        {
            op = "0" + op;
        }
        return op;
    }

    /**
     *
     * @param inputdata
     * @return
     */
    public static ConcurrentHashMap<String, String> parsePipedString(String inputdata)
    {
        int startpos = 0;
        int pos;
        String temp;
        String[] arr;
        ConcurrentHashMap<String, String> parsedData = new ConcurrentHashMap<>();

        if (inputdata.indexOf("|", startpos) == -1)
        {
            return parsedData;
        }

        while (startpos < inputdata.length())
        {
            pos = inputdata.indexOf("|", startpos);
            if (pos == -1)
            {
                pos = inputdata.length();
            }

            temp = inputdata.substring(startpos, pos);
            arr = temp.split("=");
            parsedData.put(arr[0], arr[1]);
            startpos = pos + 1;
        }

        return parsedData;
    }

    /**
     *
     * @param inputdata
     * @return
     */
    public static ConcurrentHashMap<String, String> parseResponseString(String inputdata)
    {
        int startpos = 0;
        int pos;
        String temp;
        String[] arr;
        ConcurrentHashMap<String, String> parsedData = new ConcurrentHashMap<>();

        while (startpos < inputdata.length())
        {
            pos = inputdata.indexOf("&", startpos);

            if (pos == -1)
            {
                pos = inputdata.length();
            }
            temp = inputdata.substring(startpos, pos);
            arr = temp.split("=");
            parsedData.put(arr[0], arr[1]);
            startpos = pos + 1;
        }

        return parsedData;
    }

    /**
     *
     * @param desc
     * @param inputdata
     */
    public static void printResponseString(String desc, String inputdata)
    {
        int startpos = 0;
        int pos;
        String temp;

        System.out.println(desc + " : " + inputdata.length());
        while (startpos < inputdata.length())
        {
            pos = inputdata.indexOf("&", startpos);

            if (pos == -1)
            {
                pos = inputdata.length();
            }
            temp = inputdata.substring(startpos, pos);

            System.out.println(" -- " + temp);

            startpos = pos + 1;
        }
    }

    /**
     *
     * @param amount
     * @return
     */
    public static String formatAmount(String amount)
    {
        String formattedAmount;
        if (Integer.parseInt(amount) > 0)
        {
            formattedAmount = Integer.toString(Integer.parseInt(amount));
            formattedAmount = formattedAmount.substring(0,
                    formattedAmount.length() - 2)
                    + "."
                    + formattedAmount.substring(formattedAmount.length() - 2,
                            formattedAmount.length());
        } else
        {
            formattedAmount = "0.00";
        }

        return formattedAmount;
    }

    /**
     *
     * @param input
     * @return
     */
    public static String formatDateTime(String input)
    {
        Calendar cldr = Calendar.getInstance();
        String formattedText;
        String date_o = input.substring(0, 4);
        String time_o = input.substring(4, 10);

        date_o = date_o.substring(2, 4) + "/" + date_o.substring(0, 2) + "/"
                + cldr.get(Calendar.YEAR);
        time_o = time_o.substring(0, 2) + ":" + time_o.substring(2, 4) + ":"
                + time_o.substring(4, 6);

        formattedText = date_o + " " + time_o;

        return formattedText;
    }

    /**
     *
     * @param input
     * @return
     */
    public static String formatDate(String input)
    {
        Calendar cldr = Calendar.getInstance();
        String formattedText = "";

        if (input.length() == 4)
        {
            formattedText = input.substring(2, 4) + "/" + input.substring(0, 2)
                    + "/" + cldr.get(Calendar.YEAR);
        } else if (input.length() == 8)
        {
            formattedText = input.substring(6, 8) + "/" + input.substring(4, 6)
                    + "/" + input.substring(0, 4);
        }

        return formattedText;
    }

    /**
     *
     * @param input
     * @return
     */
    public static String formatTime(String input)
    {
        String time_o = input;

        time_o = time_o.substring(0, 2) + ":" + time_o.substring(2, 4) + ":"
                + time_o.substring(4, 6);

        return time_o;
    }

    /**
     *
     * @param minuteoffset
     * @return
     */
    public static String getDate(int minuteoffset)
    {
        Calendar cldr = Calendar.getInstance();

        cldr.add(Calendar.MINUTE, minuteoffset);

        return Utility.resize(Integer.toString(cldr.get(Calendar.YEAR)), 4, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.MONTH) + 1), 2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.DATE)), 2, "0", false);
    }

    /**
     *
     * @return
     */
    public static String getDate()
    {
        Calendar cldr = Calendar.getInstance();

        return Utility.resize(Integer.toString(cldr.get(Calendar.YEAR)), 4, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.MONTH) + 1), 2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.DATE)), 2, "0", false);
    }

    /**
     *
     * @return
     */
    public static String getFormattedDateTime()
    {
        String formattedtext;
        String dt = getDate();
        String time = getTime();

        formattedtext = formatDate(dt) + " - " + formatTime(time);

        return formattedtext;
    }

    /**
     *
     * @return
     */
    public static String getTime()
    {
        Calendar cldr = Calendar.getInstance();

        return Utility.resize(Integer.toString(cldr.get(Calendar.HOUR_OF_DAY)),
                2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.MINUTE)),
                        2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.SECOND)),
                        2, "0", false);
    }

    /**
     *
     * @param minuteoffset
     * @return
     */
    public static String getTime(int minuteoffset)
    {
        Calendar cldr = Calendar.getInstance();

        cldr.add(Calendar.MINUTE, minuteoffset);

        return Utility.resize(Integer.toString(cldr.get(Calendar.HOUR_OF_DAY)),
                2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.MINUTE)),
                        2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.SECOND)),
                        2, "0", false);
    }

    /**
     *
     * @return
     */
    public static String getDateTime()
    {
        String datetime;
        Calendar cldr = Calendar.getInstance();

        datetime = Utility.resize(
                Integer.toString(cldr.get(Calendar.MONTH) + 1), 2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.DATE)), 2,
                        "0", false)
                + Utility.resize(
                        Integer.toString(cldr.get(Calendar.HOUR_OF_DAY)), 2,
                        "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.MINUTE)),
                        2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.SECOND)),
                        2, "0", false);

        return datetime;
    }

    /**
     *
     * @param offset
     * @return
     */
    public static String getDateTime(int offset)
    {
        String datetime;
        Calendar cldr = Calendar.getInstance();

        cldr.add(Calendar.MINUTE, offset);

        datetime = Utility.resize(
                Integer.toString(cldr.get(Calendar.MONTH) + 1), 2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.DATE)), 2,
                        "0", false)
                + Utility.resize(
                        Integer.toString(cldr.get(Calendar.HOUR_OF_DAY)), 2,
                        "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.MINUTE)),
                        2, "0", false)
                + Utility.resize(Integer.toString(cldr.get(Calendar.SECOND)),
                        2, "0", false);

        return datetime;
    }

    /**
     *
     * @param d
     * @param pattern
     * @return String
     */
    public static String formatDate(Date d, String pattern)
    {
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        df.applyPattern(pattern);
        return df.format(d);
    }

    /**
     * You should use this version of formatDate() if you want a specific
     * timeZone to calculate the date on.
     *
     * @param d
     * @param pattern
     * @param timeZone for GMT for example, use TimeZone.getTimeZone("GMT") and
     * for Uruguay use TimeZone.getTimeZone("GMT-03:00")
     * @return String
     */
    public static String formatDate(Date d, String pattern, TimeZone timeZone)
    {
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        df.setTimeZone(timeZone);
        df.applyPattern(pattern);
        return df.format(d);
    }

    /**
     * @param d date object to be formatted
     * @return date in YDDD format suitable for bit 31 or 37 depending on
     * interchange
     */
    public static String getJulianDate(Date d)
    {
        String day = formatDate(d, "DDD", TimeZone.getDefault());
        String year = formatDate(d, "yy", TimeZone.getDefault());
        year = year.substring(1);
        return year + day;
    }

    /**
     * @param d date object to be formatted
     * @param timeZone for GMT for example, use TimeZone.getTimeZone("GMT") and
     * for Uruguay use TimeZone.getTimeZone("GMT-03:00")
     * @return date in YDDD format suitable for bit 31 or 37 depending on
     * interchange
     */
    public static String getJulianDate(Date d, TimeZone timeZone)
    {
        String day = formatDate(d, "DDD", timeZone);
        String year = formatDate(d, "yy", timeZone);
        year = year.substring(1);
        return year + day;
    }

    /**
     *
     * @param cardnumber
     * @return
     */
    public static String getMaskedCardNumber(String cardnumber)
    {
        if (cardnumber == null)
        {
            return "";
        }
        String maskedcardnumber;
        int len = cardnumber.length();

        maskedcardnumber = cardnumber.substring(0, 6)
                + resize(cardnumber.substring(len - 4, len), len - 6, "*",
                        false);

        return maskedcardnumber;
    }

    /**
     *
     * @param input
     * @param size
     * @return
     */
    public static String centerText(String input, int size)
    {
        String output = input;
        int leng = input.length();
        int space;

        if (leng >= size)
        {
            return output;
        }

        space = ((size - leng) / 2);

        output = resize(input, leng + space, " ", false);

        return output;
    }

    /**
     *
     * @param buf
     * @return
     */
    public static String fromByteArrayToHex(byte buf[])
    {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++)
        {
            if (((int) buf[i] & 0xff) < 0x10)
            {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    /**
     *
     * @param inpBytes
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String inpBytes, PublicKey publicKey)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] inpByteArr = inpBytes.getBytes();
        byte[] finalCipher = cipher.doFinal(inpByteArr);

        return Translate.fromBinToHex(Translate.getString(finalCipher));
    }

    /**
     *
     * @param strpublickey
     * @return
     */
    public static PublicKey getPublicKeyFromString(String strpublickey)
    {
        strpublickey = Translate.fromHexToBin(strpublickey);
        PublicKey publicKey = null;

        KeyFactory keyFactory;
        try
        {
            keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                    Translate.getData(strpublickey));
            publicKey
                    = keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            debugPrint("getPublicKeyFromString", e.getMessage());
        }

        return publicKey;
    }

    /**
     *
     * @param hdr
     * @param msg
     */
    public static void debugPrint(String hdr, String msg)
    {
        System.out.printf("\n[" + Utility.getFormattedDateTime() + "] - [Debug Print] - [%s] - [%s]\n", hdr, msg);
    }

    /**
     *
     * @param data
     * @return
     */
    public static long converttolong(String data)
    {
        if (data.trim().length() == 0)
        {
            data = "0";
        }

        return Long.parseLong(data);
    }

    /**
     *
     * @param envvar
     * @return
     */
    public static String getEnvVar(String envvar)
    {
        String envvalue = System.getenv(envvar);

        return envvalue;
    }

    /**
     *
     * @param num
     * @return
     */
    public static boolean isOdd(int num)
    {
        boolean isodd;
        int rem = num % 2;

        isodd = rem != 0;

        return isodd;
    }

    /**
     * Removes the unwanted characters from the desired position.
     *
     * @param str The actual string
     * @param padRight The position where data is padded
     * @param act_len The actual length of the string
     * @return
     */
    public static String removePadChars(String str, boolean padRight,
            int act_len)
    {
        int startpos;
        int endpos;

        if (act_len > str.length())
        {
            return str;
        }

        if (!padRight)
        {
            endpos = str.length();
            startpos = 0 + (endpos - act_len);
        } else
        {
            startpos = 0;
            endpos = act_len;
        }
        str = str.substring(startpos, endpos);

        return str;
    }

    /**
     *
     * @param s
     * @return
     */
    public static int hex2decimal(String s)
    {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    /**
     *
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha1(String input) throws NoSuchAlgorithmException
    {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++)
        {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /**
     *
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSHA256(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        if (null == input)
        {
            return null;
        }

        byte[] md_byte = md.digest(input.getBytes(StandardCharsets.UTF_8));

        String op = Base64.getEncoder().encodeToString(md_byte);

        return op;
    }

    /**
     *
     * @param data
     * @return
     */
    public static String sanitizeData(String data)
    {
        StringBuilder str = new StringBuilder();
        int c;

        for (int i = 0; i < data.length(); i++)
        {
            c = data.codePointAt(i);

            if (c < 31 || c > 126)
            {
                c = 63;
            }

            str.append(Character.toChars(c));
        }

        return str.toString();
    }

    /**
     *
     * @param istDate
     * @return
     * @throws ParseException
     */
    public static String getGMTDateTime(String istDate)
            throws ParseException
    {
        SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2, Integer.parseInt(istDate.substring(0, 2)) - 1);
        calendar.set(5, Integer.parseInt(istDate.substring(2, 4)));
        calendar.set(11, Integer.parseInt(istDate.substring(4, 6)));
        calendar.set(12, Integer.parseInt(istDate.substring(6, 8)));
        calendar.set(13, Integer.parseInt(istDate.substring(8)));
        TimeZone gmtTime = TimeZone.getTimeZone("GMT");
        df.setTimeZone(gmtTime);
        return df.format(calendar.getTime());
    }

    /**
     * Mod 10 / LUHN implementation
     *
     * @param check_str
     * @return
     */
    public static String calcCheckDigit(String check_str)
    {
        int nb = check_str.length();
        byte check[] = Translate.getData(check_str);
        boolean even = false;
        int sum = 0;
        for (int i = nb - 1; i != -1; i--)
        {
            int digit = check[i] - 48;
            if (even)
            {
                sum += digit;
            } else
            {
                sum += (2 * digit) % 10 + digit / 5;
            }
            even = !even;
        }

        if (sum % 10 == 0)
        {
            return "0";
        } else
        {
            return Integer.toString(10 - sum % 10);
        }
    }

    /**
     *
     * @param trandatetime
     * @return
     */
    public static String getYDDD(String trandatetime)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(trandatetime.substring(0, 4)),
                Integer.parseInt(trandatetime.substring(4, 6)),
                Integer.parseInt(trandatetime.substring(6, 8)));

        String y = Integer.toString(cal.get(1) % 10);
        String ddd = Utility.resize(Integer.toString(cal.get(6)), 3, "0", false);

        return (new StringBuilder()).append(y).append(ddd).toString();
    }

    /**
     *
     * @param connurl
     * @param timeout
     * @param request
     * @return
     */
    public static String sendAndRxHTTPMsg(String connurl, int timeout, String request)
    {
        String response;
        try
        {
            URL url = new URL(connurl);
            URLConnection uconn = url.openConnection();
            uconn.setReadTimeout(timeout);
            uconn.setDoInput(true);
            uconn.setDoOutput(true);

            try ( DataOutputStream dos = new DataOutputStream(uconn.getOutputStream()))
            {
                dos.writeBytes(request);
                dos.flush();

                try ( DataInputStream dis = new DataInputStream(uconn.getInputStream()))
                {
                    StringBuilder str = new StringBuilder();
                    int x;

                    while ((x = dis.read()) != -1)
                    {
                        str.append((char) x);
                    }

                    response = str.toString();
                }
            }
        } catch (SocketTimeoutException stex)
        {
            Logger.getLogger(Utility.class.getName()).log(
                    Level.SEVERE, null, stex);
            return "TO";
        } catch (IOException ex)
        {
            Logger.getLogger(Utility.class.getName()).log(
                    Level.SEVERE, null, ex);
            return "IO";
        }

        return response;

    }

    /**
     *
     * @param ip
     * @param port
     * @param data
     * @param timeout
     * @return
     */
    public static byte[] sendAndReceive2ByteHeaderData(String ip, int port, byte[] data, int timeout)
    {
        byte[] resp;
        byte[] newdata = new byte[data.length + 2];
        byte[] len_byte;

        int rsp_len;
        int data_len = data.length;

        Socket clientSocket;
        DataInputStream dis;
        DataOutputStream dos;

        try
        {
            clientSocket = new Socket(ip, port);

            clientSocket.setSoTimeout(timeout);

            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());

            len_byte = Translate.getData(Translate.fromHexToBin(Utility.resize(
                    Integer.toHexString(data_len), 4, "0", false)));

            System.arraycopy(len_byte, 0, newdata, 0, len_byte.length);
            System.arraycopy(data, 0, newdata, 2, data.length);

            dos.write(newdata);
            dos.flush();

            rsp_len = dis.readUnsignedShort();
            if (rsp_len > 0)
            {
                resp = new byte[rsp_len];
                dis.readFully(resp, 0, rsp_len);
            } else
            {
                resp = null;
            }

            dis.close();
            dos.close();
            clientSocket.close();
        } catch (IOException ex)
        {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return resp;
    }

    /**
     *
     * @param connurl
     * @param timeout
     * @param request
     * @param contenttype
     * @return
     */
    public static String sendAndRxHTTPMsg(String connurl, int timeout, String request, String contenttype)
    {
        String response;
        try
        {
            URL url = new URL(connurl);
            HttpURLConnection uconn = (HttpURLConnection) url.openConnection();
            uconn.setRequestMethod("POST");
            uconn.setReadTimeout(timeout);
            uconn.setDoInput(true);
            uconn.setDoOutput(true);
            uconn.setRequestProperty("Content-Type", contenttype);
            uconn.setRequestProperty("Accept", contenttype);

            try ( DataOutputStream dos = new DataOutputStream(uconn.getOutputStream()))
            {
                dos.writeBytes(request);
                dos.flush();

                if (uconn.getResponseCode() == 200)
                {
                    try ( DataInputStream dis = new DataInputStream(uconn.getInputStream()))
                    {
                        StringBuilder str = new StringBuilder();
                        int x;

                        while ((x = dis.read()) != -1)
                        {
                            str.append((char) x);
                        }

                        response = str.toString();
                    }
                } else
                {
                    Logger.getGlobal().log(Level.SEVERE, "Received - {0} / {1}",
                            new Object[]
                            {
                                uconn.getResponseCode(), uconn.getResponseMessage()
                            });

                    response = "IO";
                }
            }
        } catch (ConnectException cx)
        {
            Logger.getLogger(Utility.class.getName()).log(
                    Level.SEVERE, null, cx);
            return "IO";
        } catch (SocketTimeoutException stex)
        {
            Logger.getLogger(Utility.class.getName()).log(
                    Level.SEVERE, null, stex);
            return "TO";
        } catch (IOException ex)
        {
            Logger.getLogger(Utility.class.getName()).log(
                    Level.SEVERE, null, ex);
            return "IO";
        }

        return response;
    }

    /**
     *
     * @param connurl
     * @param timeout
     * @param request
     * @param hdrs
     * @return
     */
    public static String sendAndRxHTTPMsg(String connurl, int timeout, String request, HashMap<String, String> hdrs)
    {
        String response;
        try
        {
            URL url = new URL(connurl);
            HttpURLConnection uconn = (HttpURLConnection) url.openConnection();
            uconn.setRequestMethod("POST");
            uconn.setReadTimeout(timeout);
            uconn.setDoInput(true);
            uconn.setDoOutput(true);

            hdrs.forEach((key, value) -> uconn.setRequestProperty(key, value));

            try ( DataOutputStream dos = new DataOutputStream(uconn.getOutputStream()))
            {
                dos.writeBytes(request);
                dos.flush();

                try ( DataInputStream dis = new DataInputStream(uconn.getInputStream()))
                {
                    if (uconn.getResponseCode() == 200)
                    {
                        StringBuilder str = new StringBuilder();
                        int x;

                        while ((x = dis.read()) != -1)
                        {
                            str.append((char) x);
                        }

                        response = str.toString();
                    } else
                    {
                        Logger.getGlobal().log(Level.SEVERE, "Received - {0} / {1}",
                                new Object[]
                                {
                                    uconn.getResponseCode(), uconn.getResponseMessage()
                                });

                        response = "IO";
                    }
                }
            }
        } catch (SocketTimeoutException stex)
        {
            Logger.getLogger(Utility.class.getName()).log(
                    Level.SEVERE, null, stex);
            return "TO";
        } catch (IOException ex)
        {
            Logger.getLogger(Utility.class.getName()).log(
                    Level.SEVERE, null, ex);
            return "IO";
        }

        return response;
    }

    /**
     *
     * @param ip
     * @param port
     * @param data
     * @param timeout
     * @param delim
     * @return
     */
    public static String sendAndReceiveDeLimHeaderData(String ip, int port, String data, int timeout, String delim)
    {
        StringBuilder newdata = new StringBuilder();
        boolean flag = true;
        byte rsp_data;

        Socket clientSocket;
        DataInputStream dis;
        DataOutputStream dos;

        try
        {
            clientSocket = new Socket(ip, port);

            clientSocket.setSoTimeout(timeout);

            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());

            dos.write(data.getBytes());
            dos.flush();

            while (flag)
            {
                rsp_data = dis.readByte();
                newdata.append(Byte.toString(rsp_data));
                if (newdata.toString().contains(delim))
                {
                    flag = false;
                }
            }

            dis.close();
            dos.close();
            clientSocket.close();
        } catch (IOException ex)
        {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return newdata.toString();
    }

    /**
     *
     * @param conn
     * @param pname
     * @return
     * @throws java.sql.SQLException
     */
    public static ConcurrentHashMap<String, AcquirerNodeInfo> loadAcquirerNodeInfo(Connection conn, String pname) throws SQLException
    {
        ConcurrentHashMap<String, AcquirerNodeInfo> hm_acquirernodes = new ConcurrentHashMap<>();

        String query = "{cm_getacquirernodeinfo(?)}";

        CallableStatement ps;
        ResultSet rs;

        ps = conn.prepareCall(query);
        ps.setString(1, pname);
        rs = ps.executeQuery();

        while (rs.next())
        {
            hm_acquirernodes.put(rs.getString("acq_node_name"),
                    new AcquirerNodeInfo(rs.getString("acq_node_name"), rs.getString("pname"),
                            rs.getInt("timeout"), rs.getString("listenip"), rs.getInt("listenport"), rs.getInt("backlog"),
                            rs.getString("acq_keyname")));
        }

        rs.close();
        ps.close();
        conn.close();

        return hm_acquirernodes;
    }
}
