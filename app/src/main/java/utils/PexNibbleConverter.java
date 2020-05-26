package utils;

/**
 *
 * @author Abhy
 */
class PexNibbleConverter implements NibbleToCharConverter, CharToNibbleConverter
{

    PexNibbleConverter()
    {
    }

    public int convertNibbleToChar(int nibble)
    {
        return nibble + 48;
    }

    public byte convertCharToNibble(byte data)
    {
        if (data >= 48 && data <= 63)
        {
            return (byte) (data & 0xf);
        } else
        {
            return 0;
        }
    }

    public char convertNibbleToChar(byte nibble)
    {
        if (nibble >= 0 && nibble <= 15)
        {
            return (char) convertNibbleToChar(nibble);
        } else
        {
            return '\0';
        }
    }

    public byte convertCharToNibble(char data)
    {
        return convertCharToNibble((byte) data);
    }
}
