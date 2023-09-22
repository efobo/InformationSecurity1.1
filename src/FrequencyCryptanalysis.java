import java.beans.Introspector;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;

public class FrequencyCryptanalysis {

    void decrypt (String str, int keyLength) {
        String stat;
        String alphabet;
        if (isRussian(str.charAt(0))) {
            stat = "ОЕЁАИТНСРВЛКМДПУЯЫГЗБЧЙХЪЖЬЮШЦЩЭФ";
            alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        } else {
            stat = "ETAOINSHRDLCUMWFGYPBVKJXQZ";
            alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        List<String> sortedChars = new ArrayList<>();
        final List<Map<Character, Integer>> frequency = frequencyCryptanalysis(str, keyLength);
        for (int i = 0; i< frequency.size(); i++) {
            Map<Character, Integer> map = frequency.get(i);
            String sortedString = map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).filter(c->c.getValue() > 0).map(c->c.getKey().toString()).collect(Collectors.joining());
            sortedChars.add(sortedString);
        }
    }
    String decrypt1 (String str, int keyLength) {
        String finalStr = str;
        String stat;
        String alphabet;
        if (isRussian(str.charAt(0))) {
            stat = "ОЕЁАИТНСРВЛКМДПУЯЫГЗБЧЙХЪЖЬЮШЦЩЭФ";
            alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        } else {
            stat = "ETAOINSHRDLCUMWFGYPBVKJXQZ";
            alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        List<String> sortedChars = new ArrayList<>();
        final List<Map<Character, Integer>> frequency = frequencyCryptanalysis(str, keyLength);
        for (int i = 0; i< frequency.size(); i++) {
            Map<Character, Integer> map = frequency.get(i);
            String sortedString = map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).filter(c->c.getValue() > 0).map(c->c.getKey().toString()).collect(Collectors.joining());
            sortedChars.add(sortedString);
        }

        for (int i = 0; i<sortedChars.size(); i++) {
            //System.out.println(sortedChars.get(i));
            String sortedString = sortedChars.get(i);
            int shift = Character.toUpperCase(sortedString.charAt(0)) - Character.toUpperCase(stat.charAt(0));
            if (shift < 0) shift += alphabet.length();
            finalStr = changeCharsAtPositions(finalStr, keyLength, shift, i, alphabet);

        }

        finalStr = makeNormalText(str, finalStr);



        return finalStr;
    }


    List<Map<Character, Integer>> frequencyCryptanalysis (String str, int keyLength) {
        String finalStr = "";
        str = str.replaceAll("[^A-Za-zА-Яа-я]", "");
        str = str.toUpperCase();
        String alphabet;
        if (isEnglish(str.charAt(0))) alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        else alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

        int numberOfKeyRepeat = str.length()/keyLength;
        int lastKeyNumber = str.length() % keyLength;
        List<Map<Character, Integer>> frequency = new ArrayList<Map<Character, Integer>>();
        for (int i = 0; i < keyLength; i ++) {
            Map<Character, Integer> map = fillZeros(alphabet);
            if (i < lastKeyNumber) {
                for (int j = 0; j < numberOfKeyRepeat+1; j++) {
                    char ch = str.charAt(keyLength*j + i);
                    int amount = map.get(ch) + 1;
                    map.remove(ch);
                    map.put(ch, amount);
                }
            } else {
                for (int j = 0; j < numberOfKeyRepeat; j++) {
                    char ch = str.charAt(keyLength*j + i);
                    int amount = map.get(ch) + 1;
                    map.remove(ch);
                    map.put(ch, amount);
            }

            }
            frequency.add(map);
        }

        printFrequency(frequency, keyLength, alphabet);
        return  frequency;
    }


    String changeCharsAtPositions (String str, int keyLength, int shift, int position, String alphabet) {
        str = str.replaceAll("[^A-Za-zА-Яа-я]", "");
        str = str.toUpperCase();
        int numberOfKeyRepeat = str.length()/keyLength;
        int lastKeyNumber = str.length() % keyLength;
        char[] charArray = str.toCharArray();
        for (int i = 0; i < numberOfKeyRepeat; i++) {
            int shiftedLetterNum = findPosition(charArray[keyLength*i + position], alphabet) - shift;
            if (shiftedLetterNum < 0) shiftedLetterNum += alphabet.length();
            charArray[keyLength*i + position] = alphabet.charAt(shiftedLetterNum);
        }
        if (position < lastKeyNumber) {
            int shiftedLetterNum = findPosition(charArray[keyLength*numberOfKeyRepeat + position], alphabet) - shift;
            if (shiftedLetterNum < 0) shiftedLetterNum += alphabet.length();
            charArray[keyLength*numberOfKeyRepeat + position] = alphabet.charAt(shiftedLetterNum);
        }
        return charArrayToString(charArray);
    }

