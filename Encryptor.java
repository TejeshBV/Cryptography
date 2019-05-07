//-----------------------------------------------------------------------------------
// Encrypt a single character
//-----------------------------------------------------------------------------------
private string Encrypt(string _inChar)
{
    string _out = "\n";

    // public key encryption?
    if (radioButton_public_key.Checked)
    {
        input_p = _inChar[0];
        BigInteger _mod = BigInteger.ModPow(input_p, public_key_e, public_key_n);
        return _mod.ToString("00000");
    }

    // currently encrypting digits?
    if (unencoded_digits)
    {
        // we are currently processing unencrypted digits
        // is the input character a digit?
        if (digits_table.Contains(_inChar))
        {
            // yes, return the digit character
            _out = _inChar;
        }
        else
        {
            // no, the input character is not a digit - we have reached the end of the digit sequence
            unencoded_digits = false;
            // end the unencoded digits sequence
            _out = "^^";
            // is this a normally unencrypted character?
            if (unencrypted_char_table.Contains(_inChar))
            {
                // yes, add the character to the output
                _out += _inChar;
            }
            // it's not a normally unencrypted character - should this character be encrypted?
            else if (encryption_table.ContainsKey(_inChar.ToString()))
            {
                // yes, lookup cipher character in the dictionary
                _out += encryption_table[_inChar].ToString();
            }
        }
    }
    else  // not currently encrypting digits
    {
        // is the current character a digit?
        if (digits_table.Contains(_inChar))
        {
            // begin the unencoded digit sequence
            unencoded_digits = true;
            _out = "^^" + _inChar;
        }
        // is this a normally unencrypted character?
        else if (unencrypted_char_table.Contains(_inChar))
        {
            _out = _inChar;
        }
        // should this character be encrypted?
        else if (encryption_table.ContainsKey(_inChar.ToString()))
        {
            // lookup cipher character(s) in the dictionary
            _out = encryption_table[_inChar].ToString();
        }
    }
    if (unencoded_digits)
    {
        // is the digit the last character in the input?
        string _inText = GetControlProperty(richTextBox_input, "Text").ToString();
        // peek into the future
        if (inputCursor + _inChar.Length >= _inText.Length)
        {
            // yes, it is the last character
            _out += "^^";
        }
    }
    return _out;
}