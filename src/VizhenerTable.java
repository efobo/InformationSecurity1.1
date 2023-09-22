import java.util.Map;

import static java.util.Map.entry;

public class VizhenerTable {
    final String ALPHABET_RUS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    final String ALPHABET_ENG = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    String decryptString (String str, String keyWord) {
        String finalStr = "";
        int keyWordLen = keyWord.length();
        int keyNumPointer = 0;

        for (int i = 0; i < str.length(); i++) {
            char curChar = str.charAt(i);
            if (!Character.isAlphabetic(curChar)) {
                finalStr += curChar;
                continue;
            }
            if (!isRussian(curChar) && !isEnglish(curChar)) {
                finalStr = "-";
                continue;
            }
            finalStr += decryptChar(curChar, keyWord.charAt(keyNumPointer % keyWordLen));
            keyNumPointer++;
        }

        return finalStr;
    }

    String encryptString(String str, String keyWord) {
        String finalStr = "";
        int keyWordLen = keyWord.length();
        int keyNumPointer = 0;

        for (int i = 0; i < str.length(); i++) {
            char curChar = str.charAt(i);
            if (!Character.isAlphabetic(curChar)) {
                finalStr += curChar;
                continue;
            }
            if (!isRussian(curChar) && !isEnglish(curChar)) {
                finalStr = "-";
                continue;
            }
            finalStr += encryptChar(curChar, keyWord.charAt(keyNumPointer % keyWordLen));
            keyNumPointer++;
        }
        return finalStr;
    }

    private char decryptChar (char wordChar, char keyChar) {
        char finalChar = '-';
        String alphabet = "";
        if (isRussian(wordChar)) alphabet = ALPHABET_RUS;
        else alphabet = ALPHABET_ENG;
        keyChar = Character.toUpperCase(keyChar);

        int alphabetWordCharNum = findPosition(Character.toUpperCase(wordChar), alphabet);
        int alphabetCodeCharNum = findPosition(keyChar, alphabet);

        int finalCharNum = alphabetWordCharNum - alphabetCodeCharNum;

        if (finalCharNum < 0) finalCharNum += alphabet.length();
        finalChar = alphabet.charAt(finalCharNum);
        if (Character.isLowerCase(wordChar)) finalChar = Character.toLowerCase(finalChar);
        else finalChar = Character.toUpperCase(finalChar);
        return finalChar;

    }

    private char encryptChar (char wordChar, char keyChar) {
        char finalChar = '-';
        String alphabet = "";
        if (isRussian(wordChar)) alphabet = ALPHABET_RUS;
        else alphabet = ALPHABET_ENG;
        keyChar = Character.toUpperCase(keyChar);

        int alphabetWordCharNum = findPosition(Character.toUpperCase(wordChar), alphabet);
        int alphabetCodeCharNum = findPosition(keyChar, alphabet);

        int finalCharNum = alphabetWordCharNum + alphabetCodeCharNum;
        if (finalCharNum >= alphabet.length()) finalCharNum -= alphabet.length();
        finalChar = alphabet.charAt(finalCharNum);
        if (Character.isLowerCase(wordChar)) finalChar = Character.toLowerCase(finalChar);
        else finalChar = Character.toUpperCase(finalChar);

        return finalChar;
    }


    private boolean isRussian(char ch) {
        if ((ch >= 'А' && ch <= 'я') || (ch == 'ё') || (ch == 'Ё') ) return true;
        return false;
    }

    private boolean isEnglish (char ch) {
        if ((ch >= 'A') && (ch <= 'z')) return true;
        return false;
    }

    private int findPosition (Character ch, String string) {
        int pos = -1;
        for (int i =0; i < string.length(); i++) {
            if (string.charAt(i) == ch) pos =i;
        }
        return pos;
    }
}