    int findKeyLength (String str) {
        int finalKeyLength = 0;
        str = str.replaceAll("\\s+","");
        int strLength = str.length();
        int maxMatch = -1;
        int shiftsNum = 0;
        for (int i = 1; i < strLength; i++) {
            String shiftedStr = str.substring(strLength - i, strLength) + str.substring(0, strLength - i);
            int match = 0;
            for (int j = 0; j < strLength; j++) {
                if (str.charAt(j) == shiftedStr.charAt(j)) match++;
            }
            if (match > maxMatch) {
                shiftsNum = i;
                maxMatch =match;
            }
        }

        finalKeyLength = shiftsNum;
        return finalKeyLength;
    }


    private int findPosition (Character ch, String string) {
        int pos = -1;
        for (int i =0; i < string.length(); i++) {
            if (string.charAt(i) == ch) pos =i;
        }
        return pos;
    }

    private Map<Character, Integer> fillZeros(String alphabet) {
        Map<Character, Integer> map = new HashMap<>();
        for (int j = 0; j < alphabet.length(); j++) {
            map.put(alphabet.charAt(j), 0);
        }
        return map;
    }


    private void printFrequency(List<Map<Character, Integer>> listOfMaps, int width, String alphabet) {
        for (int i = 0; i < width; i++) {
            System.out.print("column " + i + ": [");
            Map<Character, Integer> map = listOfMaps.get(i);
            int numberOfKeyRepeat = 0;
            for (int j = 0; j < map.size(); j++) {
                System.out.print(alphabet.charAt(j) + " = "+ map.get(alphabet.charAt(j)));
                if (j != map.size() - 1) System.out.print(", ");
                numberOfKeyRepeat += map.get(alphabet.charAt(j));
            }
            System.out.println("]");
            printFrequencyFloat(map, i, numberOfKeyRepeat, alphabet);
        }
    }

    private void printFrequencyFloat(Map<Character, Integer> map, int index, int numberOfKeyRepeat, String alphabet) {
        for (int i = 0; i < alphabet.length(); i++) {
            int curNum = map.get(alphabet.charAt(i));
            if (curNum != 0) {
                float freq = (float) curNum/ numberOfKeyRepeat;
                //System.out.println(freq + " = " + map.get(alphabet.charAt(i)) + " / " + numberOfKeyRepeat);
                System.out.println(alphabet.charAt(i) + " = " + freq);
            }

        }
        System.out.println();
    }

    private String makeNormalText(String original, String decrypted) {
        int decryptedPointer = 0;
        char[] originalChar = original.toCharArray();

        for (int i = 0; i< original.length(); i++) {
            if (Character.isLowerCase(originalChar[i])) {
                if (i != 0) {
                    decrypted = decrypted.substring(0, decryptedPointer) +
                            Character.toLowerCase(decrypted.charAt(decryptedPointer)) +
                            decrypted.substring(decryptedPointer+1);
                } else decrypted = Character.toLowerCase(decrypted.charAt(decryptedPointer)) +
                        decrypted.substring(decryptedPointer+1);

            } else if (Character.isUpperCase(originalChar[i])) {
                if (i != 0) {
                    decrypted = decrypted.substring(0, decryptedPointer) +
                            Character.toUpperCase(decrypted.charAt(decryptedPointer)) +
                            decrypted.substring(decryptedPointer + 1);
                } else decrypted = Character.toUpperCase(decrypted.charAt(decryptedPointer)) +
                        decrypted.substring(decryptedPointer + 1);
            } else if (!Character.isAlphabetic(originalChar[i])) {
                if (i != 0) {
                    decrypted = decrypted.substring(0, decryptedPointer) +
                            originalChar[i] + decrypted.substring(decryptedPointer);
                } else if (i + 1 == original.length()) {
                    decrypted = decrypted + originalChar[i];
                } else {
                    decrypted = originalChar[i] + decrypted;
                }
                //decryptedPointer++;

            }

            decryptedPointer++;
        }
        return decrypted;
    }

    private String charArrayToString (char[] arr) {
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            str += arr[i];
        }
        return str;
    }


    private boolean isRussian(char ch) {
        if ((ch >= 'А' && ch <= 'я') || (ch == 'ё') || (ch == 'Ё') ) return true;
        return false;
    }

    private boolean isEnglish (char ch) {
        if ((ch >= 'A') && (ch <= 'z')) return true;
        return false;
    }
}
