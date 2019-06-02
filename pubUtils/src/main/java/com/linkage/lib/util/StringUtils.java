package com.linkage.lib.util;

public class StringUtils {

	public static boolean isEnglishLetter(char c)
    {
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
