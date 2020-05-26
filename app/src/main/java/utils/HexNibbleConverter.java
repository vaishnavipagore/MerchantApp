package utils;

/**
 *
 * @author Abhy
 */
class HexNibbleConverter implements NibbleToCharConverter, CharToNibbleConverter
{

    HexNibbleConverter()
    {
    }

    public int convertNibbleToChar(int nibble)
    {
        if (nibble < 10)
        {
            return nibble + 48;
        } else
        {
            return nibble + 55;
        }
    }

    public byte convertCharToNibble(byte data)
    {
        if (data >= 48 && data <= 57)
        {
            return (byte) (data & 0xf);
        }
        if (data >= 65 && data <= 70)
        {
            return (byte) (data - 55);
        }
        if (data >= 97 && data <= 102)
        {
            return (byte) (data - 87);
        } else
        {
            return 0;
        }
    }

    public char convertNibbleToChar(byte nibble)
    {
        switch (nibble)
        {
            case 0:
                return '0';

            case 1:
                return '1';

            case 2:
                return '2';

            case 3:
                return '3';

            case 4:
                return '4';

            case 5:
                return '5';

            case 6:
                return '6';

            case 7:
                return '7';

            case 8:
                return '8';

            case 9:
                return '9';

            case 10:
                return 'A';

            case 11:
                return 'B';

            case 12:
                return 'C';

            case 13:
                return 'D';

            case 14:
                return 'E';

            case 15:
                return 'F';
        }
        return '\0';
    }

    @Override
    public byte convertCharToNibble(char data)
    {
        switch (Character.toLowerCase(data))
        {
            case 48:
                return 0;

            case 49:
                return 1;

            case 50:
                return 2;

            case 51:
                return 3;

            case 52:
                return 4;

            case 53:
                return 5;

            case 54:
                return 6;

            case 55:
                return 7;

            case 56:
                return 8;

            case 57:
                return 9;

            case 97:
                return 10;

            case 98:
                return 11;

            case 99:
                return 12;

            case 100:
                return 13;

            case 101:
                return 14;

            case 102:
                return 15;

            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            default:
                return 0;
        }
    }
}
