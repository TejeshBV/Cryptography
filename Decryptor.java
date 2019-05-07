//-----------------------------------------------------------------------------------
// Decrypt a single character or a pair of characters.
//-----------------------------------------------------------------------------------
private string Decrypt(ref string _inChar)
{
    string _out = string.Empty;

    // public key encryption?
    if (radioButton_public_key.Checked)
    {
        if (_inChar == "\n")
            return _inChar;

        input_p = BigInteger.Parse(_inChar);
        char _mod = (char)BigInteger.ModPow(input_p, private_key, public_key_n);
        return _mod.ToString();
    }

    // is the input character a digit 0 - 9?
    if (digits_table.Contains(_inChar[0]))
    {
        // are we processing unencoded digits?
        if (unencoded_digits)
        {
            // yes, unencoded digits
            _inChar = _inChar.Substring(0, 1);
            return _inChar;
        }
        else
        {
            // no, these are cipher digits
            // get two characters
            // parse the ASCII number
            int _cipherNumber;
            if (int.TryParse(_inChar.Substring(0, 2), out _cipherNumber))
            {
                // reverse lookup in dictionary
                if (encryption_table.ContainsValue(_cipherNumber))
                {
                    _inChar = _inChar.Substring(0, 2);
                    return encryption_table.FirstOrDefault(x => x.Value == _cipherNumber).Key.ToString();
                }
            }
        }
    }
    // is this a normally unencrypted character?
    if (unencrypted_char_table.Contains(_inChar[0]))
    {
        // unencoded character
        _inChar = _inChar.Substring(0, 1);
        return _inChar;
    }
    // is this the enclosure for unencrypted digits?
    else if (_inChar[0] == '^')
    {
        // see if there is another carat
        if (_inChar.Substring(0, 2) == "^^")
        {
            // flip the unencoded state
            unencoded_digits = !unencoded_digits;
            // discard the first two carats, return the third character
            // skip over the first two carats
            return _inChar.Substring(2, 1);
        }
    }

    _inChar = _inChar.Substring(0, 1);
    return _inChar;
}
